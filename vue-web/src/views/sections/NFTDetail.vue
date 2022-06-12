<template>
  <div v-if="isFound" class="nft-wrapper">
    <div class="preview-section">
      <div class="preview-section-image">
        <nft-preview :image="nftURI?.image" :animation_url="nftURI?.animation_url"></nft-preview>
      </div>
      <div class="preview-menus">
        <div class="preview-menu" @click="likeFunction()" :class="info.like ? 'active' : ''">
          <span class="iconfont" :class="[info.like ? 'icon-collection-fill' : 'icon-collection']"></span>
        </div>
        <div class="preview-menu" @click="isFull = true">
          <span class="iconfont icon-fullscreen"></span>
        </div>
        <el-popover placement="left" trigger="click" width="220px" :show-arrow="false" popper-class="share-popover">
          <template #reference>
            <div class="preview-menu">
              <span class="iconfont icon-upload"></span>
            </div>
          </template>
          <share></share>
        </el-popover>

        <el-popover placement="left" trigger="click" width="180px" :show-arrow="false" popper-class="menu-popover">
          <template #reference>
            <div class="preview-menu">
              <span class="iconfont icon-ellipsis"></span>
            </div>
          </template>
          <div class="menus">
            <template v-if="isOwner">
              <div class="menu" @click="showDialog('transfer', asset)">
                {{ $t("nftDetail.transfer") }}
              </div>
              <div class="menu" @click="showDialog('burn', asset)">
                {{ $t("nftDetail.burnToken") }}
              </div>
            </template>
            <div class="menu" @click="showDialog('opensea')" v-if="config.network.opensea">
              {{ $t("nftDetail.viewOpensea") }}
            </div>
          </div>
        </el-popover>
      </div>
    </div>
    <el-skeleton :loading="loading" animated>
      <template #template>
        <div class="info-section">
          <div class="info-section-inner">
            <div class="info-main">
              <div class="info-base">
                <el-skeleton-item class="nft-name el-skeleton-item1" /><br />
                <el-skeleton-item class="nft-name el-skeleton-item2" /><br />
                <el-skeleton-item class="nft-name el-skeleton-item3" /><br />
              </div>
              <el-skeleton-item class="nft-name el-skeleton-item4" /><br />
              <el-skeleton-item class="nft-name el-skeleton-item5" /><br />
              <el-skeleton-item class="nft-name el-skeleton-item4" /><br />
              <el-skeleton-item class="nft-name el-skeleton-item6 m-top-10" /><br />
              <el-skeleton-item class="nft-name el-skeleton-item6" /><br />
            </div>
            <div class="info-footer-wrap"></div>
          </div>
        </div>
      </template>
      <template #default>
        <div class="info-section">
          <div class="info-section-inner">
            <div class="info-main">
              <div class="info-base">
                <div class="nft-name">{{ nftURI.name || "" }}</div>
                <div class="nft-metas">
                  <template v-if="!asset.onsell">
                    <span class="nft-meta">{{ $t("nftDetail.nfs") }}</span>
                    <span class="nft-meta cblue">0 of 1</span>
                  </template>
                  <template v-else>
                    <span class="nft-meta">
                      {{ $tools.noDecimalsValue(asset.buyerValue, 1, asset.paytokenDecimals) }}
                      {{ asset.paytokenSymbol }}
                    </span>
                    <span class="nft-meta cblue">
                      1 of 1
                    </span>
                  </template>
                </div>
                <div class="category-group">
                  <div class="category-item" v-if="info.category">
                    {{ info.category.name }}
                  </div>
                </div>
              </div>
              <div class="description">
                {{ nftURI.description || $t("nftDetail.text2") }}
              </div>

              <div class="nft-royalties" v-if="royalties">
                {{ royalties }}% {{ $t("nftDetail.text3") }}
              </div>
              <div class="tab-section">
                <el-tabs v-model="tabIndex" @tab-click="clickTab">
                  <el-tab-pane :label="$t('nftDetail.info')" name="info">
                    <info-tab :info="info" :creator="creator" :attributes="nftURI.attributes">
                    </info-tab>
                  </el-tab-pane>
                  <el-tab-pane :label="$t('nftDetail.owners')" name="owners">
                    <owners-tab :owners="nft.items" @cancel="(asset) => showDialog('cancel_sale', asset)" @buy="(asset) => showDialog('buy', asset)"></owners-tab>
                  </el-tab-pane>
                  <el-tab-pane :label="$t('nftDetail.history')" name="history" v-if="historys.length > 0">
                    <history-tab :historys="historys"></history-tab>
                  </el-tab-pane>
                  <el-tab-pane :label="$t('nftDetail.bid')" name="bid" v-if="bids.length > 0">
                    <bid-tab :bids="bids" :myBid="myBid" :asset="asset" @cancel="(bid) => showDialog('cancel_bid', bid)" @accept="(bid) => showDialog('accept', bid)"></bid-tab>
                  </el-tab-pane>

                </el-tabs>
              </div>
            </div>

            <div class="info-footer-wrap">
              <div class="info-footer-header" v-if="highestBid">
                <highest-bid :bid="highestBid" :asset="asset" @accept="(bid) => showDialog('accept', bid)"></highest-bid>
              </div>
              <div class="info-footer">
                <div class="info-footer-body">
                  <template v-if="isOwner">
                    <el-button type="primary" v-if="!asset.onsell" @click="showDialog('sale', asset)">
                      {{ $t("nftDetail.sale") }}
                    </el-button>
                    <el-button type="primary" v-else @click="showDialog('edit_sale', asset)">
                      {{ $t("nftDetail.editSale") }}
                    </el-button>
                  </template>

                  <template v-else>
                    <el-button v-if="asset.onsell" @click="showDialog('buy', asset)" type="primary">
                      {{ $t("nftDetail.buy") }}
                    </el-button>

                    <el-button plain v-if="!myBid" @click="showDialog('bid')" type="primary">
                      {{ $t("nftDetail.pad") }}</el-button>

                    <el-button plain v-else type="primary" @click="showDialog('edit_bid', myBid)">
                      {{ $t("nftDetail.editBid") }}</el-button>

                  </template>
                </div>

                <div class="info-footer-footer" v-if="!isOwner && !highestBid">
                  <span class="c9">{{ $t("nftDetail.text4") }}</span>
                </div>

              </div>
            </div>
          </div>
        </div>
      </template>
    </el-skeleton>

    <sale-dialog :show="showSaleDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="order" :uri="nftURI" :nft="info">
    </sale-dialog>
    <cancel-sale-dialog :show="showCancelSaleDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="order" :uri="nftURI" :nft="info">
    </cancel-sale-dialog>
    <buy-dialog :show="showBuyDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="order" :uri="nftURI" :nft="info">
    </buy-dialog>
    <bid-dialog :show="showBidDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="order" :uri="nftURI" :nft="info">
    </bid-dialog>
    <cancel-bid-dialog :show="showCancelBidDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="order" :uri="nftURI" :nft="info">
    </cancel-bid-dialog>
    <accept-dialog :show="showAcceptDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="order" :uri="nftURI" :nft="info">
    </accept-dialog>
    <transfer-dialog :show="showTransferDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="order" :uri="nftURI" :nft="info">
    </transfer-dialog>
    <burn-dialog :show="showBurnDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="order" :uri="nftURI" :nft="info">
    </burn-dialog>
    <full-screen :isFull="isFull" :uri="nftURI" @exitClick="isFull = false"></full-screen>
  </div>
  <div class="no-found" v-else>
    <no-found></no-found>
  </div>
