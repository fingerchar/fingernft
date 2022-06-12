<template>
  <section class="app-main">
    <router-view #="{ Component }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="cachedViews">
          <component :is="Component" />
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script>
  import { isMobile } from "@/utils";
  export default {
    name: "AppMain",
    computed: {
      cachedViews() {
        return this.$store.state.tagsView.cachedViews;
      },
      key() {
        return this.$route.fullPath;
      },
      name() {
        if (this.$route.path === "/crm/list" && isMobile()) {
          return "dashboardSale";
        }
        return "default";
      },
    },
  };
</script>

<style lang="scss" scoped>
  .app-main {
    /* 50= navbar  50  */
    min-height: calc(100vh - 85px);
    width: 100%;
    position: relative;
    overflow: hidden;
    background-color: #fff;
  }
  .fixed-header + .app-main {
    padding-top: 50px;
  }
  .hasTagsView {
    .app-main {
      /* 84 = navbar + tags-view = 50 + 34 */
      min-height: calc(100vh - 84px);
    }
    .fixed-header + .app-main {
      padding-top: 84px;
    }
  }
</style>

<style lang="scss">
  // fix css style bug in open el-dialog
  .el-popup-parent--hidden {
    .fixed-header {
      padding-right: 15px;
    }
  }
</style>
