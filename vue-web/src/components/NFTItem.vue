<template>
  <div class="nft-item" :class="size">
    <div class="inner">
      <div class="cover-padding">
        <div class="cover" @click="goDetail">
          <el-image class="cover-image" placeholder="loading" :src="$filters.fullImageUrl(nftURI.image)" fit="cover">
            <template v-slot:placeholder>
              <el-skeleton class="placeholder-image" animated>
                <template #template>
                  <el-skeleton-item class="nft-image-skeleton" variant="h3" />
                </template>
              </el-skeleton>
            </template>
            <template v-slot:error>
              <el-image class="error-image" :src="require('@/assets/create-img/non-existent.png')" fit="contain"></el-image>
            </template>
          </el-image>

          <div class="collection-btn" :class="nft.like ? 'active' : ''" @click.stop="clickLike()">
            <span class="iconfont" :class="nft.like ? 'icon-collection-fill' : 'icon-collection'"></span>
          </div>
        </div>
      </div>
      <div class="descript">
        <div class="d">
          <div class="d-left d-top">
            <span @click="goDetail" class="bfont cpointer">{{ nftURI.name }}</span>
          </div>
          <div class="d-right">
            <span class="bid" @click="goDetail" v-if="highestBid">
              {{ highestBid.singlePrice }}{{ highestBid.paytokenSymbol }}
            </span>
            <span class="bid" @click="goDetail" v-else-if="isOwner">
              {{ $t("nftItem.pab") }}
            </span>
            <span class="bid" @click="goDetail" v-else>
              {{$t("nftItem.pab")}}
            </span>
          </div>
        </div>
        <div class="d">
          <div class="d-left" v-if="asset.onsell">
            <div class="avatar">
              <avatar @click="goUser(asset.owner)" class="bflex cpointer" :imageUrl="$filters.fullImageUrl(asset.user.avatar)" :address="asset.owner" :imgWidth="18" :imgHeight="18" shape="circular"></avatar>
            </div>
            <span class="price bfont">
              {{ $tools.noDecimalsValue(asset.buyerValue, 1, asset.paytokenDecimals) }}
              {{ asset.paytokenSymbol }}
            </span>
            <span class="stock">
              1 of 1</span>
          </div>
          <div class="d-left" v-else>
            <span class="nosale bfont">{{ $t("nftItem.nfs") }}</span>
            <span class="stock"> 0 of 1</span>
          </div>

          <div class="d-right">
            <el-popover class="right-item" placement="top" popper-class="more-menu-popover" title="" :width="100" v-model:visible="visible">
              <div class="menus">
                <template v-if="isOwner">
                  <div v-if="!asset.onsell" class="menu" @click="showDialog('sale', asset)">
                    {{ $t("nftItem.sale") }}
                  </div>
                  <template v-else>
                    <div class="menu" @click="showDialog('edit_sale', asset)">
                      {{ $t("nftItem.editSale") }}
                    </div>
                    <div class="menu" @click="showDialog('cancel_sale', asset)">
                      {{ $t("nftItem.cancelSale") }}
                    </div>
                  </template>
                  <div class="menu" @click="showDialog('transfer', asset)">
                    <img src="@/assets/img/nft/transfer.png" class="menu-icon" />
                    {{ $t("nftItem.transfer") }}
                  </div>
                  <div class="menu" @click="showDialog('burn', asset)">
                    <img src="@/assets/img/nft/destroy.png" class="menu-icon" />
                    {{ $t("nftItem.burn") }}
                  </div>
                </template>
                <template v-else>
                  <div class="menu" v-if="asset.onsell" @click="showDialog('buy', asset)">
                    {{ $t("nftItem.buy") }}
                  </div>
                  <div class="menu" @click="showDialog('bid')" v-if="!myBid">
                    {{ $t("nftItem.bid") }}
                  </div>
                  <template v-else>
                    <div class="menu" @click="showDialog('edit_bid', myBid)">
                      {{ $t("nftItem.editBid") }}
                    </div>
                    <div class="menu" @click="showDialog('cancel_bid', myBid)">
                      {{ $t("nftItem.cancelBid") }}
                    </div>
                  </template>
                </template>

                <div class="menu" @click="showDialog('opensea')" v-if="config.network.opensea">
                  {{ $t("nftDetail.viewOpensea") }}
                </div>
              </div>
              <template #reference>
                <span @click="visible = true" class="bfont iconfont icon-ellipsis"></span>
              </template>
            </el-popover>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import MixinsNFTInfo from "@/mixins/NftInfo";
  export default {
    name: "NFTItem",
    mixins: [MixinsNFTInfo],
    props: {
      index: {
        type: [Number, String],
        default: "",
      },
      nft: {
        type: Object,
        default: {},
      },
      address: {
        type: String,
        default: "",
      },
      size: {
        type: String,
        default: "",
      },
    },
    data () {
      return {
        loading: true,
        visible: false,
      };
    },
    created () {
      this.$nextTick(function () {
        this.loading = false;
      });
    },
    mounted () {
      let that = this;
    },
    computed: {
      isOwner(){
        var itemOwner = this.nft.items[0].itemOwner;
        if(!this.connected || this.user.coinbase != itemOwner) return false;
        return true;
      },
      config () {
        return this.$store.state.config;
      },
      connected () {
        return this.$store.state.connected;
      },
      asset(){
        var item = this.nft.items[0];
        if(item.onsell && this.nft.sale){
          var sale = this.nft.sale;
          return { ...sale, ...item };
        }
        return item;
      },
      myBid () {
        if (!this.connected || !this.nft.bids) return;
        var itemOwner = this.nft.items[0].itemOwner;
        if (itemOwner == this.user.coinbase) return;
        return this.getActiveAddressBid(this.nft.bids, this.user.coinbase);
      },
      highestBid () {
        if (!this.nft.bids || !this.nft.items) return;
        var itemOwner = this.nft.items[0].itemOwner;
        return this.getHighestBid(this.nft.bids, itemOwner);
      },
      nftURI () {
        if (this.nft.metadataContent) {
          return this.$tools.analysis(this.nft.metadataContent);
        } else if (this.nft.media) {
          return this.$tools.analysis(this.nft.media);
        }
        return {};
      },
      user () {
        return this.$store.state.user;
      },
    },
    methods: {
      goUser (address) {
        this.$router.push({ path: "/account/" + address });
      },
      clickLike () {
        let data = {
          address: this.nft.address,
          tokenId: this.nft.tokenId,
        };
        if (!this.nft.like) {
          this.$api("like.add", data).then((res) => {
            if (this.$tools.checkResponse(res)) {
              this.$emit("onLike", data, true);
            }
          });
        } else {
          this.$api("like.remove", data).then((res) => {
            if (this.$tools.checkResponse(res)) {
              this.$emit("onLike", data, false);
            }
          });
        }
      },
      showDialog (name, asset) {
        let nft = this.nft;
        if (name == "opensea") {
          if (!this.config.network.opensea) return;

          let url = this.config.network.opensea + '/' + nft.address + "/" + nft.tokenId;
          window.open(url, "_blank");
          return;
        }
        if (!this.$tools.needLogin(this.$route.path)) return;
        let order = asset;
        this.$emit("showDialog", name, nft, order, this.nftURI);
      },
      goDetail () {
        var ids = this.nft.address + ":" + this.nft.tokenId;
        this.$router.push("/detail/" + ids);
      },
    },
  };
