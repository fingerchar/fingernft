// import { loginByUsername, logout, getUserInfo } from '@/api/login'
import api from "@/api/index";
import tools from "@/utils/tools";
import { getToken, setToken, removeToken } from "@/utils/auth";

const user = {
  state: {
    config: {},
    user: "",
    status: "",
    code: "",
    token: getToken(),
    name: "",
    adminId: undefined,
    username: "",
    avatar: "",
    introduction: "",
    roles: [],
    rolesId: undefined,
    perms: [],
    setting: {
      articlePlatform: [],
    },
  },
  mutations: {
    SET_CODE: (state, code) => {
      state.code = code;
    },
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction;
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting;
    },
    SET_STATUS: (state, status) => {
      state.status = status;
    },
    SET_NAME: (state, name) => {
      state.name = name;
    },
    SET_ADMIN_ID: (state, adminId) => {
      state.adminId = adminId;
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles;
    },
    SET_PERMS: (state, perms) => {
      state.perms = perms;
    },
    SET_ROLES_ID(state, ROLES_ID) {
      state.rolesId = ROLES_ID;
    },
    SET_CONFIG(state, config) {
      let { ipfsUrl } = config;
      ipfsUrl =
        ipfsUrl.indexOf("/", ipfsUrl.length - 1) === -1
          ? ipfsUrl + "/"
          : ipfsUrl;
      config.ipfsUrl = ipfsUrl;
      state.config = config;
    },
  },

  actions: {
    // 用户名登录
    LoginByUsername(context, userInfo) {
      const username = userInfo.username.trim();
      return new Promise((resolve) => {
        let data = {
          username: username,
          password: userInfo.password,
          code: userInfo.code,
        };
        api("auth.login", data).then((response) => {
          if (tools.checkResponse(response)) {
            const token = response.data.token;
            setToken(token);
          }
          resolve(response);
        });
      });
    },
    // 获取用户信息
    GetUserInfo({ commit }) {
      return new Promise((resolve, reject) => {
        api("auth.info").then((response) => {
          if (tools.checkResponse(response)) {
            const data = response.data;
            if (data.perms && data.perms.length > 0) {
              // 验证返回的perms是否是一个非空数组
              commit("SET_PERMS", data.perms);
            } else {
              reject("getInfo: perms must be a non-null array !");
            }
            commit("SET_ROLES", data.roles);
            commit("SET_ROLES_ID", data.roles_id[0]);
            commit("SET_NAME", data.name);
            // commit('SET_AVATAR', data.avatar)
            commit("SET_AVATAR", "");
            commit("SET_INTRODUCTION", data.introduction);
            commit("SET_ADMIN_ID", data.id);
            // dispatch("config");
          }
          resolve(response);
        });
      });
    },
    LogOut({ commit, state }) {
      return new Promise((resolve) => {
        api("auth.logout", state.token).then(() => {
          commit("SET_TOKEN", "");
          commit("SET_ROLES", []);
          commit("SET_PERMS", []);
          commit("SET_ROLES_ID", undefined);
          removeToken();
          resolve();
        });
      });
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise((resolve) => {
        commit("SET_TOKEN", "");
        removeToken();
        resolve();
      });
    },

    // 动态修改权限
    ChangeRoles({ commit, dispatch }, role) {
      return new Promise((resolve) => {
        commit("SET_TOKEN", role);
        setToken(role);

        api("auth.info", role).then((res) => {
          const data = res.data;
          commit("SET_ROLES", data.roles);
          commit("SET_PERMS", data.perms);
          commit("SET_NAME", data.name);
          commit("SET_AVATAR", data.avatar);
          commit("SET_ROLES_ID", data.roles_id[0]);
          commit("SET_INTRODUCTION", data.introduction);
          dispatch("GenerateRoutes", data); // 动态修改权限后 重绘侧边菜单
          resolve();
        });
      });
    },
  },
};

export default user;