</template>

<script>
  import InfoTab from "@/views/sections/details/Info";
  import OwnersTab from "@/views/sections/details/Owners";
  import HistoryTab from "@/views/sections/details/History";
  import BidTab from "@/views/sections/details/Bid";
  import HighestBid from '@/views/sections/details/HighestBid';

  import FullScreen from "@/components/FullScreen";
  import Share from "@/components/Share";
  import NftPreview from "@/components/NFTPreview";
  import MixinsNFTInfo from "@/mixins/NftInfo";
  import NftItem from "@/mixins/NftItem";
  export default {
    name: "NFTDetail",
    components: {
      NftPreview,
      InfoTab,
      OwnersTab,
      HistoryTab,
      BidTab,
      HighestBid,
      FullScreen,
      Share,
    },
    mixins: [MixinsNFTInfo, NftItem],
    data: function () {
      var ids = this.$route.params.ids;
      ids = ids.split(":");
      return {
        loading: true,
        token: {
          token: ids[0],
          tokenId: ids[1],
        },
        tabIndex: "info",
        creator: {},
        info: {},
        historys: [],
        bids: [],
        showSaleDialog: false,
        showCancelSaleDialog: false,
        showBuyDialog: false,
        showBidDialog: false,
        showCancelBidDialog: false,
        showAcceptDialog: false,
        showTransferDialog: false,
        showBurnDialog: false,
        order: {},
        isFull: false,
        mediaData: [],
        isFound: true,
        royalties: '',
        nftList: []
      };
    },
    created () {
      this.getDetails();
    },
    computed: {
      nft () {
        return this.nftList[0] || {}
      },
      isOwner(){
        if(!this.nft.items) return false;
        var itemOwner = this.nft.items[0].itemOwner;
        if(!this.connected || this.user.coinbase != itemOwner) return false;
        return true;
      },
      asset(){
        if(!this.nft.items) return {}
        var item = this.nft.items[0];
        if(item.onsell && this.nft.sale){
          var sale = this.nft.sale;
          return { ...sale, ...item };
        }
        return item;
      },
      myBid () {
        if (!this.connected || !this.nft.bids) return;
        if (this.nft.onlyOwner == this.user.coinbase) return;
        return this.getActiveAddressBid(this.nft.bids, this.user.coinbase);
      },
      highestBid () {
        if (!this.nft.bids || !this.nft.items) return;
        var itemOwner = this.nft.items[0].itemOwner;
        return this.getHighestBid(this.nft.bids, itemOwner);
      },
      config () {
        return this.$store.state.config;
      },
      user () {
        return this.$store.state.user;
      },
      connected () {
        return this.$store.state.connected;
      },
      nftURI () {
        if (!this.info) return;
        if (this.info.metadataContent) {
          return this.$tools.analysis(this.info.metadataContent);
        }
        if (this.mediaData) {
          let nft;
          nft = this.info.address + ":" + this.info.tokenId;
          for (let key in this.mediaData) {
            if (key == nft) {
              return this.$tools.analysis(this.mediaData[key]);
            }
          }
        }
        return {};
      },
    },
    methods: {
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
      showDialog (type, asset) {
        if (type == "opensea") {
          if (!this.config.network.opensea) return;
          let url =
            this.config.network.opensea + '/' +
            this.info.address +
            "/" +
            this.info.tokenId;
          return window.open(url, "_blank");
        }
        if (!this.$tools.needLogin(this.$route.path)) return;
        switch (type) {
          case "sale":
            this.showSaleDialog = true;
            this.order = asset;
            break;
          case "edit_sale":
            this.showSaleDialog = true;
            this.order = asset;
            break;
          case "cancel_sale":
            this.showCancelSaleDialog = true;
            this.order = asset;
            break;
          case "buy":
            this.showBuyDialog = true;
            this.order = asset;
            break;
          case "bid":
            this.showBidDialog = true;
            this.order = asset;
            break;
          case "edit_bid":
            this.showBidDialog = true;
            this.order = asset;
            break;
          case "cancel_bid":
            this.showCancelBidDialog = true;
            this.order = asset;
            break;
          case "accept":
            this.showAcceptDialog = true;
            this.order = asset;
            break;
          case "transfer":
            this.showTransferDialog = true;
            this.order = asset;
            break;
          case "burn":
            this.showBurnDialog = true;
            this.order = asset;
            break;
        }
      },
      closeDialog () {
        this.showSaleDialog = false;
        this.showCancelSaleDialog = false;
        this.showBuyDialog = false;
        this.showBidDialog = false;
        this.showCancelBidDialog = false;
        this.showAcceptDialog = false;
        this.showTransferDialog = false;
        this.showBuyDialog = false;
        this.showBurnDialog = false;
        this.showReportDialog = false;
        this.order = {};
      },
      likeFunction (parameter) {
        let data = {
          address: this.info.address,
          tokenId: this.info.tokenId,
        };
        if (!this.info.like) {
          this.$api("like.add", data).then((res) => {
            if (this.$tools.checkResponse(res)) {
              this.info.like = true;
            }
          });
        } else {
          this.$api("like.remove", data).then((res) => {
            if (this.$tools.checkResponse(res)) {
              this.info.like = false;
            }
          });
        }
      },
      queryLike (address, tokenId) {
        if (!this.$store.state.connected) return;
        let query = {
          nfts: address + ":" + tokenId,
          address: this.$store.state.user.coinbase,
        };
        this.$api("like.listuserlike", query).then((res) => {
          if (this.$tools.checkResponse(res)) {
            if (res.data.length) {
              this.info.like = true;
            }
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      getMedia (nfts) {
        let _nfts = nfts.address + ":" + nfts.tokenId;
        let data = {
          info: _nfts,
        };
        this.$api("nft.getmedia", data).then((res) => {
          this.mediaData = res.data;
        });
      },
      getRoyalties (nfts) {
        let _nfts = nfts.address + ":" + nfts.tokenId;
        let data = {
          info: _nfts,
        };
        this.$api("nft.getroyalties", data).then((res) => {
          for (let key in res.data) {
            if (key == _nfts && res.data[key]) {
              let getroyalties = JSON.parse(res.data[key])
              let count = 0;
              for (let i = 0;i < getroyalties.length;i++) {
                count = count + getroyalties[i]
              }
              that.royalties = that.$tools.decimal(count / 100, 2);
            }
          }
        });
      },
      getDetails () {
        var data = {
          token: this.token.token,
          tokenId: this.token.tokenId,
        };
        let that = this;
        this.$api("nft.detail", data).then(async function (res) {
          if (that.$tools.checkResponse(res)) {
            if (!res.data) {
              that.isFound = false;
              return;
            }
            that.loading = false;
            let list = [res.data]
            that.nftList = list
            that.info = list[0];
            that.queryFunction(list)
            that.getBids();
            if (!that.info.royalties) {
              that.getRoyalties(res.data)
            } else {
              let data = JSON.parse(that.info.royalties)
              let count = 0;
              for (let i = 0;i < data.length;i++) {
                count = count + data[i]
              }
              that.royalties = that.$tools.decimal(count / 100, 2);
            }
            that.info.category = that.$store.getters.category(that.info.categoryId);
            await that.getCreator();
            that.getContract();
            that.getHistorys();
          } else {
            that.$tools.message(res.errmsg);
          }
        });
      },
      getCreator () {
        let data = { address: this.info.creator };
        this.$api('user.info', data).then(res => {
          if (this.$tools.checkResponse(res)) {
            this.creator = res.data;
          }
        })
      },
      getHistorys () {
        var data = {
          token: this.token.token,
          tokenId: this.token.tokenId,
        };
        this.$api("nft.history", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            let historys = [];
            let history;
            for (var i = 0;i < res.data.length;i++) {
              history = res.data[i];
              history.payToken = {
                address: history.paytokenAddress,
                symbol: history.paytokenSymbol,
                decimals: history.paytokenDecimals,
                name: history.paytokenName
              }
              historys.push(history);
            }
            this.historys = historys;
          }
        });
      },
      getBids () {
        return new Promise((resolve, reject) => {
          var data = {
            token: this.token.token,
            tokenId: this.token.tokenId,
          };
          this.$api("nft.bids", data).then((res) => {
            if (this.$tools.checkResponse(res)) {
              let bids = [];
              for (var i = 0;i < res.data.length;i++) {
                let bid = res.data[i];
                bid.payToken = {
                  address: bid.paytokenAddress,
                  symbol: bid.paytokenSymbol,
                  decimals: bid.paytokenDecimals,
                  name: bid.paytokenName
                }
                bids.push(bid);
              }
              this.bids = bids;
            } else {
              this.$tools.message(res.errmsg);
            }
            resolve(res);
          });
        });
      },
      getContract () {
        let data = {
          addresss: this.info.address,
        };
        this.$api("contract.listbyaddr", data).then((res) => {
          if (this.$tools.checkResponse && res.data.length) {
            this.info.contract = res.data[0];
            if (!this.info.contract.name || !this.info.contract.symbol) {
              this.getinfo();
            }
          }
        });
      },
      getinfo () {
        let data = {
          address: this.info.address,
        };
        this.$api("contract.getinfo", data).then((res) => {
          if (this.$tools.checkResponse && res.data.length) {
            this.info.contract.name = res.data.name;
            this.info.contract.symbol = res.data.symbol;
          }
        });
      },
      dialogConfirm () {
        this.closeDialog();
        this.getDetails();
      },
      clickTab (tab) { },
    },
  };
</script>
<style lang="scss" scoped>
  .nft-wrapper {
    width: 100% !important;
    max-width: none;
    padding: 0;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
  }

  .info-section {
    position: absolute;
    left: 360px;
    right: 0;
    top: $headerHeight;
    bottom: 0;
    overflow: hidden;
    .info-section-inner {
      height: 100%;
      overflow-y: auto;
    }
    & .main-wrapper {
      max-width: 100%;
      margin: auto;
      height: 100%;
      overflow-y: auto;
    }
  }

  .preview-section {
    right: 515px;
    width: auto;
    position: absolute;
    left: 0;
    bottom: 0;
    top: $headerHeight;
    padding: 20px 25px;
    overflow-y: auto;
  }

  .info-section {
    width: 474px;
    right: 24px;
    top: 88px;
    bottom: 30px;
    overflow: hidden;
    border-radius: 5px;
    background: #fff;
    left: auto;
    .info-section-inner {
      height: 100%;
      overflow-y: auto;
    }
  }

  .preview-section-image {
    width: 100%;
    height: 100%;
    display: flex;
    padding: 40px 80px 100px 40px;
    justify-content: center;
    .nft-image {
      max-width: 100%;
      height: auto;
      border-radius: $borderRadius;
    }
  }

  .preview-menus {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    margin: 10px 40px 40px;
    padding: 15px;
    background: #fff;
    border-radius: 10px;
    display: flex;
    justify-content: center;
    box-shadow: 0 1px 3px rgb(4 4 5 / 5%);
    .preview-menu {
      cursor: pointer;
      width: 33px;
      height: 33px;
      font-size: 16px;
      border: 1px solid #333;
      border-radius: 50%;
      margin-right: 20px;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #333;
      &.active {
        color: #ffd200;
        border: 1px solid #ffd200;
      }
    }
  }

  .menu-popover {
    .menu {
      cursor: pointer;
      padding: 8px 7px;
      font-size: 13px;
      font-weight: normal;
      border-radius: $borderRadius;
      &:hover,
      &:active {
        background: #f0f0f0;
      }
    }
  }

  .info-main {
    padding: 20px 20px 200px;
  }

  .info-base {
    display: flex;
    flex-direction: column;
  }

  .category-group {
    display: flex;
  }
  .category-item {
    padding: 4px 10px;
    border: 1px solid #e5e5e5;
    border-radius: $borderRadius;
    margin-right: 5px;
    font-size: 8px;
    font-weight: 400;
    color: #1b1b1b;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
  }

  .description {
    margin: 14px 0 10px;
    font-size: 8px;
    font-weight: 400;
    color: #333;
  }

  .nft-name {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    padding-bottom: 10px;
  }

  .nft-metas {
    display: flex;
    padding-bottom: 10px;
    .nft-meta {
      font-size: 9px;
      font-weight: bold;
      margin-right: 10px;
      &.b {
        font-weight: bold;
      }
      &.c3 {
        color: #333;
      }
      &.c9 {
        color: #999;
      }
      &.cblue {
        color: $primaryColor;
      }
    }
  }

  .nft-royalties {
    background: #e6f9ee;
    padding: 8px 10px;
    border-radius: 5px;
    width: 100%;
    font-size: 14px;
    color: #666;
    text-align: center;
  }

  .info-footer-wrap {
    display: flex;
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: #f0f0f0;
    z-index: 2;
    flex-direction: column;
  }
  .info-footer {
    padding: 20px 20px;
    border-radius: 5px;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    flex: 1;
  }
  .info-footer-header {
    display: flex;
    flex-direction: row;
    background: #fff;
    margin: 10px 10px 0;
    border-radius: $borderRadius;
    padding: 10px;
  }
  .info-footer-body {
    display: flex;
    justify-content: center;
    .el-button {
      width: auto;
    }
  }
  .info-footer-footer {
    text-align: center;
    padding-top: 20px;
    font-size: 13px;
  }

  .no-found {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    height: 100vh;
  }

  .el-skeleton-item1 {
    width: 300px !important;
    height: 22px !important;
  }
  .el-skeleton-item2 {
    width: 180px !important;
    height: 15px !important;
  }
  .el-skeleton-item3 {
    width: 50px !important;
    height: 15px !important;
  }
  .el-skeleton-item4 {
    width: 180px !important;
    height: 15px !important;
  }
  .el-skeleton-item5 {
    width: 300px !important;
    height: 18px !important;
  }
  .el-skeleton-item6 {
    margin-bottom: 10px !important;
    width: 434px !important;
    height: 54px !important;
  }
</style>

<style lang="scss">
  .tab-section {
    min-height: 400px !important;
    .el-tabs__header {
      margin-bottom: 20px;
    }
    .el-tabs__item {
      color: $grayColor;
      height: auto;
      line-height: 30px;
      padding: 0 8px;
      font-weight: 400;
      &:hover,
      &:active,
      &:focus,
      &.is-active,
      &is-focus {
        box-shadow: none !important;
        color: #333;
      }
    }
    .el-tabs__active-bar {
      background-color: #333;
    }
  }

  .info-item {
    display: flex;
    padding-bottom: 20px;
    flex-direction: row;
    align-items: flex-start;
    .avatar {
      width: 35px;
      height: 35px;
      border-radius: 50%;
      overflow: hidden;
      margin-right: 5px;
      cursor: pointer;
      .avatar-img {
        width: 100%;
        height: 100%;
      }
    }
    .info {
      position: relative;
      border-bottom: 1px solid #eee;
      flex: 1;
      padding-bottom: 10px;
      .ab-right {
        position: absolute;
        right: 0;
        top: -10px;
      }
      .label {
        font-size: 14px;
        color: #999;
        font-weight: 400;
        margin-bottom: 5px;
        line-height: 14px;
        &.expired {
          text-decoration: line-through;
        }
      }
      .c9 {
        color: #999 !important;
      }
      .nft-op {
        padding: 5px 8px;
        color: #fff;
        background: $primaryColor;
        border-radius: $borderRadius;
        display: inline-block;
        cursor: pointer;
        &.cancel {
          color: #409eff;
          background: #ecf5ff;
        }
      }
      .label-left {
        flex: 1;
      }
      .name {
        font-size: 15px;
        margin-bottom: 5px;
        display: flex;
        cursor: pointer;
        align-items: center;
      }
      .bfont {
        color: #333;
      }
      .ml5 {
        margin-left: 5px;
      }
      .t {
        color: #999;
      }
      .bluec {
        color: $primaryColor;
      }
      .text {
        flex: 1;
      }
    }
  }

  .propertys {
    padding-top: 20px;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    .property-item {
      display: flex;
      border-radius: $borderRadius;
      flex-direction: column;
      border: $border;
      padding: 10px;
      margin: 0 10px 10px 0;
      text-align: center;
      justify-content: center;
    }
    .lalbel {
      font-size: 14px;
      color: #999;
    }
    .name {
      font-size: 14px;
      color: #333;
    }
  }
</style>

