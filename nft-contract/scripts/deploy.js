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

async function createNFT721(){
  var contract = getContract("../build/contracts/NFT721.json");
  try{
    return await contract.new(
      config.NFTName,
      config.NFTName,
      config.miner,
      "ipfs:/",
      "ipfs:/",
      { from: provider.addresses[0] } );
  }catch(e){
    return { error: e.message };
  }
}

async function createTransferProxy(){
  var contract = getContract("../build/contracts/TransferProxy.json");
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}

async function createTransferProxyDeprecated(){
  var contract = getContract("../build/contracts/TransferProxy.json");
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}

async function createERC20TransferProxy(){
  var contract = getContract("../build/contracts/ERC20TransferProxy.json");
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }
}

async function createExchangeState(){
  var contract = getContract("../build/contracts/ExchangeState.json");
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }

}

async function createOrderHolder(){
  var contract = getContract("../build/contracts/ExchangeOrdersHolder.json");
  try{
    return await contract.new( {from: provider.addresses[0] });
  }catch(e){
    return { error: e.message };
  }

}

async function createExchange(data){
  var contract = getContract("../build/contracts/NftExchange.json");
  try{
    return await contract.new(
      data.transferProxy.address,
      data.transferProxyForDeprecated.address,
      data.erc20TransferProxy.address,
      data.exchangeState.address,
      data.exchangeOrderHolder.address,
      config.beneficiary,
      config.buyerFeeSigner,
      {from: provider.addresses[0] });
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
  if(!data.NFT721){
    console.log("create NFT721...");
    result = await createNFT721();
    if(result.error){
      process.exit();
    }
    console.log("create NFT721 address:", result.address);
    data.NFT721 = {
      address: result.address
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.transferProxy){
    console.log("create TransferProxy...");
    result = await createTransferProxy();
    if(result.error){
      console.log("create TransferProxy error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create TransferProxy address:", result.address);
    data.transferProxy = {
      address: result.address,
    };
    await utils.writeJsonFile(filename, data);
  }

  if(!data.transferProxyForDeprecated){
    console.log("create TransferProxyDeprecated...");
    result = await createTransferProxyDeprecated();
    if(result.error){
      console.log("create TransferProxyDeprecated error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create TransferProxyDeprecated address:", result.address);
    data.transferProxyForDeprecated = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.erc20TransferProxy){
    console.log("create ERC20TransferProxy...");
    result = await createERC20TransferProxy();
    if(result.error){
      console.log("create ERC20TransferProxy error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create ERC20TransferProxy address:", result.address);
    data.erc20TransferProxy = {
      address: result.address
    }
    await utils.writeJsonFile(filename, data);
  }
  
  if(!data.exchangeState){
    console.log("create ExchangeState...");
    result = await createExchangeState();
    if(result.error){
      console.log("create ExchangeState error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create ExchangeState address:", result.address);
    data.exchangeState = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchangeOrderHolder){
    console.log("create ExchangeOrderHolder...");
    result = await createOrderHolder();
    if(result.error){
      console.log("create ExchangeOrderHolder error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create ExchangeOrderHolder address:", result.address);
    data.exchangeOrderHolder = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }

  if(!data.exchange){
    console.log("create Exchange...");
    result = await createExchange(data);
    if(result.error){
      console.log("create Exchange error", result.error);
      await utils.writeJsonFile(filename, data);
      process.exit();
    }
    console.log("create Exchange address:", result.address);
    data.exchange = {
      address: result.address,
    }
    await utils.writeJsonFile(filename, data);
  }
  process.exit();
}


deploy();


