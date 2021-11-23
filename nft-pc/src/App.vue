<template>
  <div class="app-wrapper">
    <div class="web-loading" v-if="!webLoading" v-loading.fullscreen.lock="!webLoading">
    </div>
    <template v-else>
      <router-view v-if="isRouterAlive" :current-view="currentView" />
    </template>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  name: "App",
  beforeCreate: async function () {
    await this.$store.dispatch("config");
    let result = await this.$store.dispatch("connect", true);
    await this.$store.dispatch("categorys");
    this.$store.dispatch("handlerLog");
    await this.$store.dispatch("payTokens");
    if (result) {
      /* connect fail => logout */
      await this.$store.dispatch("reload");
    }
    this.$store.commit("WEB_LOADING");
  },
  data() {
    return {
      isRouterAlive: true,
    };
  },
  computed: {
    user() {
      return this.$store.state.user;
    },
    ...mapState({
      currentView: (state) => state.currentView,
    }),
    webLoading() {
      return this.$store.state.webLoading;
    },
  },
  watch: {
    $route(newRoute) {
      this["changeCurrentRouteTo"](newRoute);
      this["setCurrentView"](newRoute);
    },
    user(val1, val2){
      if(val1.coinbase != val2.coinbase && this.user.coinbase){
        this.reload();
      }
    },
  },
  created: function () {
    this["changeCurrentRouteTo"](this.$route);
    this["setCurrentView"](this.$route);
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
    ...mapActions(["changeCurrentRouteTo", "setCurrentView"]),
  },
};
</script>


<style lang="scss">
html,
body {
  height: 100%;
}
#app {
  height: 100%;
  font-family: Montserrat-Regular;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
.router-view {
  width: 100%;
  height: auto;
  position: absolute;
  top: 0;
  bottom: 0;
  margin: 0 auto;
  -webkit-overflow-scrolling: touch;
}
.app-wrapper {
  height: 100%;
}
</style>

