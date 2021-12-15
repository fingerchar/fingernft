const fs = require("fs");
var Web3 = require("web3");
var config = require("./config.js");
var utils = require("./utils.js");
const HDWalletProvider = require('@truffle/hdwallet-provider');

const provider = new HDWalletProvider({
  privateKeys: [ config.privateKey ],
  providerOrUrl: config.apiUrl,
  chainId: config.chainId,
});
const web3 = new Web3(provider);

const truffle_contract = require('@truffle/contract');

function getContract(abi_name){
  var abi = require(abi_name);
  var contract = truffle_contract(abi);
  contract.setProvider(web3.currentProvider);
  return contract;
}

async function createToken(){
  var contract = getContract("../build/contracts/Token.json");
  try{
    return await contract.new(
      config.name,
      config.symbol,
      config.supply,
      { from: provider.addresses[0] } );
  }catch(e){
    return { error: e.message };
  }
}


async function deploy(){
  var address = provider.addresses[0].toLocaleLowerCase();
  var filename = "./" + address + "_" + config.chainId +  ".json";
  var data = await utils.readJsonFile(filename);
  console.log("data", data);
  console.log("start deploying...");
  
  var result = null;
  if(!data.Token){
    console.log("create Token...");
    result = await createToken();
    if(result.error){
      process.exit();
    }
    console.log("create Token address:", result.address);
    data.Token = {
      address: result.address
    }
    await utils.writeJsonFile(filename, data);
  }
  process.exit();
}


deploy();


