<template>
  <div class="app-wrapper">
    <div class="web-loading" v-if="!webLoading" v-loading.fullscreen.lock="!webLoading">
    </div>
    <div v-else>
      <router-view v-if="isRouterAlive" :current-view="currentView" />
    </div>
  </div>
</template>

<script>
  import { mapState, mapActions } from "vuex";

  export default {
    name: "App",
    beforeCreate: async function () {
      await this.$store.dispatch("config");
      await this.$store.dispatch("categorys");
      await this.$store.dispatch("payTokens");
      this.$store.dispatch("countNotices");
      this.$store.commit("WEB_LOADING");
    },
    data () {
      return {
        isRouterAlive: true,
      };
    },
    computed: {
      user () {
        return this.$store.state.user;
      },
      ...mapState({
        currentView: (state) => state.currentView,
      }),
      webLoading () {
        return this.$store.state.webLoading;
      },
    },
    watch: {
      $route (newRoute) {
        this["changeCurrentRouteTo"](newRoute);
        this["setCurrentView"](newRoute);
      },
      user (val1, val2) {
        if (val1.coinbase != val2.coinbase && this.user.coinbase) {
          this.reload();
        }
      }
    },
    mounted () {
      this["changeCurrentRouteTo"](this.$route);
      this["setCurrentView"](this.$route);
      setTimeout(() => {
        this.initWeb3()
      }, 300);
    },
    destroyed () {
      clearInterval(this.$store.state.heartbeatTimer)
    },
    methods: {
      reload () {
        this.isRouterAlive = false;
        this.$nextTick(function () {
          this.isRouterAlive = true;
        });
      },
      async initWeb3 () {
        var connected = this.$web3.checkWeb3();
        if (connected) {
          let result = await this.$store.dispatch("connect", true);
          if (result) {
            // 连接成功，则重新加载用户信息
            this.$store.dispatch("reload");
          }
        }
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

