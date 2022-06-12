<template>
  <div class="home-header">
    <div class="header-container header-padding" :style="style">
      <router-link to="/" class="head-logo header-margin-r">
        <img fit="contain" class="logo-image"
          :src="require('@/assets/img/logo.jpg')"
        />
      </router-link>

      <div class="header-search header-margin-r">
        <el-input
          class="search-input"
          v-model="keyword"
          @keyup.enter="searchClick"
          :placeholder="$t('navigation.searchTip')"
        >
          <template #prefix>
            <span class="iconfont icon-search"></span>
          </template>
        </el-input>
      </div>

      <div class="head-navs">

        <router-link class="nav-link header-margin-r"
          :class="$route.name == 'Items' ? 'active' : ''"
          :to="!connected ? '/connect' : '/items'"
        >
          {{ $t("navigation.myItems") }}
        </router-link>

        <router-link v-if="connected"
          class="nav-link header-margin-r" to="/message?tab=unread"
          :class="$route.name == 'Message' ? 'active' : ''"
        >
          {{$t('navigation.news')}}
          <span v-if="message.unread" class="red-tip"></span>
        </router-link>
        <el-popover v-model:visible="languagePopover" placement="bottom" trigger="click" :show-arrow="false"
          popper-class="nav-popover" :offset="-8">
          <template #reference>
            <div class="nav-link header-margin-r">
              <!-- {{$t('footer.language')}} -->{{language}}
              <span class="iconfont icon-arrow-down bfont"></span>
            </div>
          </template>
          <div class="popover-menu-item" @click="languageSelect('English')" :class="language =='English' ? 'active':''">English</div>
          <div class="popover-menu-item" @click="languageSelect('中文')" :class="language =='中文' ? 'active':''">中文</div>
        </el-popover>
      </div>
      <div class="head-menus">

        <router-link class="create-link header-margin-l" to="/erc721" >
          <el-button class="link-btn">
            {{ $t("navigation.create") }}
          </el-button>
        </router-link>

        <router-link to="/connect" class="head-connect align-center header-margin-l" v-if="!connected">
          {{ $t("navigation.connectWallet") }}
        </router-link>

        <div class="user-link header-margin-l" v-else>
          <div class="user-popover">
            <profile-popover></profile-popover>
          </div>
        </div>

      </div>
    </div>

    <follow-popup
      :show="showFollowing"
      ftype="following"
      @close="showFollowing = false"
      v-if="connected"
      :address="user.coinbase"
    >
    </follow-popup>

  </div>
</template>
<script>
import FollowPopup from "@/components/FollowPopup";

export default {
  name: "HeaderTemplate",
  components: {
    FollowPopup,
  },
  data: function () {
    return {
      style: {
        backgroundColor: "",
      },
      keyword: this.$route.query.keyword,
      showFollowing: false,
      networks: this.$store.state.networks,
      searchShow: false,
      menuShow: false,
      languagePopover: false
    };
  },
  computed: {
    notice() {
      return this.$store.state.notice;
    },
    connected() {
      return this.$store.state.connected;
    },
    user: function () {
      var user = this.$store.state.user;
      return user;
    },
    message(){
      return this.$store.state.message;
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
    window.addEventListener("scroll", this.handleScroll);
  },
  methods: {
    handleScroll(){
      let scrollTop = window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      if (scrollTop) {
        if(scrollTop < 112){
          this.style.backgroundColor = `rgba(255, 255, 255,${scrollTop / (scrollTop + 112)})`;
        }else{
          this.style.backgroundColor = "#fff";
        }
      } else if (scrollTop == 0) {
        this.style.backgroundColor = "transparent";
      }
    },
    async searchClick() {
      this.$router.push({ name: "Search", query: { keyword: this.keyword} });
    },
    goProfile(){
      if(!this.$tools.needLogin()) return;
      this.$router.push("/profile");
    },
    goItems() {
      this.$router.push({ name: "Items" });
    },
    logout() {
      this.$web3.disconnect();
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
.header-search{
  .search-input {
    border-radius: 10px;
    overflow: hidden;
    background: rgba(0,0,0,0.1);
    height: 40px;
    color: #fff;
    display: flex;
    align-items: center;
    font-size: 16px;
    .el-input__inner {
      font-size:13px;
      padding-left: 30px !important;
      height: 100%;
      border: none;
      background: rgba(255,255,255,0.5);
    }
    .el-input__prefix {
      line-height: 40px;
    }
    .iconfont {
      font-size: 22px;
      font-weight: bold;
      color: #000;
    }
  }

  .nav-popover {
    width: fit-content !important;
    height: fit-content !important;
    min-width: unset !important;
    padding-bottom: 0px !important;
    z-index: 15000 !important;
    :hover {
      color: grey;
    }
  }

}


</style>

<style lang="scss" scoped>

.home-header{
  position: fixed;
  right: 0;
  left: 0;
  top: 0;
  z-index: 100;

  .header-container {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    font-size: 15px;
    line-height: 40px;
    height: 112px;
    color: #fff;
  }
}

.head-navs{
  display: flex;
  align-items: center;
  flex: 1;
  justify-content: flex-end;
}

.head-menus{
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  .round-link{
    color: #333;
    font-size: 28px;
    cursor: pointer;
  }
  .create-link {
    font-size: 16px;
  }
}
.link-btn {
  background: #0D3079;
  border-radius: 10px;
  color: #fff;
  border: 0;
}
.header-search {
  display: flex;
  flex: 1;
  align-items: center;
  color: #333;;
  font-size: 15px;
  font-weight: 900;
  width: 430px;
}
.nav-link {
  position: relative;
  white-space: nowrap;
  color: #000;
  font-size: 15px;
  cursor: pointer;
  &.active {
    border-bottom: 2px solid #333;
    line-height: 23px;
  }
}

.search-link{
  display: none;
}
.menu-link{
  display: none;
}

.user-popover{
  display: flex;
}
.user-dialog{
  display: none;
}

.user-link {
  cursor: pointer;
  display: flex;
}

.head-logo{
  display: flex;
  align-items: center;
}

.head-connect {
  display: flex;
  padding: 0 15px;
  color: #fff;
  background: #0D3079;
  border-radius: 10px;
  font-size: 16px;
  font-weight: bold;
}

.red-tip {
  position: absolute;
  cursor: pointer;
  top: 8px;
  height: 8px;
  width: 8px;
  right: -5px;
  background: #dd3b3b;
  border-radius: 5px;
  font-size: 12px;
  font-weight: bold;
  color: #ffffff;
  padding: 1px 3px;
}

.popover-menu-item{
  display: flex;
  flex: 1;
  cursor: pointer;
  padding: 5px 0;
  font-size: 14px;
  color: #666;
  &.active {
    color: $primaryColor;
  }
}
</style>
