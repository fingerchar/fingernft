var ExchangeState = artifacts.require("ExchangeState");

module.exports = function(deployer) {
  return;
  deployer.then(function() {
    return deployer.deploy(ExchangeState).then(function(token) {
      console.log(`ExchangeState is deployed at ${token.address}`);
    });
  });
};
