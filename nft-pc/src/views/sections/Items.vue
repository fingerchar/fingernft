<template>
  <div class="detail-wrapper">
    <div class="detail-left">
      <div class="detail-left-inner">
        <div class="flex align-center justify-between">
          <div class="u-share-follow">
            <el-button>
              <router-link to="/profile">
                {{ $t("items.editProfile") }}
              </router-link>
            </el-button>
          </div>
          <div class="u-share-transmit margin-left-xs">
            <el-popover
              placement="bottom"
              :width="230"
              trigger="click"
              class="u-share-transmit-pop"
            >
              <share></share>
              <template #reference>
                <div class="u-share-btn">
                  <span class="iconfont icon-upload"></span>
                </div>
              </template>
            </el-popover>
          </div>
        </div>
        <div class="head-portrait">
          <avatar
            class="u-info-avatar"
            :imageUrl="$filters.fullImageUrl(user.avatar)"
            :address="user.coinbase"
            :imgWidth="88"
            :imgHeight="88"
            shape="circular"
          ></avatar>
        </div>
        <div class="name-text margin-bottom-xs" v-if="user.nickname">{{user.nickname}}</div>
        <div class="coinbase-text margin-bottom-xs flex align-center">
          <span class="mr-5">{{ $filters.ellipsisAddress(user.coinbase) }}</span>
          <i class="el-icon-copy-document cpointer"
            v-clipboard:copy="user.coinbase"
            v-clipboard:success="onSuccessCopy"
            v-clipboard:error="onErrorCopy"
          ></i>
        </div>
        <div
          class="u-info-introduction brief-text margin-bottom-xs"
          v-if="!user.brief">
          {{ $t("items.introduction") }}
        </div>
        <div class="u-info-introduction brief-text margin-bottom-xs" v-else>
          {{ user.brief }}
        </div>
        <div class="u-info-follow pt-0">
          <div class="follow-item" @click="showFollowing = true">
            <span>{{ $t("items.following") }}</span>
            <span class="u-info-follow-num">({{ stat.followingCount }})</span>
          </div>
          <div class="follow-item" @click="showFollow = true">
            <span>{{ $t("items.followers") }} </span>
            <span class="u-info-follow-num">({{ stat.followCount }})</span>
          </div>
        </div>
        <div class="sign-out">
          <el-button class="w100" @click="logout">{{$t('items.signOut')}}</el-button>
        </div>
      </div>
    </div>
    <div class="right-section">
      <div class="item-page main-wrapper">
        <div class="item-tab mt-50">
          <el-tabs
            class="user-tab-menus"
            @tab-click="tabindexFunc"
            v-model="tab"
          >
            <el-tab-pane
              class="tab-content-info"
              v-for="(tab, i) in tabs"
              :key="i"
              :name="tab.name"
            >
              <template #label>
                <div class="tab-content-title">
                  <span>{{ tab.title }}</span>
                  <span>({{ tab.count }})</span>
                </div>
              </template>

              <div class="new-nft-list" v-infinite-scroll="loadGoodsList">
                <div
                  class="tab-content-none"
                  v-if="
                    !nftList[tab.name].length && loadStatus != 'loading'
                  "
                >
                  <div class="tab-info">
                    <div class="tab-title">{{ $t("items.nif") }}</div>
                    <div class="tab-intro">{{ $t("items.text1") }}</div>
                    <el-button class="tab-btn" type="primary" @click="goHome">{{
                      $t("items.browse")
                    }}</el-button>
                  </div>
                </div>
                <template v-else>
                  <div class="nft">
                    <nft-item
                      v-for="(nft, i) in nftList[tab.name]"
                      :nft="nft"
                      :index="i"
                      @showDialog="showDialog"
                      @onLike="onLike"
                      :address="user.coinbase"
                      :key="i"
                    >
                    </nft-item>
                    <nft-item-load
                      :loadStatus="loadStatus"
                    ></nft-item-load>
                  </div>
                </template>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
        <follow-popup
          :show="showFollowing"
          ftype="following"
          @close="closeMespopup"
          :address="user.coinbase"
        ></follow-popup>
        <follow-popup
          :show="showFollow"
          ftype="follow"
          @close="closeMespopup"
          :address="user.coinbase"
        >
        </follow-popup>

        <sale-dialog
          :show="showSaleDialog"
          @close="closeDialog"
          @confirm="dialogConfirm"
          :asset="dialogOrder"
          :nft="dialogNft"
        >
        </sale-dialog>
        <cancel-sale-dialog
          :show="showCancelSaleDialog"
          @close="closeDialog"
          @confirm="dialogConfirm"
          :asset="dialogOrder"
          :nft="dialogNft"
        >
        </cancel-sale-dialog>
        <buy-dialog
          :show="showBuyDialog"
          @close="closeDialog"
          @confirm="dialogConfirm"
          :asset="dialogOrder"
          :nft="dialogNft"
        >
        </buy-dialog>

        <bid-dialog
          :show="showBidDialog"
          @close="closeDialog"
          @confirm="dialogConfirm"
          :bid="dialogOrder"
          :nft="dialogNft"
        >
        </bid-dialog>
        <cancel-bid-dialog
          :show="showCancelBidDialog"
          @close="closeDialog"
          @confirm="dialogConfirm"
          :bid="dialogOrder"
          :nft="dialogNft"
        >
        </cancel-bid-dialog>
        <accept-dialog
          :show="showAcceptDialog"
          @close="closeDialog"
          @confirm="dialogConfirm"
          :bid="dialogOrder"
          :nft="dialogNft"
        >
        </accept-dialog>
        <transfer-dialog
          :show="showTransferDialog"
          @close="closeDialog"
          @confirm="dialogConfirm"
          :asset="dialogOrder"
          :nft="dialogNft"
        >
        </transfer-dialog>
        <burn-dialog
          :show="showBurnDialog"
          @close="closeDialog"
          @confirm="dialogConfirm"
          :asset="dialogOrder"
          :nft="dialogNft"
        >
        </burn-dialog>
      </div>
    </div>
  </div>
