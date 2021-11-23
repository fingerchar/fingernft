<template>
  <div class="head-container">
    <div class="home-header" id="home-header" :style="style">
      <router-link to="/" class="head-FirstSvg">
        <img fit="contain" class="logo-image"
          :src="require('@/assets/img/logo.jpg')"
        />
      </router-link>

      <div class="header-nav-wrapper">
        <el-input
          class="header-search"
          v-model="keyword"
          @keyup.enter="searchClick"
          :placeholder="$t('navigation.searchTip')"
        >
          <template #prefix>
            <span class="iconfont icon-search"></span>
          </template>
        </el-input>
      </div>

      <div class="head-wrapper">
        <router-link class="nav-link"
          :class="$route.name == 'Root' ? 'active' : ''"
          to="/"
        >
          {{ $t("navigation.explore") }}
        </router-link>
        <router-link class="nav-link"
          v-if="connected && user.coinbase"
          :class="$route.name == 'Items' ? 'active' : ''"
          to="/items"
        >
          {{ $t("navigation.myItems") }}
        </router-link>
        <router-link v-else class="nav-link" to="/connect">
          {{ $t("navigation.myItems") }}
        </router-link>
        <div class="nav-link"
          v-if="connected && user.coinbase"
          @click="showFollowing = true"
        >
          {{ $t("navigation.following") }}
        </div>
        <router-link v-else class="nav-link" to="/connect">
          {{ $t("navigation.following") }}
        </router-link>
        <router-link
          class="create-link"
          to="/Create"
        >
          <el-button class="custom-button" round size="mini">
            {{ $t("navigation.create") }}
          </el-button>
        </router-link>
        <router-link to="/connect" class="flex align-center" v-if="!connected">
          <div class="head-connect">
            {{ $t("navigation.connectWallet") }}
          </div>
        </router-link>

        <div class="user-link" v-else>
          <el-popover
            placement="bottom"
            trigger="click"
            :show-arrow="false"
            popper-class="myself-popover"
            :offset="0"
          >
            <template #reference>
              <div class="user-link-inner">
                <span class="avatar">
                  <avatar
                    class="avatar-img"
                    :imageUrl="$filters.fullImageUrl(user.avatar)"
                    :address="user.coinbase"
                    :imgWidth="30"
                    :imgHeight="30"
                    shape="circular"
                  >
                  </avatar>
                  <span class="iconfont icon-arrow-down"></span>
                </span>
              </div>
            </template>
            <div class="myself-wrapper">
              <div class="myself-wrapper-inner">
                <el-skeleton :loading="loading" animated>
                  <template #template>
                    <div class="myself-header">
                      <div class="f3left">
                        <el-skeleton-item class="skeleton-name" />
                        <el-skeleton-item class="skeleton-setName" />
                      </div>
                      <div class="f1right">
                        <el-skeleton-item class="skeleton-introduction" />
                      </div>
                    </div>
                  </template>
                  <template #default>
                    <div class="myself-header">
                      <div class="f3left">
                        <div @click="goItems" class="cpointer coinbase">
                          {{
                            user.nickname ||
                            $filters.ellipsisAddress(user.coinbase)
                          }}
                        </div>
                        <div class="setDisplayName">
                          <router-link to="/profile">{{
                            $t("navigation.sdn")
                          }}</router-link>
                        </div>
                      </div>
                      <div class="f1right">
                        <avatar
                          @click="goItems"
                          class="cpointer avatar-img"
                          :imageUrl="$filters.fullImageUrl(user.avatar)"
                          :address="user.coinbase"
                          :imgWidth="40"
                          :imgHeight="40"
                        >
                        </avatar>
                      </div>
                    </div>
                  </template>
                </el-skeleton>

                <div class="myself-unit">
                  <img class="paytoken-img"
                    src="@/assets/img/FINGER.jpg"
                  />
                  <div class="myself-unit-text">
                    <div class="myself-unit-title">
                      {{ $t("navigation.balance") }}
                    </div>
                    <div class="myself-unit-value">
                      <el-skeleton :loading="loading" animated>
                        <template #template>
                          <el-skeleton-item class="skeleton-balance1" /><br />
                          <el-skeleton-item class="skeleton-balance2" />
                        </template>
                        <template #default>
                          <div class="myself-unit-value-left">
                            {{ erc20Balance.balance }}
                            {{ defaultPaytoken.symbol }}
                          </div>
                          <div class="myself-unit-value-right">
                            ${{ erc20Balance.usdtBalance }}
                          </div>
                        </template>
                      </el-skeleton>
                    </div>
                  </div>
                </div>
              </div>
              <div class="myself-Line"></div>
              <router-link
                v-if="connected && user.coinbase"
                class="myself-bottom"
                to="/items"
              >
                {{ $t("navigation.myItems") }}</router-link
              >
              <router-link v-else class="myself-bottom" to="/connect">
                {{ $t("navigation.myItems") }}</router-link
              >

              <div class="myself-bottom">
                <router-link to="/Profile">
                  {{ $t("navigation.editProfile") }}
                </router-link>
              </div>
              <div class="myself-bottom" @click="logout">
                {{ $t("navigation.disconnect") }}
              </div>
            </div>
          </el-popover>
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
import NotifyDialog from "@/components/NotifyDialog";

