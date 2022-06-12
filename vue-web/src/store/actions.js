import tools from "@/util/tools.js";
import i18n from "@/i18n/i18n";
import api from "@/api/index.js";
import sdk from "@/util/sdk/index.js";
import util_web3 from "@/util/web3/index.js";
import { getLocalStorage, removeLocalStorage } from "@/util/local-storage.js";
import BigNumber from "bignumber.js";

function getActiveNetwork(result, networks) {
  let network = null;
  for (var i = 0; i < networks.length; i++) {
    let _network = networks[i];
    if (result.networkId == _network.channelId) {
      network = _network;
      break;
    }
  }
  return network;
}

export default {
  config({ state, commit }) {
    return new Promise((resolve, reject) => {
      api("config.fetch").then((res) => {
        if (tools.checkResponse(res)) {
          commit("CONFIG", res.data);
        }
        resolve();
      });
    });
  },
  gasTracker({state, commit}){
    return new Promise((resolve, reject) => {
      api("config.gasTracker").then((res) => {
        if (tools.checkResponse(res)) {
          commit("GAS_TRACKER", res.data);
        }
        resolve();
      });
    });
  },
  reload({ state, commit, dispatch }) {
    return new Promise(function(resolve, reject) {
      var items = getLocalStorage("Authorization");
      if (items.Authorization) {
        api("user.reload")
          .then(async function(response) {
            if (tools.checkResponse(response)) {
              if (
                state.user.coinbase !=
                response.data.user.address.toLocaleLowerCase()
              ) {
                removeLocalStorage("Authorization");
                resolve(response);
              } else {
                commit("RELOAD");
                commit("USERINFO", response.data.user);
                dispatch("heartbeat");
              }
            } else {
              removeLocalStorage("Authorization");
            }
            resolve(response);
          })
          .catch((err) => {
            removeLocalStorage("Authorization");
            resolve(response);
          });
      } else {
        resolve();
      }
    });
  },
  updatePayToken({ state, commit, dispatch }, payToken) {
    return new Promise((resolve, reject) => {
      if (payToken.address == sdk.NULL_ADDRESS()) {
        dispatch("ethBalance");
      } else if (!payToken.tokenId || !parseInt(payToken.tokenId)) {
        dispatch("erc20Balance", payToken);
      }
    });
  },
  payTokens({ state, commit, dispatch }) {
    return new Promise((resolve, reject) => {
      api("paytoken.list").then((res) => {
        if (tools.checkResponse(res)) {
          commit("PAYTOKENS", res.data);
          dispatch("allBalance");
        }
        resolve(res);
      });
    });
  },
  categorys({ state, commit }) {
    return new Promise((resolve, reject) => {
      api("category.list").then((res) => {
        if (tools.checkResponse(res)) {
          commit("CATEGORYS", res.data);
        }
        resolve(res);
      });
    });
  },
  logout({ state, commit }) {
    return new Promise((resolve, reject) => {
      commit("LOGOUT");
      resolve();
    });
  },
  unreadNotice({ state, commit, getters }) {
    return new Promise(function(resolve, reject) {
      api("notice.unread").then((res) => {
        if (tools.checkResponse(res)) {
          commit("NOTICE_UNREAD", res.data);
        }
      });
    });
  },
  allBalance({ state, commit, getters, dispatch }) {
    return new Promise(async function(resolve, reject) {
      for (var i = 0; i < state.payTokens.length; i++) {
        let token = state.payTokens[i];
        if (token.address == sdk.NULL_ADDRESS()) {
          dispatch("ethBalance");
        } else {
          dispatch("erc20Balance", token);
        }
      }
    });
  },
  ethBalance({ state, commit, getters }) {
    return new Promise(async function(resolve, reject) {
      let asset = {
        address: sdk.NULL_ADDRESS(),
      };
      let owner = state.user.coinbase;
      let balance = await sdk.getBalance(asset, owner);
      if (balance.error) return resolve();

      commit("ETH_BALANCE", balance);
      resolve();
    });
  },
  erc20Balance({ state, commit, getters }, payToken) {
    return new Promise(async function(resolve, reject) {
      let asset = {
        address: payToken.address,
      };
      let owner = state.user.coinbase;
      let balance = await sdk.getBalance(asset, owner);
      if (balance.error) return resolve();
      balance = new BigNumber(balance.toString());
      let decimal = new BigNumber(10).exponentiatedBy(payToken.decimals);
      balance = balance.dividedBy(decimal).toFixed();
      let payload = {};
      payload[payToken.address.toLocaleLowerCase()] = balance;
      commit("ERC20_BALANCE", payload);
      resolve();
    });
  },
  authinfo({ state, commit, dispatch }) {
    return new Promise((resolve, reject) => {
      let data = {
        address: state.user.coinbase,
      };
      api("user.info", data).then((res) => {
        if (tools.checkResponse(res)) {
          let _data = Object.assign({}, res.data, {
            address: state.user.coinbase,
          });
          commit("USERINFO", _data);
          dispatch("heartbeat");
        }
        resolve(res);
      });
    });
  },
  heartbeat({ state, commit, dispatch }) {
    if (state.heartbeatTimer) {
      clearTimeout(state.heartbeatTimer);
      commit("HEARTBEAT", null);
    }
    if (!state.connected) return;
    return new Promise((resolve, reject) => {
      dispatch("allBalance");
      dispatch("gasTracker");
      var timer = setTimeout(() => {
        dispatch("heartbeat");
      }, 20000);
      commit("HEARTBEAT", timer);
    });
  },
  monitorWeb3({ state, commit, dispatch }) {
    return new Promise(async function(resolve, reject) {
      await util_web3.monitorWeb3();
      resolve();
    });
  },
  connect({ state, commit, dispatch }, isInit) {
    return new Promise(async function(resolve, reject) {
      var result = await util_web3.connectWallet();
      if (result.error) {
        if (!isInit) {
          tools.message(result.error);
        }
        return resolve();
      }

      commit("CONNECT", result);
      await dispatch("authinfo");
      resolve(true);
    });
  },
  signLogin({ state, commit, dispatch }, payload) {
    return new Promise(async function(resolve, reject) {
      let user = payload;
      if (!user || !user.coinbase) user = state.user;

      let signature = await util_web3.loginWallet(user.coinbase);

      if (signature.error) {
        tools.messageBox(i18n.global.t("global.errSignature"), signature.error);
        return resolve();
      }
      var data = {
        userAddress: user.coinbase,
        signature: signature.signature,
        timestamp: signature.timestamp,
      };
      api("user.login", data).then((res) => {
        if (tools.checkResponse(res)) {
          let _data = Object.assign(res.data, {
            walletType: state.web3.walletType,
          });
          commit("LOGIN", _data);
          dispatch("authinfo");
        }
        resolve(res);
      });
    });
  },
  connectAndSign({ state, commit, dispatch }, type) {
    return new Promise(async function(resolve, reject) {
      let result = await util_web3.connectWallet(type);
      if (result.error) {
        tools.message(result.error);
        return resolve();
      }

      commit("WEB3", result);
      let data = {
        coinbase: result.coinbase,
        networkId: result.networkId,
      };
      result = await dispatch("signLogin", data);
      resolve(result);
    });
  },
  countNotices({ state, commit }) {
    return new Promise(function(resolve, reject) {
      if (!state.connected) resolve();
      let data = {
        address: state.user.coinbase,
      };
      api("notice.count", data).then((res) => {
        if (tools.checkResponse(res)) {
          commit("MESSAGE", res.data);
        }
        resolve();
      });
    });
  },
  unreadNoticeByAddress({ state, commit }) {
    return new Promise(function(resolve, reject) {
      let data = {
        address: state.user.coinbase,
      };
      api("notice.countunread", data).then((res) => {
        if (tools.checkResponse(res)) {
          commit("NOTICE_UNREAD", res.data);
        }
        resolve();
      });
    });
  },
  disconnect({ state, commit }) {
    return new Promise((resolve, reject) => {
      commit("DISCONNECT");
      resolve();
    });
  },
  setCurrentView({ commit }, newRoute) {
    commit("setCurrentView", newRoute);
  },
  //改变当前路由
  changeCurrentRouteTo({ commit }, newRoute) {
    commit("changeCurrentRouteTo", newRoute);
  },
};
