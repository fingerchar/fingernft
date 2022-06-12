<template>
  <div class="navbar">
    <hamburger
      :toggle-click="toggleSideBar"
      :is-active="sidebar.opened"
      class="hamburger-container"
    />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <template>
        <!--

        <el-tooltip v-if="device!=='mobile'" :content="$t('navbar.fullScreen')" effect="dark" placement="bottom">
          <screenfull class="right-menu-item" />
        </el-tooltip>

        <el-tooltip v-if="device!=='mobile'" :content="$t('navbar.layoutSize')" effect="dark" placement="bottom">
          <size-select class="right-menu-item" />
        </el-tooltip>
        -->
      </template>

      <el-link
        v-if="!connected"
        type="info"
        :underline="false"
        class="avatar-container right-menu-item"
        @click="connectWallet"
        >{{ $t("navbar.connectWallet") }}</el-link
      >
      <span class="avatar-container right-menu-item profileClass" v-else>
        {{ $filters.ellipsisAddress(web3.coinbase) }}
      </span>
      <el-dropdown
        class="avatar-container right-menu-item profileClass"
        trigger="click"
      >
        <div class="avatar-wrapper">
          {{ $t("navbar.languageUpdate") }}
          <el-icon><CaretBottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="change('en')">
              <span class="display-block">English</span>
            </el-dropdown-item>
            <el-dropdown-item divided @click="change('cn')">
              <span class="display-block">中文</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          {{ $t("navbar.user") }}
          <el-icon><CaretBottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link to="/dashboard">
              <el-dropdown-item>
                {{ $t("navbar.dashboard") }}
              </el-dropdown-item>
            </router-link>

            <el-dropdown-item divided>
              <router-link to="/profile/password">
                {{ $t("navbar.passwordUpdate") }}
              </router-link>
            </el-dropdown-item>
            <el-dropdown-item divided @click="logout">
              <span class="display-block">{{ $t("navbar.logout") }}</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import Breadcrumb from "@/components/Breadcrumb";
  import Hamburger from "@/components/Hamburger";

  export default {
    data() {
      return {
        value: "",
        options: [
          {
            value: "en",
            label: "English",
          },
          {
            value: "cn",
            label: "中文",
          },
        ],
      };
    },
    components: {
      Breadcrumb,
      Hamburger,
    },
    created() {
      this.$i18n.locale = this.lang;
    },
    computed: {
      lang() {
        return this.$store.state.app.language;
      },
      connected() {
        return this.$store.state.network.connected;
      },
      web3() {
        return this.$store.state.network.web3;
      },
      ...mapGetters(["sidebar", "name", "avatar", "device"]),
    },
    methods: {
      toggleSideBar() {
        this.$store.dispatch("toggleSideBar");
      },
      logout() {
        this.$store.dispatch("LogOut").then(() => {
          this.$store.dispatch("disconnect");
          // 登出后重新登录默认登录到后台首页。
          this.$router.replace({
            path: "/login",
            // query: { ...this.$route.query, redirect: this.$route.path },
          });
        });
      },
      change(param) {
        if (param == "en") {
          this.$store.dispatch("setLanguage", "en");
          this.$i18n.locale = this.lang;
        } else if (param == "cn") {
          this.$store.dispatch("setLanguage", "zh");
          this.$i18n.locale = this.lang;
        }
        this.$i18n.setLocale(this.lang);
      },
      connectWallet() {
        this.$store.dispatch("connect").then(() => {});
      },
    },
  };
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .navbar {
    height: 50px;
    line-height: 50px;
    border-radius: 0 !important;
    position: fixed;
    width: 100%;
    top: 0;
    background-color: #fff;
    z-index: 1002;
    box-shadow: 0 3px 6px 0 rgba(0, 0, 0, 0.16);
    .hamburger-container {
      line-height: 58px;
      height: 50px;
      float: left;
      padding: 0 10px;
    }
    .breadcrumb-container {
      float: left;
    }
    .errLog-container {
      display: inline-block;
      vertical-align: top;
    }
    .right-menu {
      float: right;
      height: 100%;
      display: flex;
      align-items: center;
      &:focus {
        outline: none;
      }
      .right-menu-item {
        display: inline-block;
        margin: 0 8px;
        vertical-align: top;
      }
      .avatar-container {
        margin-right: 30px;
        .avatar-wrapper {
          cursor: pointer;
          // margin-top: 5px;
          position: relative;
          display: flex;
          align-items: center;
          .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 10px;
          }
          .el-icon-caret-bottom {
            position: absolute;
            right: -20px;
            // top: 25px;
            font-size: 12px;
          }
        }
      }
    }
  }
  .display-block {
    display: block;
  }
  .profileClass {
    font-size: 14px;
    color: #606266;
    cursor: pointer;
  }
</style>