</script>

<style lang="scss" scoped>
  .menu-icon {
    width: 12px;
    margin-right: 7px;
    vertical-align: middle;
  }
  .huomiao-icon {
    width: 13px;
    height: 13px;
  }
  .d-right {
    .iconfont {
      font-size: 18px;
      margin-left: 5px;
      color: #aaa;
      cursor: pointer;
      &.active {
        color: $redColor;
      }
    }
  }
  .auction-info {
    background-color: #f06f6f;
    border-radius: 20px;
    color: #fff;
    text-align: center;
    margin-top: -20px;
    position: relative;
    font-weight: 600;
    padding: 5px 20px;
    .count-time {
      display: flex;
      align-items: center;
      justify-content: center;
      .huomiao-icon {
        width: 17px;
        height: 16px;
        margin-right: 5px;
      }
    }
  }

  .nft-item {
    width: 25%;
    position: relative;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    margin-bottom: 20px;
    padding: 0 10px;
    &.big {
      width: 33.33%;
    }
    .inner {
      position: relative;
      background: #fff;
      border-radius: $borderRadius;
      overflow: hidden;
      --coverWidth: 100%;
    }
    .like {
      position: absolute;
      z-index: 100;
      right: 5px;
      top: 5px;
      .iconfont {
        cursor: pointer;
        margin-right: 5px;
        margin-top: 6px;
        font-size: 18px;
        border-radius: 18px;
        color: #aaa;
        &.active {
          color: $redColor;
        }
      }
    }
    .cover-padding {
      position: relative;
      padding-bottom: calc(var(--coverWidth) / 1.1);
    }
    .cover {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
    }
    .hot-tip {
      display: flex;
      align-items: center;
      position: absolute;
      left: 0;
      top: 0;
      border-radius: 6px;
      border: 2px solid #3c4aff;
      padding: 5px;
      background-color: #fff;
    }
    .data-text {
      font-weight: bold;
      margin: 0px 10px;
    }
    .x-text-8A8A8A {
      color: #8a8a8a;
    }
    .collection-btn {
      width: 35px;
      height: 35px;
      text-align: center;
      line-height: 35px;
      position: absolute;
      right: 16px;
      top: 16px;
      background: #fff;
      border-radius: 50%;
      font-size: 35px;
      color: #666;
      box-shadow: 0 0 5px #ccc;
      &.active {
        color: #ffd200;
      }
    }
    .avatar {
      margin-right: 3px;
    }
    .descript {
      padding: 5px 10px;
    }
    .d {
      padding: 5px;
      display: flex;
    }
    .d-left {
      display: flex;
      flex: 1;
      align-items: center;
    }
    .d-right {
      display: flex;
      flex: 1;
      align-items: center;
      justify-content: flex-end;
    }
    .bid {
      white-space: nowrap;
      color: $primaryColor;
      cursor: pointer;
      font-weight: 400;
      font-size: 12px;
    }
    .price {
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      font-size: 12px;
      font-family: Montserrat-Regular;
      font-weight: 400;
      color: #1d1e22;
      margin-right: 5px;
    }
    .nosale {
      text-overflow: ellipsis;
      font-size: 12px;
      font-weight: 400;
      color: $grayColor;
    }
    .stock {
      display: flex;

      flex: 1;
      margin-left: 4px;
      margin-right: 6px;
      white-space: nowrap;
      font-size: 12px;
      font-weight: 400;
      color: #999;
      text-align: center;
    }
  }
  .bfont {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
  .d-top {
    width: 50%;
    flex: 0 !important;
  }

  .more-menu-popover {
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

  .sale-bid {
    display: flex;
    flex-direction: column;
    button {
      border: none;
      background: none;
      font-size: 14px;
      font-family: Montserrat-Regular;
      font-weight: 500;
      color: #1d1e22;
      line-height: 26px;
      cursor: pointer;
      &:hover {
        color: red;
        border-color: inherit;
        background-color: inherit;
      }
      &:focus {
        color: red;
        border-color: inherit;
        background-color: inherit;
      }
    }
  }

  .nft-image-skeleton {
    width: 90%;
    height: 90%;
  }
</style>