</template>
<script>
import { toClipboard } from "@soerenmartius/vue3-clipboard";
import TradesInfo from "@/components/TradesInfo";
import FollowPopup from "@/components/FollowPopup";
import Share from "@/components/Share";

import NftDialog from "@/mixins/NftDialog";
import NftTabItem from "@/mixins/NftTabItem";

export default {
  name: "Items",
  mixins: [NftDialog, NftTabItem],
  components: {
    FollowPopup,
    Share,
    TradesInfo,
  },
  data() {
    return {
      direction: true,
      isFollow: false,
      tab: "collectibles",
      tabs: [
        { name: "sale", title: this.$t("items.sale"), count: 0 },
        { name: "collectibles", title: this.$t("items.collectibles"), count: 0 },
        { name: "created", title: this.$t("items.created"), count: 0 },
        { name: "liked", title: this.$t("items.liked"), count: 0 },
      ],
      page: 1,
      limit: this.$store.state.pageLimit,
      nftList: {
        sale: [],
        collectibles: [],
        created: [],
        liked: [],
      },
      showFollow: false,
      showFollowing: false,
      stat: {},
      loadStatus: "",
    };
  },
  created() {
    this.init();
  },
  computed: {
    user() {
      return this.$store.state.user;
    },
    connected() {
      return this.$store.state.connected;
    },
  },
  watch: {
    tab(val, old) {
      if (val == old) return;
      this.page = 1;
      this.$router.push({
        path: "/items",
        query: { tab: this.tab },
      });
      this.loadStatus = "";
      this.getList();
    },
  },
  methods: {
    init() {
      if (this.$route.query.tab) {
        this.tab = this.$route.query.tab;
      }
      this.showFollownum();
      this.getList();
    },
    onSuccessCopy() {
      this.$tools.message(this.$t("request.copySuccess"), "success");
    },
    onErrorCopy() {
      this.$tools.message(this.$t("request.copyError"));
    },
    goHome() {
      this.$router.push("/");
    },
    loadGoodsList() {
      if (this.loadStatus == "over") return;
      this.getList();
    },
    reloadList() {
      this.page = 1;
      this.getList();
    },
    closeMespopup() {
      this.showFollow = false;
      this.showFollowing = false;
      this.showFollownum();
    },
    onShare(type) {},
    onLike(index, like) {
      this.nftList[this.tab][index].like = like;
    },
    showFollownum() {
      let params = {
        address: this.user.coinbase,
      };
      this.$api("user.stat", params).then((res) => {
        if (this.$tools.checkResponse(res)) {
          this.stat = Object.assign( {},
            {
              followingCount: !res.data.followingCount ? 0 : res.data.followingCount,
              followCount: !res.data.followCount ? 0 : res.data.followCount,
            }
          );
          this.tabs[0].count = !res.data.saleCount ? 0 : res.data.saleCount;
          this.tabs[1].count = !res.data.collectionCount ? 0 : res.data.collectionCount;
          this.tabs[2].count = !res.data.createCount ? 0 : res.data.createCount;
          this.tabs[3].count = !res.data.likeCount ? 0 : res.data.likeCount;
          this.tabs = [].concat(this.tabs);
        }
      });
    },
    getOnSale: function () {
      let params = {
        page: this.page,
        limit: this.limit,
        address: this.user.coinbase,
      };
      if (this.loadStatus == "loading") return;
      this.loadStatus = "loading";
      var that = this;
      this.$api("user.onsales", params).then((res) => {
        this.loadStatus = "loaded";
        if (that.$tools.checkResponse(res)) {
          if (params.page == 1) this.nftList.sale = [];
          that.nftList.sale = that.nftList.sale.concat(res.data.list);
          that.queryFunction(res.data.list, "sale");
          that.page = params.page + 1;
          that.loadStatus =
            res.data.list.length < res.data.limit
              ? "over"
              : this.loadStatus;
        } else {
          that.$tools.message(res.errmsg);
        }
      });
    },
    getCollectiable: function () {
      let params = {
        page: this.page,
        limit: this.limit,
        address: this.user.coinbase,
      };
      if (this.loadStatus == "loading") return;
      this.loadStatus = "loading";
      var that = this;
      this.$api("user.collections", params).then((res) => {
        this.loadStatus = "loaded";
        if (that.$tools.checkResponse(res)) {
          if (params.page == 1) this.nftList.collectibles = [];
          that.nftList.collectibles = that.nftList.collectibles.concat(
            res.data.list
          );
          that.queryFunction(res.data.list, "collectibles");
          that.page = params.page + 1;
          that.loadStatus =
            res.data.list.length < res.data.limit
              ? "over"
              : this.loadStatus;
        } else {
          that.$tools.message(res.errmsg);
        }
      });
    },
    getCreated: function () {
      let params = {
        address: this.user.coinbase,
        page: this.page,
        limit: this.limit,
      };
      if (this.loadStatus == "loading") return;
      this.loadStatus = "loading";
      var that = this;
      this.$api("user.created", params).then((res) => {
        this.loadStatus = "loaded";
        if (that.$tools.checkResponse(res)) {
          if (params.page == 1) this.nftList.created = [];
          that.nftList.created = that.nftList.created.concat(res.data.list);
          that.queryFunction(res.data.list, "created");
          that.page = params.page + 1;
          that.loadStatus =
            res.data.list.length < res.data.limit
              ? "over"
              : this.loadStatus;
        } else {
          that.$tools.message(res.errmsg);
        }
      });
    },
    getLiked: function () {
      let params = {
        page: this.page,
        limit: this.limit,
        address: this.user.coinbase,
      };
      if (this.loadStatus == "loading") return;
      this.loadStatus = "loading";
      var that = this;
      this.$api("user.like", params).then((res) => {
        this.loadStatus = "loaded";
        if (that.$tools.checkResponse(res)) {
          if (params.page == 1) this.nftList.liked = [];
          that.nftList.liked = that.nftList.liked.concat(res.data.list);
          that.queryFunction(res.data.list, "liked");
          that.page = params.page + 1;
          that.loadStatus =
            res.data.list.length < res.data.limit
              ? "over"
              : this.loadStatus;
        } else {
          that.$tools.message(res.errmsg);
        }
      });
    },
    getList() {
      switch (this.tab) {
        case "sale":
          this.getOnSale();
          break;
        case "collectibles":
          this.getCollectiable();
          break;
        case "created":
          this.getCreated();
          break;
        case "liked":
          this.getLiked();
          break;
      }
    },
    tabindexFunc: function (tab, event) {},
    logout() {
      this.$web3.disconnect();
      this.$router.push("/")
    }
  },
};
</script>
<style lang="scss" scoped>

