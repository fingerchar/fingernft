import Cookies from "js-cookie";
import { removeToken } from "@/utils/auth";
import setting from "@/setting.js";
import api from "@/api/index.js";

const app = {
  state: {
    sidebar: {
      opened: !+Cookies.get("sidebarStatus"),
      withoutAnimation: false,
    },
    device: "desktop",
    language: Cookies.get("language") || "en",
    size: Cookies.get("size") || "small",
    bashApi: Cookies.get("bashApi") || process.env.VUE_APP_BASE_API,
    setting: setting,
    config: {},
  },
  mutations: {
    TOGGLE_SIDEBAR: (state) => {
      if (state.sidebar.opened) {
        Cookies.set("sidebarStatus", 1);
      } else {
        Cookies.set("sidebarStatus", 0);
      }
      state.sidebar.opened = !state.sidebar.opened;
      state.sidebar.withoutAnimation = false;
    },
    CLOSE_SIDEBAR: (state, withoutAnimation) => {
      Cookies.set("sidebarStatus", 1);
      state.sidebar.opened = false;
      state.sidebar.withoutAnimation = withoutAnimation;
    },
    TOGGLE_DEVICE: (state, device) => {
      state.device = device;
    },
    SET_LANGUAGE: (state, language) => {
      state.language = language;
      Cookies.set("language", language);
    },
    SET_SIZE: (state, size) => {
      state.size = size;
      Cookies.set("size", size);
    },
    // 改变基地址
    CHANGE_BASH_API: (state, params) => {
      state.bashApi = params;
      Cookies.set("bashApi", params);
      removeToken();
    },
    UPDATE_CONFIG(state, payload) {
      for (const key in payload) {
        state.config[key] = payload[key];
      }
    },
    UPDATE_CONFIG_ITEM(state, { key, value }) {
      state.config[key] = value;
    },
  },
  actions: {
    toggleSideBar({ commit }) {
      commit("TOGGLE_SIDEBAR");
    },
    closeSideBar({ commit }, { withoutAnimation }) {
      commit("CLOSE_SIDEBAR", withoutAnimation);
    },
    toggleDevice({ commit }, device) {
      commit("TOGGLE_DEVICE", device);
    },
    setLanguage({ commit }, language) {
      commit("SET_LANGUAGE", language);
    },
    setSize({ commit }, size) {
      commit("SET_SIZE", size);
    },
    findConfig({ commit }) {
      api("config.fetch").then((res) => {
        if (res && !res.errno) {
          const config = res.data;
          if (config.configNetwork) {
            config.configNetwork = JSON.parse(config.configNetwork);
          }
          if (config.configDeploy) {
            config.configDeploy = JSON.parse(config.configDeploy);
          }
          if (config.configContract) {
            config.configContract = JSON.parse(config.configContract);
          }
          if (config.configStake) {
            config.configStake = JSON.parse(config.configStake);
          }
          if (config.gasTracker) {
            config.gasTracker = JSON.parse(config.gasTracker);
          }
          commit("UPDATE_CONFIG", config);
        }
      });
    },
    findUnauthConfig({ commit }) {
      api("config.unauthFetch").then((res) => {
        if (res && !res.errno) {
          const config = res.data;
          if (config.configNetwork) {
            config.configNetwork = JSON.parse(config.configNetwork);
          }
          if (config.configDeploy) {
            config.configDeploy = JSON.parse(config.configDeploy);
          }
          if (config.configContract) {
            config.configContract = JSON.parse(config.configContract);
          }
          if (config.configStake) {
            config.configStake = JSON.parse(config.configStake);
          }
          if (config.gasTracker) {
            config.gasTracker = JSON.parse(config.gasTracker);
          }
          commit("UPDATE_CONFIG", config);
        }
      });
    },
  },
};

export default app;