export default {
  name: "HeaderTemplate",
  components: {
    NotifyDialog,
    FollowPopup,
  },
  data: function () {
    return {
      style: {
        backgroundColor: "",
      },
      keyword: this.$route.query.keyword,
      showFollowing: false,
      loading: true,
      networks: this.$store.state.networks,
    };
  },
  computed: {
    notice() {
      return this.$store.state.notice;
    },
    connected() {
      return this.$store.state.connected;
    },
    defaultPaytoken() {
      return this.$store.getters.defaultSalePayToken();
    },
    erc20Balance() {
      let paytoken = this.$store.getters.payToken(this.defaultPaytoken.address);
      if (!paytoken) return { balance: 0, usdtBalance: 0 };
      let balance = this.$store.getters.getBalance(
        this.defaultPaytoken.address
      );
      balance = !balance ? 0 : balance;
      let usdtBalance = balance * paytoken.rate;
      return {
        balance: this.$tools.decimal(balance, 3),
        usdtBalance: this.$tools.decimal(usdtBalance, 2),
      };
    },
    user: function () {
      var user = this.$store.state.user;
      return user;
    },
    notice_unread() {
      return this.$store.state.notice_unread;
    },
  },
  created() {
    window.addEventListener("scroll", this.handleScroll);
    let that = this;
    setTimeout(() => {
      that.loading = false;
    }, 2000);
  },
  methods: {
    handleScroll(){
      let scrollTop = window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      if (scrollTop) {
        if(scrollTop < 60){
          this.style.backgroundColor = `rgba(255, 255, 255,${scrollTop / (scrollTop + 60)})`;
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
    goItems() {
      this.$router.push({ name: "Items" });
    },
    logout() {
      this.$web3.disconnect();
    },
  },
};
</script>
<style lang="scss" scoped>
.logo-image {
  height: 26px;
  line-height: 0px;
}
.home-header {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  padding: 5px 30px;
  position: fixed;
  right: 0;
  left: 0;
  top: 0;
  font-size: 15px;
  line-height: 40px;
  color: #fff;
  z-index: 2000;
}

.notice-link {
  position: relative;
  cursor: pointer;
  width: 26px;
  height: 26px;
  border: 1px solid #e5e5e5;
  border-radius: 5px;
  margin-left: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 10px;

  .iconfont {
    color: #333;
    font-size: 22px;
  }
}

.header-nav-wrapper {
  margin-left: 0px;
  display: flex;
  flex: 1;
  align-items: center;
  color: rgba(4, 4, 5, 0.5);
  font-size: 15px;
  font-weight: 900;
  min-width: 288px;
  padding-top: 10px;
  padding-bottom: 10px;
}
.nav-link {
  position: relative;
  white-space: nowrap;
  margin-right: 25px;
  color: #000;
  font-size: 10px;
  cursor: pointer;
  &.active {
    color: $primaryColor;
  }
}

.user-link {
  cursor: pointer;
  .El-popover__wrapper {
    height: 26px;
    line-height: 26px;
  }
  .user-link-inner {
    display: flex;
    align-items: center;
    height: 40px;
  }
  .b {
    color: #333;
    padding: 0 10px;
    font-size: 12px;
  }
  .avatar {
    display: flex;
    align-items: center;
    padding: 0 5px;
    border-radius: 5px;
    height: 26px;
    .avatar-img {
      display: flex;
    }
    .iconfont {
      font-weight: bold;
      color: #333;
      margin-left: 5px;
    }
  }
}
.myself-wrapper {
  padding: 30px 20px 0;
  display: flex;
  flex-direction: column;
}

.myself-wrapper-inner {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
.head-FirstSvg {
  margin-right: 10px;
  display: flex;
  align-items: center;
}

.headLine {
  width: 1px;
  height: 24px;
  margin: 0 18px;
  margin-right: 0;
  background: rgba(4, 4, 5, 0.1);
}

.head-wrapper {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
.head-connect {
  padding: 0 15px;
  color: #fff;
  background: $gradientColor;
  border-radius: 20px;
  height: 28px;
  font-size: 10px;
  font-weight: bold;
  line-height: 28px;
}
.myselfSpan {
  font-size: 10px;
  font-weight: bold;
  color: #1d1e22;
  line-height: 12px;
  margin-left: 10px;
}

.coinBase {
  padding: 8px 0px 14px 0;
  padding-right: 0px;
  display: block;
  font-size: 16px;
  font-weight: bold;
  color: #1d1e22;
  line-height: 12px;
}
.copyTag {
  margin-left: 8px;
  color: rgba(4, 4, 5, 0.4);
}
.myself-Line {
  margin-top: 10px;
  width: 242px;
  border: 1px solid #eeeeee;
  margin-bottom: 40px;
}
.EditProfile {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  max-width: 100%;
  font-weight: 700;
  font-size: 14px;
  padding: 8px 12px;
}
.disconnect {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  max-width: 100%;
  font-weight: 700;
  font-size: 14px;
  padding: 8px 12px;
}


.myself-header {
  display: flex;
  width: 100%;
  padding-bottom: 30px;
  .coinbase {
    font-size: 15px;
    margin-bottom: 5px;
  }
  .setDisplayName {
    font-size: 12px;
    font-weight: 400;
    color: $primaryColor;
    cursor: pointer;
  }

  .f3left {
    display: flex;
    flex-direction: column;
    flex: 3;
  }
  .f1right {
    display: flex;
    flex: 1;
    justify-content: flex-end;
  }
}
.myself-unit {
  display: flex;
  align-items: center;
  margin-bottom: 28px;
  .paytoken-img {
    width: 35px;
    height: 35px;
    border-radius: 50%;
  }
}

.myself-unit-text {
  margin-left: 10px;
}
.myself-unit-title{
  width: 49px;
  height: 10px;
  font-size: 12px;
  font-weight: 400;
  color: #999;
  line-height: 12px;
}
.myself-unit-value{
  margin-top: 5px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-direction: column;
}
.myself-unit-value-left {
  font-weight: 400;
  color: #444;
  line-height: 12px;
  white-space: nowrap;
  margin-top: 5px;
}
.myself-unit-value-right {
  background: #eeeeee;
  border-radius: 9px;
  padding: 2px 4px;
  font-size: 12px;
  font-weight: 400;
  color: #999;
  line-height: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5px;
}

.myself-bottom {
  cursor: pointer;
  height: 11px;
  font-size: 12px;
  font-weight: 400;
  color: #444444;
  line-height: 12px;
  margin-bottom: 29px;
  white-space: nowrap;
  display: flex;
}

.redTip {
  position: absolute;
  cursor: pointer;
  top: 16px;
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

@media only screen and (max-width: 992px) {
  .home-header {
    flex-wrap: wrap;
  }
  .header-nav-wrapper {
    padding-left: 0px;
  }
  .header-search {
    margin-left: 0;
    min-width: 100px;
  }
  .head-wrapper {
    padding: 0;
  }
  .notice-link {
    margin-left: 0;
  }
}
.skeleton-name {
  width: 168px;
  height: 21px;
  margin-bottom: 5px;
}
.skeleton-setName {
  width: 102px;
  height: 15px;
}
.skeleton-introduction {
  width: 40px;
  height: 40px;
  border-radius: 10px;
}
.skeleton-balance1 {
  width: 60px;
  height: 12px;
}
.skeleton-balance2 {
  width: 20px;
  height: 12px;
}
.networkClass {
  height: 28px;
  line-height: 1;
  white-space: nowrap;
  cursor: pointer;
  border: 1px solid #dcdfe6;
  box-sizing: border-box;
  padding: 7px 10px 7px 15px;
  color: #fff;
  border-color: #aaa;
  border-radius: 20px;
  margin-right: 20px;
  .iconfont {
    color: white;
    font-weight: bold;
  }
}
.create-link {
  margin-right: 20px;
}
.stakingClass {
  .iconfont {
    font-weight: bold;
  }
}
</style>
<style lang="scss">
.network-popover {
  width: fit-content !important;
  height: fit-content !important;
  min-width: unset !important;
  padding-bottom: 0px !important;
  z-index: 15000 !important;
  :hover {
    color: grey;
  }
}
.myself-popover {
  width: 290px !important;
  inset: 0 auto auto 5px !important;
}
</style>
