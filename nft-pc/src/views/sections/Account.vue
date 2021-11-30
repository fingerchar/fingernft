<template>
  <div class="detail-wrapper">
    <div class="detail-left">
      <div class="detail-left-inner">
        <div class="flex align-center justify-between">
          <div class="u-share-follow" v-if="connected && user.coinbase == address">
            <el-button class="u-edit-btn">
              <router-link to="/profile">
                {{$t('account.editProfile')}}
              </router-link>
            </el-button>
          </div>
          <div class="u-share-follow" v-else>
            <el-button
              class="u-follow-btn custom-button"
              size="small"
              @click="addFollow()"
              v-if="!isFollow"
            >
              {{ $t("account.Follow") }}</el-button
            >
            <el-button
              class="u-follow-btn" 
              size="small"
              @click="deleteFollow()"
              v-else
              >{{ $t("account.Followed") }}
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
            :imageUrl="$filters.fullImageUrl(userinfo.avatar)"
            :address="userinfo.address"
            :imgWidth="88"
            :imgHeight="88"
            shape="circular"
          ></avatar>
        </div>
        <div class="name-text margin-bottom-xs" v-if="userinfo.nickname">{{userinfo.nickname}}</div>
        <div class="coinbase-text margin-bottom-xs flex align-center">
          <span class="mr-5">{{ $filters.ellipsisAddress(address) }}</span>
          <i
            class="el-icon-copy-document cpointer"
            v-clipboard:copy="address"
            v-clipboard:success="onSuccessCopy"
            v-clipboard:error="onErrorCopy"
          ></i>
        </div>
        <div
          class="u-info-introduction brief-text margin-bottom-xs"
          v-if="!userinfo.description"
        >
          {{ $t("account.introduction") }}
        </div>
        <div class="u-info-introduction brief-text margin-bottom-xs" v-else>
          {{ userinfo.description }}
        </div>
        <div class="u-info-follow pt-0">
          <div class="follow-item" @click="showFollowing = true">
            <span>{{ $t("account.Following") }}</span>
            <span class="u-info-follow-num">({{ stat.followingCount }})</span>
          </div>
          <div class="follow-item" @click="showFollow = true">
            <span>{{ $t("account.Followers") }} </span>
            <span class="u-info-follow-num">({{ stat.followCount }})</span>
          </div>
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
              :label="tab.title"
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
                    <div class="tab-title">{{ $t("account.noFound") }}</div>
                    <div class="tab-intro">{{ $t("account.noFoundText") }}</div>
                    <el-button class="tab-btn" @click="goHome"
                      >{{ $t("account.browseMarketplace") }}
                    </el-button>
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
                      :key="i"
                      :address="address"
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
          @close="closeFollowPopup"
          key="following"
          v-if="address"
          :address="address"
        ></follow-popup>
        <follow-popup
          :show="showFollow"
          ftype="follow"
          @close="closeFollowPopup"
          key="follow"
          v-if="address"
          :address="address"
        ></follow-popup>

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
import FollowPopup from "@/components/FollowPopup";

import NftDialog from "@/mixins/NftDialog";
import Share from "@/components/Share";
import NftTabItem from "@/mixins/NftTabItem";

