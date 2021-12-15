// var HDWalletProvider = require("truffle-hdwallet-provider");

module.exports = {
  networks: {
    // rinkeby: {
    //  provider: () => new HDWalletProvider("", `https://rinkeby.infura.io/v3/{token}`),
    //   network_id: 4,       // Ropsten's id
    //   gas: 5500000,        // Ropsten has a lower block limit than mainnet
    //  confirmations: 2,    // # of confs to wait between deployments. (default: 0)
    //  timeoutBlocks: 10000,  // # of blocks before a deployment times out  (minimum/default: 50)
    //  networkCheckTimeout: 1000000000,
    //  skipDryRun: true     // Skip dry run before migrations? (default: false for public nets )
    // },
  },

  // Set default mocha options here, use special reporters etc.
  mocha: {
    // timeout: 100000
  },

  // Configure your compilers
  compilers: {
    solc: {
      version: "0.8.0",    // Fetch exact version from solc-bin (default: truffle's version)
    }
  }
};
