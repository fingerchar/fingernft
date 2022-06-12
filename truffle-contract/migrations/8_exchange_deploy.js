var NftExchange = artifacts.require("NftExchange");
var TransferProxy = artifacts.require("TransferProxy");
var TransferProxyForDeprecated = artifacts.require("TransferProxyForDeprecated");
var ERC20TransferProxy = artifacts.require("ERC20TransferProxy");
var ExchangeState = artifacts.require("ExchangeState");
var ExchangeOrdersHolder = artifacts.require("ExchangeOrdersHolder");

module.exports = function(deployer) {
  deployer.then(async () => {
    const beneficiary = "0x5d48efd4076314E489813D5dD6f4a39F22bAF64A";
    const buyerFeeSigner = "0x5d48efd4076314E489813D5dD6f4a39F22bAF64A";

    const tranferProxy = await TransferProxy.deployed();
    const transferProxyForDeprecated = await TransferProxyForDeprecated.deployed();
    const erc20TransferProxy = await ERC20TransferProxy.deployed();
    const exchangeState= await ExchangeState.deployed();
    const exchangeOrderHolder = await ExchangeOrdersHolder.deployed();



    return deployer
      .deploy(
        NftExchange,
        tranferProxy.address,
        transferProxyForDeprecated.address,
        erc20TransferProxy.address,
        exchangeState.address,
        exchangeOrderHolder.address,
        beneficiary,
        buyerFeeSigner
      )
      .then((exchange) => {
        console.log(`NftExchange is deployed at ${exchange.address}`);
      });
  });
};