export default {
  name: "Items",
  mixins: [NftDialog, NftTabItem],
  components: {
    FollowPopup,
    Share,
  },
  data() {
    return {
      imgUrl: "",
      address: "",
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
      userinfo: {},
      nftList: {
        sale: [],
        collectibles: [],
        created: [],
        liked: [],
      },
      stat: {},
      showFollowing: false,
      showFollow: false,
      loadStatus: "",
    };
  },
  created() {
    this.init();
  },
  computed: {
    connected() {
      return this.$store.state.connected;
    },
    user() {
      return this.$store.state.user;
    },
  },
  watch: {
    tab(val, old) {
      if (val == old) return;
      this.page = 1;
      this.$router.push({
        path: "/account/" + this.address,
        query: { tab: this.tab },
      });
      this.loadStatus = "";
      this.getList();
    },
    $route(newRoute) {
      let address = this.$route.params.address;
      if (!address) return;
      this.init();
    },
  },
  methods: {
    init() {
      this.page = 1;
      this.nftList = { sale: [], collectibles: [], created: [], liked: []};
      this.stat = {};
      this.isFollow = false;
      this.address = this.$route.params.address;
      if (this.$route.query.tab) {
        this.tab = this.$route.query.tab;
      } else {
        this.tab = "collectibles";
      }
      this.getUserinfo();
      if (this.connected) {
        this.matchFollow();
      }
      this.getUserStat();
      this.getList();
    },
    onSuccessCopy() {
      this.$tools.message(this.$t("request.copySuccess"), "success");
    },
    onErrorCopy() {
      this.$tools.message("Copy error");
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
    closeFollowPopup() {
      this.showFollow = false;
      this.showFollowing = false;
    },
    onShare(type) {},
    onLike(index, like) {
      this.nftList[this.tab][index].like = like;
    },
    getUserStat() {
      let params = {
        address: this.address,
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
        address: this.address,
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
            res.data.list.length < res.data.limit ? "over" : this.loadStatus;
        } else {
          that.$tools.message(res.errmsg);
        }
      });
    },
    getCollectiable: function () {
      let params = {
        page: this.page,
        limit: this.limit,
        address: this.address,
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
        address: this.address,
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
        address: this.address,
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
    tabindexFunc: function () {
      return;
    },
    getUserinfo() {
      let params = {
        address: this.address,
      };
      this.$api("user.info", params).then((res) => {
        if (this.$tools.checkResponse(res)) {
          let _data = Object.assign({}, res.data, {
            address: this.address,
          });
          this.userinfo = _data;
          this.imgUrl = _data.bannerUrl;
        } else {
          this.$tools.message(res.errmsg);
        }
      });
    },
    addFollow() {
      let data = {
        address: this.address,
      };
      var that = this;
      this.$api("follow.add", data).then((res) => {
        if (that.$tools.checkResponse(res)) {
          that.isFollow = true;
          this.getUserStat();
        }
      });
    },
    deleteFollow() {
      let data = {
        address: this.address,
      };
      var that = this;
      this.$api("follow.delete", data).then((res) => {
        if (that.$tools.checkResponse(res)) {
          that.isFollow = false;
          this.getUserStat();
        }
      });
    },
    matchFollow() {
      let address = {
        address: this.user.coinbase,
        userAddrs: this.address,
      };
      var that = this;
      this.$api("follow.match", address).then((res) => {
        if (that.$tools.checkResponse(res)) {
          if (!res.data.length) {
            that.isFollow = false;
          } else {
            that.isFollow = true;
          }
        }
      });
    },
    //更换界面
    async uploadChange(file, fileList) {
      let size = file.raw.size;
      if (size > 30 * 1024 * 1024) {
        this.errorForm.file = this.$t("form.limitFile");
      }
      var event = event || window.event;
      var result = await this.uploadStorage(file.raw);
      this.setBanner(result.url);
    },
    setBanner(url) {
      let data = {
        url: url,
      };
      this.$api("user.setbanner", data).then((res) => {
        if (this.$tools.checkResponse(res)) {
          this.imgUrl = url;
        } else {
          this.$tools.message(res.errmsg);
        }
      });
    },
    async uploadStorage(file) {
      return new Promise((resolve, reject) => {
        const formData = new FormData();
        formData.append("file", file);
        var that = this;
        this.$api("storage.upload", formData).then(async function (res) {
          if (that.$tools.checkResponse(res)) {
            resolve(res.data);
          } else {
            resolve({
              error: res.errmsg,
            });
          }
        });
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.replace-cover {
  height: 19px;
  border: 1px solid #000000;
  font-size: 7px;
  color: #333333;
  position: absolute;
  top: 75px;
  right: 14px;
  background-color: transparent !important;
}
.editProfile-btn {
  border: 1px solid #cccccc;
  border-radius: 4px;
  background-color: #f3f4fa;
}
.editProfile-btn:hover {
  background-color: #f8f8f8;
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
.flex {
  display: flex;
}
.justify-center {
  justify-content: center;
}
.align-center {
  align-items: center;
}
.cursor-pointer {
  cursor: pointer;
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
.head-portrait {
  margin-top: 40px; 
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
}

.iconfont.icon-upload {
  font-weight: bold;
}
.item-page {
  display: flex;
  flex-direction: column;
  margin: 90px auto;
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
      margin: 0 auto;
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
        background: $primaryColor;
        border-radius: 8px;
        color: #ffffff;
        font-family: "Montserrat-Regular";
        margin-top: 21px;
      }
    }
    .new-nft-list {
      margin: 0 -10px;
      .nft{
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
      }
      .loading-status {
        width: 100%;
        padding-bottom: 50px;
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
  .dialog-title-colse {
    .close-icon {
      width: 34px;
      height: 34px;
    }
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
        .dialog-u-name {
        }
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
.dialog-report {
  .report-header {
    display: flex;
    justify-content: space-between;
  }
  .dialog-title-report {
    width: 300px;
    height: 81px;
    font-size: 40px;
    font-weight: 400;
    color: #1d1e22;
    line-height: 42px;
  }
  .report-describe {
    width: 466px;
    height: 46px;
    font-size: 16px;
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

.u-info-avatar{
  display: flex;
  justify-content: center;
}
</style>


<style lang="scss">
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
.mt-50{
  margin-top: -50px !important;
}
</style>
