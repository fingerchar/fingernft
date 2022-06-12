var TransferProxy = artifacts.require("TransferProxy");

module.exports = function(deployer) {
  return;
  deployer.then(function() {
    return deployer.deploy(TransferProxy).then(function(token) {
      console.log(`TransferProxy is deployed at ${token.address}`);
    });
  });
};
