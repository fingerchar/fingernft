import Web3 from "web3";
import store from "@/store";
import i18n from "@/i18n/i18n";
import tools from "@/util/tools.js"
import { removeLocalStorage } from "@/util/local-storage.js"

const promisify = (inner) =>
    new Promise((resolve, reject) =>
        inner((err, res) => {
            if (err) {
                reject(err);
            } else {
                resolve(res);
            }
        })
    );


export default {
  async connectWeb3() {
    var error = "";
    if (window.ethereum) {
      try {
        var t = await window.ethereum.request({ method: 'eth_requestAccounts' });
        if (!t) {
          error = "MetaMask enable Error";
          return { error };
        }
        var web3 = new Web3(window.ethereum);
        window.wallet = web3;
        var networkId = await promisify(cb => web3.eth.getChainId(cb));
        var coinbase = await promisify(cb => web3.eth.getCoinbase(cb));

        window.ethereum.once("accountsChanged", this.accountsChanged);
        window.ethereum.on("chainChanged", this.chainChanged);
        window.ethereum.on("disconnect", this.disconnect);
        let walletType = 'metamask';
        return { networkId, coinbase, walletType}
      } catch(e) {
        store.commit("PUSH_LOG", {
          name: "connectWeb3",
          projectName: "testOne",
          level: 3,
          content: JSON.stringify({
            message: e.message,
            stack: e.stack,
          }),
        });
        error = e.message;
      }
    } else {
      error = "MetaMask not Install";
    }
    return { error };
  },
  accountsChanged(accounts) {
    if(!store.state.connected) return;
    store.dispatch("logout")
    if(accounts.length){
      store.dispatch('connect');
    }
  },
  chainChanged(channelId) {
    let config = store.state.config;
    if (parseInt(channelId) != parseInt(config.networkId) ) {
      tools.messageBox(i18n.global.t('global.errNetwork'),
        i18n.global.t('global.changeNetworkTo') +
        tools.networkName(config.networkId));
    }
  },
  disconnect(error) {
    if(!store.state.connected) return;
    store.dispatch("logout");
  },
  async getTransaction(tx) {
    let web3 = this.getWeb3();
    try {
      // return await web3.eth.getTransaction(tx)
      return await promisify(cb => web3.eth.getTransaction(tx, cb));
    } catch (e) {
      return { error: e.message }
    }
  },
  async getTransactionReceipt(tx) {
    let web3 = this.getWeb3();
    try {
      return await promisify(cb => web3.eth.getTransactionReceipt(tx, cb));
    } catch (e) {
      return { error: e.message }
    }
  },
  async decodeLog(inputs, hexString, options){
    let web3 = this.getWeb3();
    try {
      return await promisify(cb => web3.eth.abi.decodeLog(inputs, hexString, options, cb));
    } catch (e) {
      return { error: e.message }
    }
  },
  getWeb3() {
    return window.wallet;
  },
  async loginWallet(address) {
    let timestamp = parseInt(new Date().getTime()/1000);
    var message = store.state.message + " " + timestamp;
    try {
      let signature = await this.sign(message, address);
      return {
        signature: signature,
        timestamp: timestamp,
      }
    } catch (e) {
      return { error: e.message }
    }
  },
  async sign(message, address) {
    var web3 = window.wallet;
    try {
      address = web3.utils.toChecksumAddress(address);
      var signature = await promisify( cb => web3.eth.personal.sign(message, address, cb));
      // var signature = await web3.eth.personal.sign(message, address);
      return signature;
    } catch (e) {
      return { error: e.message }
    }
  },
  async checkWeb3(web3) {
    if (window.ethereum) {
      try {
        // var isListening = await web3.eth.net.isListening();
        // var isListening = await promisify(cb => web3.eth.net.isListening(cb))
        // if (!isListening) return false;
        return true;
      } catch {
      }
    }
    return false;
  },
  async monitorWeb3() {
    let web3 = window.wallet;
    if (typeof web3 == "undefined" || !web3) return;
    var result = await this.checkWeb3(web3);
    if (!result) {
      var result = await this.connectWeb3();
      if (result.error) return;
      web3 = window.wallet;
    }
    let config = store.state.config;

    var networkId = await promisify(cb => web3.eth.getChainId(cb));
    if (networkId != config.networkId) {
      tools.messageBox(i18n.global.t('global.errNetwork'),
        i18n.global.t('global.changeNetworkTo') + tools.networkName(config.networkId));
    }
  },
  async changeNetwork(network){
    try{
      let result = await window.ethereum.request({
        method: "wallet_switchEthereumChain",
        params: [{ chainId: "0x" + network.channelId.toString(16) }],
      });
      return result;
    }catch(e){
      if(e.code == 4001) return { error: e.message  };
      try{
        let result = await window.ethereum.request({
          method: "wallet_addEthereumChain",
          params: [{
            chainId: "0x" + network.channelId.toString(16),
            chainName: network.name,
            nativeCurrency:{
              name: network.coinSymbol,
              symbol: network.coinSymbol,
              decimals: 18,
            },
            rpcUrls: [ network.rpc ],
          }]
        });
        return result;
      }catch(e){
        return { error: e.message }
      }
    }
  },
}
