<template>
  <div class="collection-detail">
    <div class="bg-box" :style=" imgUrl ? 'background: url('+$filters.fullImageUrl(imgUrl)+') no-repeat; background-size: cover;':'' ">
      <div class="head-portrait">
        <avatar class="u-info-avatar" :imageUrl="$filters.fullImageUrl(contract.cover)" :address="contract.address" :imgWidth="88" :imgHeight="88" shape="circular"></avatar>
      </div>
    </div>
    <div class="collection-info">
      <div class="u-info-name" v-if="contract.name">{{ contract.name }}</div>
      <div class="u-info-address justify-center">
        <span>{{ showAddress }}</span>
        <span class="u-info-copy iconfont icon-copy" v-clipboard:copy="contract.address" v-clipboard:success="onSuccessCopy" v-clipboard:error="onErrorCopy"></span>
      </div>
      <div class="u-info-introduction">
        {{ contract.description || $t('collection.introduction') }}
      </div>
    </div>
    <div class="item-page main-wrapper">
      <div class="item-tab">
        <el-tabs class="user-tab-menus" v-model="tab">
          <el-tab-pane class="tab-content-info" v-for="(tab,i) in tabs" :key="i" :name="tab.name">
            <template #label>
              <div class="tab-content-title">
                <span>{{tab.title}}</span>
                <span>({{tab.count}})</span>
              </div>
            </template>

            <div class="new-nft-list" v-infinite-scroll="loadList">
              <div class="tab-content-none" v-if="!nftList[tab.name].length && loadStatus != 'loading'">
                <div class="tab-info">
                  <div class="tab-title">{{$t('account.noFound')}}</div>
                  <div class="tab-intro">{{$t('account.noFoundText')}}</div>
                  <el-button class="tab-btn" @click="goHome">{{$t('account.browseMarketplace')}}
                  </el-button>
                </div>
              </div>
              <template v-else>
                <div class="nft">
                  <nft-item v-for="(nft, i) in nftList[tab.name]" :nft="nft" :index="i" @showDialog="showDialog" @onLike="onLike" :key="i">
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
</template>
<script>
  import NftDialog from "@/mixins/NftDialog";
  import NftItem from "@/mixins/NftItem";
  export default {
    name: "Items",
    mixins: [NftDialog, NftItem],
    data () {
      return {
        imgUrl: "",
        address: "",
        tab: "collectibles",
        tabs: [
          { name: "sale", title: "On sale", count: 0 },
          { name: "collectibles", title: "Collitibles", count: 0 },
        ],
        page: 1,
        limit: this.$store.state.pageLimit,
        nftList: {
          sale: [],
          collectibles: [],
        },
        loadStatus: "",
        contract: {},
      };
    },
    created () {
      this.init();
    },
    computed: {
      user () {
        return this.$store.state.user;
      },
      showAddress () {
        let address = this.contract.address;
        if (!address) return "";
        var showAddress =
          address.slice(0, 11) + "..." + address.slice(address.length - 4);
        return showAddress;
      },
    },
    watch: {
      tab (val, old) {
        if (val == old) return;
        this.page = 1;
        this.$router.push({
          path: "/collection/" + this.address,
          query: { tab: this.tab },
        });
        this.loadStatus = "";
        this.getList();
      },
    },
    methods: {
      init () {
        this.address = this.$route.params.address;
        if (this.$route.query.tab) {
          this.tab = this.$route.query.tab;
        }

        this.getContract();
        if (this.$route.query.tab) {
          this.tab = this.$route.query.tab;
        }
        this.getList();
        this.statContract();
      },
      onSuccessCopy () {
        this.$tools.message(this.$t("request.copySuccess"), "success");
      },
      onErrorCopy () {
        this.$tools.message("Copy error");
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
      statContract () {
        let params = {
          address: this.address,
        };
        this.$api("contract.stat", params).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.tabs[0].count = !res.data.saleCount ? 0 : res.data.saleCount;
            this.tabs[1].count = !res.data.collectionCount ? 0 : res.data.collectionCount;
            this.tabs = [].concat(this.tabs);
          }
        });
      },
      getContract () {
        let address = {
          caddress: this.address, //待获取合约信息
        };
        this.$api("contract.info", address).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.contract = res.data;
            this.imgUrl = res.data.bannerUrl;
            if (!this.contract.name || !this.contract.symbol) {
              this.getinfo();
            }
          }
        });
      },
      getinfo () {
        let address = {
          address: this.address, //待获取合约信息
        };
        this.$api("contract.getinfo", address).then((res) => {
          if (this.$tools.checkResponse && res.data.length) {
            this.contract.name = res.data.name;
            this.contract.symbol = res.data.symbol;
          }
        });
      },
      getOnSale: function () {
        this.getContractItems("sale");
      },
      getCollectiable () {
        this.getContractItems("collectibles");
      },
      getContractItems (tab) {
        let isSell = tab == "sale" ? true : null;
        let params = {
          page: this.page,
          limit: this.limit,
          address: this.address,
          isSell: isSell,
        };
        if (this.loadStatus == "loading") return;
        this.loadStatus = "loading";
        var that = this;
        this.$api("contract.listitems", params).then((res) => {
          this.loadStatus = "loaded";
          if (that.$tools.checkResponse(res)) {
            if (params.page == 1) this.nftList[tab] = [];
            that.nftList[tab] = that.nftList[tab].concat(res.data.list);
            that._queryFunction(res.data.list, this.nftList[tab]);
            that.page = params.page + 1;
            that.loadStatus =
              res.data.list.length < res.data.limit ? "over" : this.loadStatus;
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
        }
      },
      onLike(data, like){
        this._onLike(data, like, this.nftList[this.tab]);
      },
    },
  };
