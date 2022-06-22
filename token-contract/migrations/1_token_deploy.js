var Token = artifacts.require("Token");

module.exports = function(deployer) {
  deployer.then(function() {
    const name = "Coin Token"
    const symbol = "COIN"
    const supply = "100000";
    return deployer
      .deploy(Token, name, symbol, supply)
      .then(function(token) {
        console.log(`Token is deployed at ${token.address}`);
      });
  });
};
