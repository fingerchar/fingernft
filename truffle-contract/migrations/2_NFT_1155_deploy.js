var NFT1155 = artifacts.require("NFT1155");

module.exports = function(deployer) {
  deployer.then(function() {
    const admin = "0x5d48efd4076314E489813D5dD6f4a39F22bAF64A";
    return deployer
      .deploy(NFT1155, "FingerNFT", "FingerNFT", admin, "ipfs:/", "ipfs:/")
      .then(function(token) {
        console.log(`NFT1155 is deployed at ${token.address}`);
      });
  });
};