.head-portrait {
  margin-top: 40px; 
}
.sign-out {
    position:absolute;
    left: 0;
    right: 0;
    bottom: 0;
}
.replace-cover {
  height: 19px;
  border: 1px solid #000000;
  font-size: 7px;
  color: #333333;
  position: absolute;
  top: 75px;
  right: 14px;
  background-color: transparent;
}
.mr-5 {
  margin-right: 5px !important;
}
.margin-left-xs {
  margin-left: 10px !important;
}
.pt-0 {
  padding-top: 0 !important;
}
.margin-bottom-xs {
  margin-bottom: 10px;
}
.bg-box {
  position: absolute;
  top: 0;
  left: 0;
  display: flex;
  justify-content: center;
  width: 100%;
  height: 200px;
  background: linear-gradient(-90deg, #e7d5cb, #e3c7c4, #82aabe);
}

.user-information {
  margin-top: 190px;
  text-align: center;
}
.coinbase-text {
  color: #333333;
  font-weight: 800;
  margin-top: 20px;
  justify-content: center;
}
.name-text {
  font-size: 15px;
  font-weight: bold;
  color: #000000;
  margin-top: 15px;
  text-align: center;
}
.brief-text {
  font-size: 9px;
  color: #666666;
}

.filter-sort {
  min-width: 80px;
  font-size: 9px;
  color: #000000;
  position: absolute;
  top: 10px;
  right: 150px;
}
.sort-icon {
  width: 7px;
  height: 4px;
  margin-left: 5px;
}

.user-header {
  background: #fff;
  .user-header-inner {
    padding: 40px 40px 0;
  }
  .info {
    flex: 1;
  }
  .avatar {
    margin-right: 20px;
  }
  .name {
    font-size: 24px;
    font-weight: bold;
  }
  .brief {
    font-size: 13px;
    color: #666;
    margin-top: 5px;
    line-height: 22px;
  }
  .edit-btn {
    border: none;
    background: #f8f8f8;
    color: #333;
    font-weight: bold;
    &:hover,
    &:focus,
    &:active {
      background: $secondPrimaryColor !important;
      color: $primaryColor !important;
    }
  }

  .user-header-footer {
    padding-top: 20px;
  }
  .user-nav {
    flex: 1;
  }
  .user-nav-item {
    padding: 0 25px;
    font-weight: bold;
    text-transform: capitalize;
    cursor: pointer;
    .text {
      padding: 10px 0;
    }
    &.first {
      margin-left: -25px;
    }
    &.active {
      padding-bottom: 0;
      .text {
        padding-bottom: 5px;
        border-bottom: 5px solid $primaryColor;
      }
    }
  }
}

.u-info-left {
  .u-info-name {
    padding-top: 10px;
    font-size: 26px;
    font-weight: bold;
    color: #1d1e22;
  }
  .u-info-address {
    font-size: 15px;
    font-weight: 400;
    color: #444444;
    display: flex;
    align-items: center;
    padding: 10px 0;
  }
  .u-info-copy {
    font-size: 18px;
    padding-left: 5px;
    cursor: pointer;
  }
  .u-info-introduction {
    font-size: 12px;
    font-weight: 400;
    color: #999;
  }
}
.iconfont.icon-upload {
  font-weight: bold;
}
.item-page {
  display: flex;
  flex-direction: column;
  margin: 3% auto;
  // padding: 0px 30px;
}
.item-content {
  border-bottom: $border;
  padding-bottom: 20px;
  padding-top: 20px;
  margin-bottom: 10px;
  .u-detail {
    .u-header {
      width: 88px;
      height: 88px;
      background: rgb(167, 163, 163);
      img {
        width: 88px;
        height: 88px;
      }
    }
    .u-info-content {
      .u-info-right {
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        .u-info-share {
          display: flex;
          align-items: center;
          justify-content: flex-end;
          .u-share-twitter {
            width: 78px;
            height: 16px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: auto 10px;
          }
          .u-share-instagram {
            width: 78px;
            height: 16px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: auto 10px;
          }
        }
      }
    }
  }
}
.u-info-follow {
  margin-top: 40px;
  color: #444;
  padding-top: 10px;
  font-weight: bold;
  .follow-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    cursor: pointer;
    margin-bottom: 15px;
  }
  .u-info-follow-num {
    margin-left: 2px;
    font-weight: 400;
    color: #999;
  }
}
.item-tab {
  position: relative;
  .tab-content-info {
    min-height: 200px;
    .tab-content-none {
      width: 100%;
      display: flex;
      justify-content: center;
      text-align: center;
      margin: 43px auto;
    }
    .tab-info {
      .tab-title {
        font-size: 24px;
        font-family: "Montserrat-Regular";
        font-weight: bold;
        color: #1d1e22;
        margin: 45px auto 23px;
      }
      .tab-intro {
        width: 313px;
        /* height: 38px; */
        font-size: 16px;
        font-family: "Montserrat-Regular";
        font-weight: 400;
        color: #1d1e22;
        line-height: 22px;
        opacity: 0.5;
      }
      .tab-btn {
        width: 250px;
        height: 54px;
        font-family: "Montserrat-Regular";
        margin-top: 21px;
      }
    }
    .new-nft-list {
      // margin: 0 -10px;
      padding: 8px 0;
      .nft{
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
      }
      .loading-status {
        padding-bottom: 50px;
        width: 100%;
      }
      .nft-loading {
        height: 60px;
      }
      .nft-loading-over {
        text-align: center;
        padding: 30px 0;
        color: $grayColor;
      }
    }
    .tab-actdetail {
      border: 1px solid #e5e5e5;
      margin: 10px;
      border-radius: 20px;
    }
  }
}
.dialog-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .dialog-title-left {
    font-size: 40px;
    font-weight: 400;
    color: #1d1e22;
    line-height: 22px;
    .dialog-title-num {
      font-size: 16px;
      font-weight: 400;
      color: #444444;
      line-height: 22px;
    }
  }
  .close-icon {
    width: 26px;
    height: 26px;
  }
}

