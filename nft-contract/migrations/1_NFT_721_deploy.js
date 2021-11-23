var NFT721 = artifacts.require("NFT721");

module.exports = function(deployer) {
  deployer.then(function() {
    const admin = "";
    return deployer
      .deploy(NFT721, "FingerNFT", "FingerNFT", admin, "ipfs:/", "ipfs:/")
      .then(function(token) {
        console.log(`NFT721 is deployed at ${token.address}`);
      });
  });
};
