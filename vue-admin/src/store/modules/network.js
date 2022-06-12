import util_web3 from "@/utils/web3/index.js";
import i18n from "@/i18n/i18n";
import { ElNotification } from "element-plus";

const network = {
  state: {
    web3: {
      coinbase: null,
      networkId: null,
    },
    connected: false,
  },
  mutations: {
    CONNECT(state, payload) {
      state.web3 = Object.assign({}, state.web3, {
        networkId: payload.networkId,
        coinbase: payload.coinbase,
      });
      state.connected = true;
    },
    DISCONNECT(state) {
      state.connected = false;
      state.web3 = {
        coinbase: null,
        networkId: "",
      };
    },
  },
  actions: {
    connect({ commit }) {
      util_web3
        .connectWeb3()
        .then((result) => {
          if (result.error) {
            ElNotification({
              type: "error",
              title: i18n.t("global.fail"),
              message: result.error.message || result.error,
            });
            return;
          }
          commit("CONNECT", result);
          return result;
        })
        .catch((error) => {
          ElNotification({
            type: "error",
            title: i18n.t("global.fail"),
            message: error.message || error,
          });
        });
    },
    disconnect({ state, commit }) {
      if (!state.connected) return;
      return new Promise(() => {
        commit("DISCONNECT");
      });
    },
  },
};

export default network;