.dialog-follow {
  font-family: "Montserrat-Regular";
  .dialog-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 20px auto;
    .dialog-u-detail {
      display: flex;
      width: 160px;
      justify-content: space-between;
      .dialog-u-header {
        width: 58px;
        height: 58px;
        background: #313131;
        border-radius: 50%;
      }
      .dialog-u-info {
        display: flex;
        flex-direction: column;
        justify-content: space-around;
      }
    }
    .dialog-follow-btn {
      width: 88px;
      height: 44px;
      background: $primaryColor;
      border-radius: 8px;
      color: aliceblue;
    }
  }
}
.u-share-btn {
  color: #606266;
  border: $border;
  border-radius: $borderRadius;
  background: #fff;
  padding: 0 20px;
  height: 32px;
  line-height: 32px;
  font-size: 16px;
  font-weight: normal;
  cursor: pointer;
  &:hover{
    color: #409eff;
    background-color: #ecf5ff;
    border-color: #c6e2ff;
  }
}

.dialog-report {
  .report-header {
    display: flex;
    justify-content: space-between;
  }
  .dialog-title-report {
    width: 300px;
    height: 81px;
    font-size: 40px;
    font-family: "Montserrat-Regular";
    font-weight: 400;
    color: #1d1e22;
    line-height: 42px;
  }
  .report-describe {
    width: 466px;
    height: 46px;
    font-size: 16px;
    font-family: "Montserrat-Regular";
    font-weight: 400;
    color: #333333;
    line-height: 30px;
    opacity: 0.6;
  }
  .submit-btn {
    width: 100%;
    height: 54px;
    background: $primaryColor;
    border-radius: 8px;
    margin-top: 66px;
  }
  .dialog-report-content {
    margin-top: 60px;
    font-size: 16px;
    font-family: "Montserrat-Regular";
    font-weight: 400;
    color: #333333;
    line-height: 30px;
  }
}



.tab-content-title {
  min-width: $tabTitleWidth;
  text-align: center;
}
.skeleton-avatar {
  width: 88px;
  height: 88px;
  border-radius: 10px;
}
.skeleton-name {
  width: 150px;
  height: 19px;
  margin-top: 10px;
}
.skeleton-introduction {
  width: 100px;
  height: 19px;
  margin-top: 10px;
}
.u-info-avatar{
  display: flex;
  justify-content: center;
}
</style>

<style lang="scss">

.head-portrait .avatar-image{
  justify-content: center;
}
.user-tab-menus {
  .el-tabs__item {
    color: #999;
    height: 35px;
    line-height: 35px;
    &:hover,
    &:active,
    &:focus {
      color: #333;
    }
    &.is-active {
      color: #333;
    }
  }
  .el-tabs__active-bar {
    background-color: #333;
  }
}
// .main-wrapper {
//   width: 100% !important;
//   max-width: 100% !important;
//   padding: 60px 0 0 !important;
// }
.el-tabs__active-bar {
  height: 0px !important;
}

.mt-50 {
  margin-top: -50px !important;
  // padding: 0px 150px;
}

</style>
