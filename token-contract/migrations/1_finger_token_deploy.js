const FingerToken = artifacts.require("FingerToken");

module.exports = function (deployer) {
  deployer.deploy(FingerToken);
};
