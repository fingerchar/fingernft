const user = {
  state: {
    hasNotice: false
  },

  mutations: {
    SET_NOTICE: (state, payload) => {
      state.hasNotice = payload
    }
  },

  actions: {
    // 用户名登录
    getNotice() {
    }
  }
}

export default user
