var ExchangeOrdersHolder = artifacts.require("ExchangeOrdersHolder");

module.exports = function(deployer) {
  deployer.then(function() {
    return deployer.deploy(ExchangeOrdersHolder).then(function(token) {
      console.log(`ExchangeOrdersHolder is deployed at ${token.address}`);
    });
  });
};
