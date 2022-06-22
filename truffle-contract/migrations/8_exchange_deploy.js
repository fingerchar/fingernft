var NftExchange = artifacts.require("NftExchange");
var TransferProxy = artifacts.require("TransferProxy");
var TransferProxyForDeprecated = artifacts.require("TransferProxyForDeprecated");
var ERC20TransferProxy = artifacts.require("ERC20TransferProxy");
var ExchangeState = artifacts.require("ExchangeState");
var ExchangeOrdersHolder = artifacts.require("ExchangeOrdersHolder");

module.exports = function(deployer) {
  deployer.then(async () => {
    const beneficiary = "0xa60554d4eb7ec0a28154f4b8e0058238572f4aed";
    const buyerFeeSigner = "0xa60554d4eb7ec0a28154f4b8e0058238572f4aed";

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
