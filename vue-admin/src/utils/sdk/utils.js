import util_web3 from "@/utils/web3/index";
import store from "@/store";

const calcGas = async (web3, key, args, lastArg, ts) => {
  var block = await web3.eth.getBlock("latest");
  var lastBlock = block.number;
  var gasTracker = store.state.app.config.gasTracker;
  let gasPrice = await web3.eth.getGasPrice();
  if (
    gasTracker &&
    parseFloat(gasTracker.lastBlock) > parseFloat(lastBlock - 50)
  ) {
    if(gasPrice < gasTracker.medium){
      gasPrice = gasTracker.medium;
    }
  }

  const gas = await new Promise((resolve, reject) => {
    ts.estimateGas(
      {
        ...lastArg,
        gasPrice,
      },
      (e, r) => {
        if (e) {
          reject(e);
        } else {
          resolve(r);
        }
      }
    );
  });
  return {
    gasPrice,
    gas,
  };
};

class MyContract {
  constructor(contract, abi, account) {
    this.contract = contract;
    this.abi = abi;
    this.account = account;
    for (const key in this.contract.methods) {
      this[key] = async (...args) => {
        const a = this.abi.abi.find((a) => {
          return a.name === key;
        });
        return new Promise((resolve, reject) => {
          try {
            const web3 = util_web3.getWeb3();
            if (a.inputs.length === args.length) {
              this.contract.methods[key](...args).call(
                {
                  from: this.account,
                },
                (e, r) => {
                  if (e) {
                    reject(e);
                  } else {
                    resolve(r);
                  }
                }
              );
            } else {
              const lastArg = args.pop();
              const ts = this.contract.methods[key](...args);
              calcGas(web3, key, args, lastArg, ts).then(({ gas, gasPrice }) => {
                ts.send(
                  {
                    ...lastArg,
                    gasPrice,
                    gas,
                  },
                  (e, r) => {
                    if (e) {
                      reject(e);
                    } else {
                      resolve(r);
                    }
                  }
                );
              });
            }
          } catch (e) {
            reject(e);
          }
        });
      };
    }
  }
}

export default {
  contractAt(abiName, address) {
    if (!store.state.network.connected) {
      return {
        error: "wallet not connected",
      };
    }
    try {
      const account = util_web3.getAccount();
      const abi = require(`./abi/${abiName}.json`);
      const web3 = util_web3.getWeb3();
      const contract = new web3.eth.Contract(abi.abi, address);
      const myContract = new MyContract(contract, abi, account);
      return myContract;
    } catch (err) {
      return err;
    }
  },
  async getContract(abiName) {
    return this.contractAt(abiName);
  },
  getAccount() {
    return util_web3.getAccount();
  },
  async deploy(abiName, args) {
    try {
      const account = util_web3.getAccount();
      const abi = require(`./abi/${abiName}.json`);
      const web3 = util_web3.getWeb3();
      const contract = new web3.eth.Contract(abi.abi);
      var lastBlock = await web3.eth.getBlockNumber();
      var gasTracker = store.state.app.config.gasTracker;
      let gasPrice = 0;
      if (
        gasTracker &&
        parseFloat(gasTracker.lastBlock) > parseFloat(lastBlock - 50)
      ) {
        gasPrice = gasTracker.medium;
      } else {
        gasPrice = await web3.eth.getGasPrice();
      }
      const ts = contract.deploy({
        data: abi.bytecode,
        arguments: args,
      });
      const gas = await new Promise((resolve) => {
        ts.estimateGas({ from: account, gasPrice }, (error, result) => {
          if (error) {
            resolve(error);
          } else {
            resolve(result);
          }
        });
      });
      if(gas.code && gas.code == -32000){
        return { error: gas.message}
      }
      return await new Promise((resolve) => {
        ts.send({ from: account, gasPrice, gas })
          .on("error", (error) => {
            resolve({ error: error.message });
          })
          .then((newContractInstance) => {
            resolve(newContractInstance.options);
          });
      });
    } catch (err) {
      return err;
    }
  },
};
