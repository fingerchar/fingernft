<template>
  <div class="flex-row-box">
    <side :stat="stat" :userinfo="userinfo" @refreshStat="getUserStat" />
    <div class="flex-row-content">
      <div class="item-page">
        <div class="item-tab main-wrapper">
          <el-tabs class="user-tab-menus" @tab-click="tabindexFunc" v-model="tab">
            <el-tab-pane class="tab-content-info" v-for="(tab, i) in tabs" :key="i" :label="tab.title" :name="tab.name">
              <template #label>
                <div class="tab-content-title">
                  <span>{{ tab.title }}</span>
                  <span>({{ tab.count }})</span>
                </div>
              </template>
              <div class="nft-list" v-infinite-scroll="loadList">
                <div class="tab-content-none" v-if="
                    !userNftList[tab.name].length && loadStatus != 'loading'
                  ">
                  <div class="tab-info">
                    <div class="tab-title">{{ $t("account.noFound") }}</div>
                    <div class="tab-intro">{{ $t("account.noFoundText") }}</div>
                    <el-button class="tab-btn" @click="goHome">{{ $t("account.browseMarketplace") }}
                    </el-button>
                  </div>
                </div>
                <template v-else>
                  <div class="flex-box">
                    <nft-item v-for="(nft, i) in userNftList[tab.name]" :nft="nft" :index="i" @showDialog="showDialog" @onLike="onLike" :key="i" :address="address">
                    </nft-item>
                    <nft-item-load :loadStatus="loadStatus"></nft-item-load>
                  </div>
                </template>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <sale-dialog :show="showSaleDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
        </sale-dialog>
        <cancel-sale-dialog :show="showCancelSaleDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
        </cancel-sale-dialog>
        <buy-dialog :show="showBuyDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
        </buy-dialog>

        <bid-dialog :show="showBidDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
        </bid-dialog>
        <cancel-bid-dialog :show="showCancelBidDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
        </cancel-bid-dialog>
        <accept-dialog :show="showAcceptDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
        </accept-dialog>
        <transfer-dialog :show="showTransferDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
        </transfer-dialog>
        <burn-dialog :show="showBurnDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft">
        </burn-dialog>

      </div>
    </div>
  </div>
</template>
<script>
  import NftDialog from "@/mixins/NftDialog";
  import NftItem from "@/mixins/NftItem";
  import Side from "./components/Side";

  export default {
    name: "Items",
    mixins: [NftDialog, NftItem],
    components: {
      Side,
    },
    data () {
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
        userNftList: {
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
    created () {
      this.init();
    },
    computed: {
      connected () {
        return this.$store.state.connected;
      },
      user () {
        return this.$store.state.user;
      },
    },
    watch: {
      tab (val, old) {
        if (val == old) return;
        this.page = 1;
        this.$router.push({
          path: "/account/" + this.address,
          query: { tab: this.tab },
        });
        this.loadStatus = "";
        this.getList();
      },
      $route (newRoute, oldRoute) {
        if (newRoute.path == oldRoute.path) return;
        let address = this.$route.params.address;
        if (!address) return;
        this.init();
      },
    },
    methods: {
      init () {
        this.page = 1;
        this.userNftList = { sale: [], collectibles: [], created: [], liked: [] };
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
      onSuccessCopy () {
        this.$tools.message(this.$t("request.copySuccess"), "success");
      },
      onErrorCopy () {
        this.$tools.message("Copy error");
      },
      goProfile () {
        if (!this.$tools.needLogin()) return;
        this.$router.push("/profile");
      },
      goHome () {
        this.$router.push("/");
      },
      loadList () {
        if (this.loadStatus == "over") return;
        this.getList();
      },
      reloadList () {
        this.page = 1;
        this.getList();
      },
      closeFollowPopup () {
        this.showFollow = false;
        this.showFollowing = false;
      },
      onShare (type) { },
      getUserStat () {
        let params = {
          address: this.address,
        };
        this.$api("user.stat", params).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.stat = Object.assign({},
              {
                followCount: !res.data.followCount ? 0 : res.data.followCount,
                followerCount: !res.data.followerCount ? 0 : res.data.followerCount,
              }
            );
            this.tabs[0].count = !res.data.saleCount ? 0 : res.data.saleCount;
            this.tabs[1].count = !res.data.collectionCount ? 0 : res.data.collectionCount;
            this.tabs[2].count = !res.data.createCount ? 0 : res.data.createCount;
            this.tabs[3].count = !res.data.likeCount ? 0 : res.data.likeCount;
            this.tabs = [].concat(this.tabs);
          }
        });
        this.matchFollow();
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
            if (params.page == 1) this.userNftList.sale = [];
            that.userNftList.sale = that.userNftList.sale.concat(res.data.list);
            that._queryFunction(res.data.list, that.userNftList.sale);
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
            if (params.page == 1) this.userNftList.collectibles = [];
            that.userNftList.collectibles = that.userNftList.collectibles.concat(
              res.data.list
            );
            that._queryFunction(res.data.list, that.userNftList.collectibles);
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
            if (params.page == 1) this.userNftList.created = [];
            that.userNftList.created = that.userNftList.created.concat(res.data.list);
            that._queryFunction(res.data.list, that.userNftList.created);
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
            if (params.page == 1) this.userNftList.liked = [];
            that.userNftList.liked = that.userNftList.liked.concat(res.data.list);
            that._queryFunction(res.data.list, that.userNftList.liked);
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
      getList () {
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
      onLike(data, like){
        this._onLike(data, like, this.userNftList[this.tab]);
      },
      tabindexFunc: function () {
        return;
      },
      getUserinfo () {
        let params = {
          address: this.address,
        };
        this.$api("user.info", params).then((res) => {
          if (this.$tools.checkResponse(res)) {
            let _data = Object.assign({}, res.data, {
              address: this.address,
            });
            this.userinfo = _data;
            this.userinfo.coinbase = _data.address;
            this.imgUrl = _data.bannerUrl;
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      addFollow () {
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
      deleteFollow () {
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
      matchFollow () {
        let address = {
          address: this.user.coinbase,
          userAddrs: this.address,
        };
        var that = this;
        this.$api("follow.match", address).then((res) => {
          if (that.$tools.checkResponse(res)) {
            if (!res.data.length) {
              that.userinfo.isFollow = false;
              that.isFollow = false;
            } else {
              that.userinfo.isFollow = true;
              that.isFollow = true;
            }
          }
        });
      },
    },
  };
</script>
<style lang="scss" scoped>
  .item-page {
    display: flex;
    flex-direction: column;
    margin: 3% auto;
    width: 100%;
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
          font-weight: bold;
          color: #1d1e22;
          margin: 45px auto 23px;
        }
        .tab-intro {
          width: 313px;
          font-size: 16px;
          font-weight: 400;
          color: #1d1e22;
          line-height: 22px;
          opacity: 0.5;
        }
        .tab-btn {
          width: 250px;
          height: 54px;
          margin-top: 21px;
        }
      }
      .nft-list {
        margin: 0 -10px;
        padding: 8px 0;
        .nft {
          display: flex;
          flex-wrap: wrap;
          justify-content: flex-start;
          width: 100%;
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
    }
  }

  .tab-content-title {
    min-width: $tabTitleWidth;
    text-align: center;
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
  .el-tabs__active-bar {
    height: 0px !important;
  }
</style>
