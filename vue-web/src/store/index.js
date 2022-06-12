import { createStore } from "vuex";
import VuexPersist from "vuex-persist";
import state from "./state";
import actions from "./actions";
import getters from "./getters";
import mutations from "./mutations";

const vuexLocalStorage = new VuexPersist({
  key: "vuex",
  storage: window.localStorage,
  reducer: (state) => ({ user: state.user, netWork: state.netWork }),
});

const store = createStore({
  plugins: [vuexLocalStorage.plugin],
  state,
  actions,
  getters,
  mutations,
});

export default store;
