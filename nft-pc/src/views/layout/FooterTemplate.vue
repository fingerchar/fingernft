<template>
  <div class="wrapper">
    <div class="smallWrapper">
      <div class="mainSection">
        <div class="firstSection">
          <router-link to="/" class="head-FirstSvg">
            <img fit="contain"
              class="logo-image"
              :src="require('@/assets/img/logo.jpg')"
            />
          </router-link>
        </div>
        <div class="secondSection">
          <router-link to="/"
            :class="$route.path == '/' ? 'router-text active' : 'router-text'"
          >
            {{ $t("footer.explore") }}
          </router-link>
          <router-link
            v-if="connected && user.coinbase"
            :class="$route.name == 'Items' ? 'router-text active' : 'router-text'"
            to="/items"
          >
            {{ $t("footer.myItems") }}</router-link
          >
          <router-link v-else class="router-text" to="/connect">
            {{ $t("footer.myItems") }}
          </router-link>
          <div
            v-if="connected && user.coinbase"
            class="router-text"
            @click="showFollowing = true"
          >
            {{ $t("navigation.following") }}
          </div>
          <router-link v-else class="router-text" to="/connect">
            {{ $t("navigation.following") }}
          </router-link>

        </div>
      </div>
      <div class="lastSection">
        <div class="header-nav-wrapper">
          <el-input
            class="header-search"
            v-model="searchKey"
            @keyup.enter="searchClick"
            :placeholder="$t('navigation.searchTip')"
          >
            <template #prefix>
              <span class="iconfont icon-search"></span>
            </template>
          </el-input>
        </div>
        <ul class="copyright">
          <li class="item">
          @ 2021 fingerchar
          </li>
          <li class="item">
          闽ICP备2020021234号-1
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>
import { ref, computed } from "vue";
export default {
  setup() {
    const PopoverSelectActive = ref("English");
    return {
      PopoverSelectActive,
    };
  },
  name: "FooterTemplate",
  data: function () {
    return {
      languagePopoverState: false,
      searchKey: this.$route.query.keyword,
    };
  },
  computed: {
    share(){
      return this.$store.state.share;
    },
    connected() {
      return this.$store.state.connected;
    },
    user() {
      return this.$store.state.user;
    }
  },
  created() {
    if (localStorage.getItem("locale") == "") {
    } else {
      if (localStorage.getItem("locale") == "zh") {
        this.PopoverSelectActive = "中文";
      } else {
        this.PopoverSelectActive = "English";
      }
    }
  },
  methods: {
    send() {
      var site_name = this.$store.state.name;
      location =
        "mailto:?cc=" +
        this.share.email.url +
        "&subject=" +
        site_name +
        "&body=email content";
    },
    open(name) {
      switch (name) {
        case "twitter":
          window.open(this.share.twitter.url);
          break;
        case "telegram":
          window.open(this.share.twitter.url);
          break;
        case "youtube":
          window.open(this.share.youtube.url);
          break;
      }
    },
    popoverSelect(parameter) {
      if (parameter == "English") {
        this.PopoverSelectActive = "English";
        localStorage.setItem("locale", "en");
        this.$i18n.locale = localStorage.getItem("locale");
      } else if (parameter == "中文") {
        this.PopoverSelectActive = "中文";
        localStorage.setItem("locale", "zh");
        this.$i18n.locale = localStorage.getItem("locale");
      }
      this.languagePopoverState = false;
    },
    async searchClick() {
      this.$router.push({ name: "Search", query: { keyword: this.searchKey } });
    },
  },
};
</script>

<style lang="scss" scoped>
.wrapper {
  width: 100%;
  background: url("../../assets/img/home/footer-bg.jpg") no-repeat;
  background-size: 100% 100%;
  border: 0px;
  border-style: solid;
  display: flex;
  justify-content: center;
  z-index: 0;
  color: #000;
}
.smallWrapper {
  width: 100%;
  padding: 0 10%;
  border-top: 1px solid transparent;
}
.mainSection {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
  align-items: center;
}
.firstSection {
  flex: 1;
  display: flex;
  margin-top: 20px;
  margin-bottom: 10px;
}
.logo-image{
  width: 200px;
}


.secondSection {
  display: flex;
  flex: 1;
  justify-content: flex-end;
  align-items: flex-start;
}
.router-text{
  cursor: pointer;
  display: flex;
  font-size: 14px;
  font-weight: 400;
  margin-right: 20px;
  &.active{
    color: #fff;
  }
}
.secondSection .router-text:last-child {
  margin-right: 0;
}

.lastSection {
  box-sizing: border-box;
  border: 0px;
  font: inherit;
  padding: 22px 0px;
  border-width: 0px;
  display: flex;
  z-index: 0;
  align-items: center;
  flex-direction: row;
  border-top: 1px solid rgba(4, 4, 5, 0.1);
  flex-wrap: wrap;
}
.copyright{
  display: flex;
  flex: 1;
  margin-right: 32px;
  .item{
    margin-right: 10px;
  }
}

.header-search{
  margin-left: 0;
  margin-right: 55px;
}
</style>
