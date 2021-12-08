import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import notice from './modules/notice'
import getters from './getters'
import VuexPersist from "vuex-persist";
Vue.use(Vuex)
const vuexLocalStorage = new VuexPersist({
  key: "vuex",
  storage: window.localStorage,
  // reducer: state => ({app:state.app})
  reducer: state => ({ })
});

const store = new Vuex.Store({
  plugins: [vuexLocalStorage.plugin],
  modules: {
    app,
    permission,
    tagsView,
    user,
    notice,
  },
  getters
})

export default store
