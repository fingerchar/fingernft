<template>
  <div class="flex-row-box">
    <side :stat="stat" :userinfo="user" @refreshStat="getUserStat" />
    <div class="flex-row-content">
      <div class="item-page main-wrapper">
        <div class="item-tab">
          <el-tabs class="user-tab-menus" @tab-click="tabindexFunc" v-model="tab">
            <el-tab-pane class="tab-content-info" v-for="(tab, i) in tabs" :key="i" :name="tab.name">
              <template #label>
                <div class="tab-content-title">
                  <span>{{ tab.title }}</span>
                  <span>({{ tab.count }})</span>
                </div>
              </template>

              <div class="nft-list" v-infinite-scroll="loadList">
                <div class="tab-content-none" v-if="!userNftList[tab.name].length && loadStatus != 'loading'">
                  <div class="tab-info">
                    <div class="tab-title">{{ $t("items.nif") }}</div>
                    <div class="tab-intro">{{ $t("items.text1") }}</div>
                    <el-button class="tab-btn" type="primary" @click="goHome">
                      {{ $t("items.browse") }}
                    </el-button>
                  </div>
                </div>
                <template v-else>
                  <div class="flex-box">
                    <nft-item v-for="(nft, i) in userNftList[tab.name]" :nft="nft" :index="i" @showDialog="showDialog" @onLike="onLike" :address="user.coinbase" :key="i">
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
        <burn-dialog :show="showBurnDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
        </burn-dialog>

      </div>
    </div>
  </div>
</template>

<script>

  import Side from "./components/Side";

  import NftDialog from "@/mixins/NftDialog";
  import NftItem from "@/mixins/NftItem";

  export default {
    name: "Items",
    mixins: [NftDialog, NftItem],
    components: {
      Side,
    },
    data () {
      return {
        tab: "collectibles",
        tabs: [
          { name: "sale", title: this.$t("items.sale"), count: 0 },
          { name: "collectibles", title: this.$t("items.collectibles"), count: 0 },
          { name: "created", title: this.$t("items.created"), count: 0 },
          { name: "liked", title: this.$t("items.liked"), count: 0 },
        ],
        page: 1,
        limit: this.$store.state.pageLimit,
        userNftList: {
          sale: [],
          collectibles: [],
          created: [],
          liked: [],
        },
        stat: {},
        loadStatus: "",
      };
    },
    created () {
      this.init();
    },
    computed: {
      user () {
        return this.$store.state.user;
      },
    },
    watch: {
      tab (val, old) {
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
      init () {
        if (this.$route.query.tab) {
          this.tab = this.$route.query.tab;
        }
        this.getUserStat();
        this.getList();
      },
      goHome () {
        this.$router.push("/");
      },
      reloadList () {
        this.loadStatus = "";
        this.page = 1;
        this.getList();
      },
      loadList () {
        if (this.loadStatus == "over") return;
        this.getList();
      },
      getUserStat () {
        let params = { address: this.user.coinbase };
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
      },
      getOnSale: function () {
        let params = { page: this.page, limit: this.limit, address: this.user.coinbase };
        if (this.loadStatus == "loading") return;
        this.loadStatus = "loading";
        var that = this;
        this.$api("user.onsales", params).then((res) => {
          this.loadStatus = "loaded";
          if (that.$tools.checkResponse(res)) {
            if (params.page == 1) this.userNftList.sale = [];
            that.userNftList.sale = that.userNftList.sale.concat(res.data.list);
            that._queryFunction(res.data.list, that.userNftList.sale);
            // that.userQueryFunction(res.data.list, 'sale');
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
        let params = { page: this.page, limit: this.limit, address: this.user.coinbase };
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
            // that.userQueryFunction(res.data.list, 'collectibles');
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
            if (params.page == 1) this.userNftList.created = [];
            that.userNftList.created = that.userNftList.created.concat(res.data.list);
            // that.userQueryFunction(res.data.list, 'created');
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
          address: this.user.coinbase,
        };
        if (this.loadStatus == "loading") return;
        this.loadStatus = "loading";
        var that = this;
        this.$api("user.like", params).then((res) => {
          this.loadStatus = "loaded";
          if (that.$tools.checkResponse(res)) {
            if (params.page == 1) this.userNftList.liked = [];
            that.userNftList.liked = that.userNftList.liked.concat(res.data.list);
            // that.userQueryFunction(res.data.list, 'liked');
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
      tabindexFunc: function (tab, event) { },
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
  .flex-box {
    margin: 0;
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
