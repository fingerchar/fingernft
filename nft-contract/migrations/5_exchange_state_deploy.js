var ExchangeState = artifacts.require("ExchangeState");

module.exports = function(deployer) {
  deployer.then(function() {
    return deployer.deploy(ExchangeState).then(function(token) {
      console.log(`ExchangeState is deployed at ${token.address}`);
    });
  });
};
