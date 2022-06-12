var TransferProxyForDeprecated = artifacts.require("TransferProxyForDeprecated");

module.exports = function(deployer) {
  return;
  deployer.then(function() {
    return deployer.deploy(TransferProxyForDeprecated).then(function(token) {
      console.log(`TransferProxyForDeprecated is deployed at ${token.address}`);
    });
  });
};
