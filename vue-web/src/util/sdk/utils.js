import ethABI from 'ethereumjs-abi';
const BN = require('bn.js');
import utils_web3 from "@/util/web3/index";
// import truffle_contract from "@truffle/contract";
import store from "@/store";
import constants from './constants'

const SolidityTypes = {
  Address: "address",
  Uint256: "uint256",
  Uint8: "uint8",
  Uint: "uint",
  Bytes: "bytes",
  String: "string"
}

function bigNumberToBN(value){
  return new BN(value.toString(), 10);
}

const assetTypes = [0,1,2,3,4, 5]
function isAssetTypes(assetType){
  for(var i = 0; i < assetTypes.length; i++){
    if(assetTypes[i] == assetType) return true;
  }
  return false;
}

function getOrderKey(asset){
  if(!isAssetTypes(asset.sellType)) return false;
  if(!isAssetTypes(asset.buyType)) return false;
  let orderKey = {
    owner: asset.owner,
    salt: asset.salt,
    sellAsset: {
      token: asset.sellToken,
      tokenId: asset.sellTokenId,
      assetType: asset.sellType,
    },
    buyAsset: {
      token: asset.buyToken,
      tokenId: asset.buyTokenId,
      assetType: asset.buyType,
    }
  }
  return orderKey;
}

function createOrder(asset){
  let orderKey = getOrderKey(asset);
  if(!orderKey) return;
  let order = {
    key: orderKey,
    selling: asset.sellValue,
    buying: asset.buyValue,
    sellerFee: asset.sellerFee,
  }
  return order;
}

function tupleOrderKey(orderKey){
  return [
    orderKey.owner,
    orderKey.salt,
    [
      orderKey.sellAsset.token,
      orderKey.sellAsset.tokenId,
      orderKey.sellAsset.assetType,
    ],[
      orderKey.buyAsset.token,
      orderKey.buyAsset.tokenId,
      orderKey.buyAsset.assetType,
    ]
  ]
}

function tupleOrder(order){
  return [
    tupleOrderKey(order.key),
    order.selling,
    order.buying,
    order.sellerFee
  ]
}


function generateOrderHash(order){
  let sellA = [
    {value: order.orderkey.sellAsset.token, type: SolidityTypes.Address},
    {value: order.orderkey.sellAsset.tokenId, type: SolidityTypes.Uint},
    {value: order.orderkey.sellAsset.assetType, type: SolidityTypes.Uint8},
  ];
  let sellB = [
    {value: order.orderkey.buyAsset.token, type: SolidityTypes.Address},
    {value: order.orderkey.buyAsset.tokenId, type: SolidityTypes.Uint},
    {value: order.orderkey.buyAsset.assetType, type: SolidityTypes.Uint8},
  ];
  const typesA = _.map(sellA, o => o.type);
  const valuesA = _.map(sellA, o => o.value);
  const hashBufA = ethABI.soliditySHA3(typesA, valuesA);
  const typesB = _.map(sellB, o => o.type);
  const valuesB = _.map(sellB, o => o.value);
  const hashBufB = ethABI.soliditySHA3(typesB, valuesB);
  let orderKey = [
    {value: order.orderkey.owner, type: SolidityTypes.Address},
    {value: bigNumberToBN(order.orderkey.salt), type: SolidityTypes.Uint},
    {value: new Buffer(hashBufA, 'hex'), type: SolidityTypes.Bytes},
    {value: new Buffer(hashBufB, 'hex'), type: SolidityTypes.Bytes},
  ]
  const typesKey = _.map(orderKey, o => o.type);
  const valuesKey = _.map(orderKey, o => o.value);
  const hashBufKey = ethABI.soliditySHA3(typesKey, valuesKey);
  let _order = [
    {value: new Buffer(hashBufKey, 'hex'), type: SolidityTypes.Bytes},
    {value: order.selling, type: SolidityTypes.Uint},
    {value: order.buying, type: SolidityTypes.Uint},
    {value: order.sellerFee, type: SolidityTypes.Uint},
  ];
  const typesOrder = _.map(_order, o => o.type);
  const valuesOrder = _.map(_order, o => o.value);
  const hashBufOrder = ethABI.soliditySHA3(typesOrder, valuesOrder);
  var eth_util = require("ethereumjs-util");
  return eth_util.bufferToHex(hashBufOrder);
}

function contractAbi(type){
  let file = null;
  switch(type){
    case "ERC721":
      file = require('./abi/NFT721.json')
      break;
    case "ERC20":
      file = require("./abi/IERC20.json")
      break;
    case "EXCHANGE":
      file = require("./abi/NftExchange.json");
      break;
    case "NFT":
      file = require("./abi/NFT.json");
      break;
  }
  return file || {};
}


const calcGas = async (web3, key, args, lastArg, ts) => {
  var block = await web3.eth.getBlock("latest");
  var lastBlock = block.number;
  var gasTracker = store.state.gasTracker;
  let gasPrice = await web3.eth.getGasPrice();
  if (
    gasTracker &&
    parseFloat(gasTracker.lastBlock) > parseFloat(lastBlock - 50)
  ) {
    if(gasPrice < gasTracker.medium){
      gasPrice = gasTracker.medium;
    }
  }

  const gas = await new Promise((resolve, reject) => {
    ts.estimateGas(
      {
        ...lastArg,
        gasPrice,
      },
      (e, r) => {
        if (e) {
          reject(e);
        } else {
          resolve(r);
        }
      }
    )
  }).catch(e => {
    return { error: e.message }
  });

  if(gas.error) return gas;

  return {
    gasPrice,
    gas,
  };
};



class MyContract {
  constructor(contract, abi) {
    this.contract = contract;
    this.abi = abi;
    for (const key in this.contract.methods) {
      this[key] = async (...args) => {

        const a = this.abi.abi.find((a) => {
          return a.name === key;
        });
        return new Promise((resolve, reject) => {
          try {
            const web3 = utils_web3.getWeb3();
            if (a.inputs.length === args.length) {
              this.contract.methods[key](...args).call(
                (e, r) => {
                  if (e) {
                    reject(e);
                  } else {
                    resolve(r);
                  }
                }
              )
            } else {
              const lastArg = args.pop();
              const ts = this.contract.methods[key](...args);
              calcGas(web3, key, args, lastArg, ts).then((res) => {
                if(res.error){
                  return resolve(res);
                }
                var { gas, gasPrice } = res;
                ts.send(
                  {
                    ...lastArg,
                    gasPrice,
                    gas,
                  }
                ).then(res => {
                  resolve(res);
                })
              });
            }
          } catch (e) {
            reject({ error: e });
          }
        });
      };
    }
  }
}


function checkContractCode(code, address){
  if (!code || code.replace("0x", "").replace(/0/g, "") === "")
      throw new Error(
        `This network does not have this contract at address ${address}`
      );
}


async function contractAt(abi, address, onlyRead){
    if (!store.state.connected) {
      return {
        error: "wallet not connected",
      };
    }
    try {
      const web3 = utils_web3.getWeb3();
      var code = await web3.eth.getCode(address);
      checkContractCode(code, address);
      const contract = new web3.eth.Contract(abi.abi, address);
      const myContract = new MyContract(contract, abi);
      return myContract;
    } catch (err) {
      return { error: err };
    }
}




export default {
  contractAbi,
  generateOrderHash,
  createOrder,
  getOrderKey,
  tupleOrderKey,
  tupleOrder,

  contractAt,
}

