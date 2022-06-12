<template>
  <div class="footer-wrapper">
    <div class="inner global-lr">
      <div class="main-section">
        <div class="flex1">
          <router-link to="/">
            <img fit="contain"
              class="logo-image"
              :src="require('@/assets/img/logo.jpg')"
            />
          </router-link>
        </div>
        <div class="navs">
          <router-link to="/"
            class="nav-item"
            :class="$route.path == '/' ? 'active' : ''"
          >
            {{ $t("footer.explore") }}
          </router-link>
          <router-link
            v-if="connected && user.coinbase"
            class="nav-item"
            :class="$route.name == 'Items' ? 'active' : ''"
            to="/items"
          >
            {{ $t("footer.myItems") }}</router-link
          >
          <router-link v-else class="nav-item"
            to="/connect">
            {{ $t("footer.myItems") }}
          </router-link>
          <div class="language-router">
            <el-popover v-model:visible="languagePopover" placement="top-start" trigger="click"
              :show-arrow="false" popper-class="footPopover">
              <template #reference>
                <div class="nav-item flex-column">
                  <div class="language">{{$t('footer.language')}}</div>
                  <div class="language-text" v-if="language != '中文'">
                    English
                    <span class="iconfont icon-arrow-right"></span>
                  </div>
                  <div class="language-text" v-else>
                    中文
                    <span class="iconfont icon-arrow-right"></span>
                  </div>
                </div>
              </template>
              <div class="popover">
                <div class="popover-item" @click="languageSelect('English')">
                  <span>English</span>
                  <span v-if="language != '中文'" class="iconfont icon-seleted"></span>
                </div>
                <div class="popover-item" @click="languageSelect('中文')">
                  <span>中文</span>
                  <span v-if="language == '中文'" class="iconfont icon-seleted"></span>
                </div>
              </div>
            </el-popover>
          </div>

        </div>
      </div>
      <div class="last-section">
        <div class="flex1 footer-search">
          <el-input
            class="search-input"
            v-model="searchKey"
            @keyup.enter="searchClick"
            :placeholder="$t('navigation.searchTip')"
          >
            <template #prefix>
              <span class="iconfont icon-search"></span>
            </template>
          </el-input>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { ref, computed } from "vue";
export default {
  name: "FooterTemplate",
  data: function () {
    return {
      languagePopover: false,
      searchKey: this.$route.query.keyword
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
    },
    locale(){
      return this.$i18n.locale;
    },
    language() {
      return this.$store.state.language;
    }
  },
  created() {
    if (localStorage.getItem("locale") == "zh") {
      this.$store.state.language = "中文";
    } else {
      this.$store.state.language = "English";
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
    async searchClick() {
      this.$router.push({ name: "Search", query: { keyword: this.searchKey } });
    },
    languageSelect(parameter) {
      this.$store.state.language = parameter;
      if (parameter == "English") {
        localStorage.setItem("locale", "en");
        this.$i18n.locale = localStorage.getItem("locale");
      } else if (parameter == "中文") {
        localStorage.setItem("locale", "zh");
        this.$i18n.locale = localStorage.getItem("locale");
      }
      this.languagePopover = false;
    },
 
  },
};
</script>

<style lang="scss">
.footer-search{
  .search-input {
    border-radius: 15px;
    overflow: hidden;
    background: #fff;
    height: 30px;
    color: black;
    display: flex;
    align-items: center;
    .el-input__inner {
      padding-left: 30px !important;
      height: 100%;
      border: none;
      background: #eeeeee6e;
    }
    .el-input__prefix {
      line-height: 30px;
    }
    .iconfont {
      font-size: 22px;
      font-weight: bold;
      color: #000;
    }
  }
}
</style>

<style lang="scss" scoped>
.footer-wrapper {
  width: 100%;
  background: url("../../assets/img/home/index_bg.png");
  background-size: 100% 100%;
  background-size: cover;
  color: #000;
  padding-bottom: 50px;
  .inner{
    display: flex;
    flex-direction: column;
    flex: 1;
  }
}

.main-section{
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 0;
}

.header-section {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
  align-items: center;
}
.logo-image{
  width: 200px;
}


.navs{
  display: flex;
  flex: 1;
  justify-content: flex-end;
  align-items: flex-start;
  .nav-item{
    cursor: pointer;
    display: flex;
    font-size: 14px;
    font-weight: 400;
    margin-left: 20px;
    &.active{
      color: #fff;
    }
  }
}

.last-section {
  padding: 22px 0px;
  display: flex;
  flex-direction: row;
  border-top: 1px solid rgba(4, 4, 5, 0.1);
  align-items: center;
}
.copyright{
  display: flex;
  flex: 1;
  justify-content: flex-end;
  .item{
    margin-left: 10px;
  }
}

.language-router{
  display: flex;
  flex-direction: column;
  font-size: 14px;
  cursor: pointer;
  .language{
    opacity: 0.7;
  }
  .language-text{
    cursor: pointer;
    display: flex;
    align-items: center;
  }
}

.popover {
  .popover-item{
    display: flex;
    cursor: pointer;
    font-size: 14px;
    font-weight: 400;
    line-height: 30px;
    justify-content: space-between;
    align-items: center;
  }
  .icon-seleted{
    font-size: 24px;
    color: $primaryColor;
    font-weight: bold;
  }
}


.footer-search{
  display: flex;
  flex: 1;
  align-items: center;
  color: #333;;
  font-size: 15px;
  font-weight: 900;
}


</style>
