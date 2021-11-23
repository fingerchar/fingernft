var ERC20TransferProxy = artifacts.require("ERC20TransferProxy");

module.exports = function(deployer) {
  deployer.then(function() {
    return deployer.deploy(ERC20TransferProxy).then(function(token) {
      console.log(`ERC20TransferProxy is deployed at ${token.address}`);
    });
  });
};
