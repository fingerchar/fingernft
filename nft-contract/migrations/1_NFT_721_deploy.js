var NFT721 = artifacts.require("NFT721");

module.exports = function(deployer) {
  deployer.then(function() {
    const singer = "0x79aca18162577437cc763e36df07bac6938b0b69";
    return deployer
      .deploy(NFT721, "FingerNFT", "FingerNFT", singer, "ipfs:/", "ipfs:/")
      .then(function(token) {
        console.log(`NFT721 is deployed at ${token.address}`);
      });
  });
};
