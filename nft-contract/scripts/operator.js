var Web3 = require("web3");
var config = require("./config.js");
var utils = require("./utils.js");
var abi = require('../build/contracts/OwnableOperatorRole.json');
const HDWalletProvider = require('@truffle/hdwallet-provider');

const provider = new HDWalletProvider({
  privateKeys: [ config.privateKey ],
  providerOrUrl: config.apiUrl,
  chainId: config.chainId,
});


const web3 = new Web3(provider);
const truffle_contract = require('@truffle/contract');
let contract = truffle_contract(abi);
contract.setProvider(web3.currentProvider);

async function contractAt(address){
  try{
    return await contract.at(address);
  }catch(e){
    return { error: e.message }
  }
}

async function addOperator(operator, address){
  var _contract = await contractAt(address);
  if(_contract.error) return _contract;
  try{
    return await _contract.addOperator(operator, { "from" : provider.addresses[0]});
  }catch(e){
    return { error: e.message }
  }
}

async function set_operator(){
  console.log("start runing...");
  var address = provider.addresses[0].toLocaleLowerCase();
  var filename = "./" + address + "_" + config.chainId +  ".json";
  var data = await utils.readJsonFile(filename);
  if(!data.transferProxy || !data.transferProxyForDeprecated ||
      !data.erc20TransferProxy || !data.exchangeState || !data.exchange){
    console.log("transferProxy || transferProxyForDeprecated || erc20TransferProxy || exchangeState || exchange contract not deploy");
    process.exit();
  }

  var result = null;

  if(!data.exchange.transferProxyTx){
    console.log("transferProxy operator...");
    result = await addOperator(data.exchange.address, data.transferProxy.address);
    if(result.error){
      console.log("transferProxy operator error", result.error);
      process.exit();
    }
    console.log("add transferProxy operator tx", result.tx);
    data.exchange.transferProxyTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchange.transferProxyForDeprecatedTx){
    console.log("transferProxyForDeprecated operator...");
    result = await addOperator(data.exchange.address, data.transferProxyForDeprecated.address);
    if(result.error){
      console.log("transferProxyForDeprecated operator error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("add transferProxyForDeprecated operator tx", result.tx);
    data.exchange.transferProxyForDeprecatedTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchange.erc20TransferProxyTx){
    console.log("erc20TransferProxy operator...");
    result = await addOperator(data.exchange.address, data.erc20TransferProxy.address);
    if(result.error){
      console.log("erc20TransferProxy operator error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("add erc20TransferProxy operator tx", result.tx);
    data.exchange.erc20TransferProxyTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchange.exchangeStateTx){
    console.log("exchangeState operator...");
    result = await addOperator(data.exchange.address, data.exchangeState.address);
    if(result.error){
      console.log("exchangeState operator error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("add exchangeState operator tx", result.tx);
    data.exchange.exchangeStateTx = result.tx;
    await utils.writeJsonFile(filename, data);
  }
  process.exit();
}

set_operator();