</script>
<style lang="scss" scoped>
  .main-wrapper {
    max-width: 100% !important;
  }
  .collection-detail {
    padding: 0;
    .bg-box {
      position: relative;
      width: calc(var(--coverWidth));
      .head-portrait {
        width: 100%;
        height: 200px;
        background: linear-gradient(-90deg, #e7d5cb, #e3c7c4, #82aabe);
      }
      .u-info-avatar {
        position: absolute;
        left: 0;
        right: 0;
        bottom: -44px;
        display: flex;
        justify-content: center;
      }
    }
  }

  .mt-50 {
    margin-top: -50px !important;
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
  .collection-info {
    margin-top: 50px;
    text-align: center;
  }
  .coinbase-text {
    font-size: 9px;
    color: #333333;
  }
  .name-text {
    font-size: 15px;
    font-weight: bold;
    color: #000000;
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

  .collection-info {
    .u-info-name {
      padding-top: 10px;
      font-size: 15px;
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
    width: 1320px !important;
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
          font-family: 'Montserrat-Regular';
          font-weight: bold;
          color: #1d1e22;
          margin: 45px auto 23px;
        }
        .tab-intro {
          width: 313px;
          /* height: 38px; */
          font-size: 16px;
          font-family: 'Montserrat-Regular';
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
          font-family: 'Montserrat-Regular';
          margin-top: 21px;
        }
      }
      .new-nft-list {
        margin: 0 -10px;
        .nft {
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
    font-family: 'Montserrat-Regular';
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
  .dialog-report {
    .report-header {
      display: flex;
      justify-content: space-between;
    }
    .dialog-title-report {
      width: 300px;
      height: 81px;
      font-size: 40px;
      font-family: 'Montserrat-Regular';
      font-weight: 400;
      color: #1d1e22;
      line-height: 42px;
    }
    .report-describe {
      width: 466px;
      height: 46px;
      font-size: 16px;
      font-family: 'Montserrat-Regular';
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
      font-family: 'Montserrat-Regular';
      font-weight: 400;
      color: #333333;
      line-height: 30px;
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
</style>
