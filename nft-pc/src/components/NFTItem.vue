<template>
  <div class="nft-item">
    <div class="inner">
      <div class="cover-padding">
        <div class="cover" @click="goDetail">
          <el-image
            class="cover-image"
            placeholder="loading"
            :src="$filters.fullImageUrl(nftURI.image)"
            fit="contain"
          >
            <template v-slot:placeholder>
              <el-skeleton class="placeholder-image" animated>
                <template #template>
                  <el-skeleton-item
                    class="nftItem-image-skeleton"
                    variant="h3"
                  />
                </template>
              </el-skeleton>
            </template>
            <template v-slot:error>
              <el-image
                class="error-image"
                :src="require('@/assets/create-img/non-existent.png')"
                fit="contain"
              ></el-image>
            </template>
          </el-image>
          <div
            class="collection-btn"
            :class="nft.like ? 'active' : ''"
            @click.stop="clickLike()"
          >
            <span
              class="iconfont"
              :class="nft.like ? 'icon-collection-fill' : 'icon-collection'"
            ></span>
          </div>
        </div>
      </div>
      <div class="descript">
        <div class="d">
          <div class="d-left d-top">
            <div class="avatar">
              <avatar
                @click="goUser(asset.address)"
                class="bflex cpointer"
                :imageUrl="$filters.fullImageUrl(asset.avatar)"
                :address="asset.address"
                :imgWidth="18"
                :imgHeight="18"
                shape="circular"
              ></avatar>
            </div>
            <span @click="goDetail" class="bfont cpointer">{{ nftURI.name }}</span>
          </div>

          <div class="d-right" v-if="nft.itemList && nft.itemList.length">
            <span class="bid" @click="goDetail" v-if="highestBid">
              {{ highestBid.singlePrice }}{{ highestBid.payToken.symbol }}
            </span>
            <span class="bid" @click="goDetail" v-else-if="isOwner">
              {{ $t("nftItem.pab") }}
            </span>
            <span class="bid" @click="showDialog('bid')" v-else>{{
              $t("nftItem.pab")
            }}</span>
          </div>
        </div>
        <div class="d">
          <div class="d-left" v-if="asset.onsell">
            <span class="price bfont">
              {{ asset.price }}
              {{ asset.payToken ? asset.payToken.symbol : "" }}
            </span>
            <span class="stock"> 1 of 1 </span>
          </div>
          <div class="d-left" v-else>
            <span class="nosale bfont">{{ $t("nftItem.nfs") }}</span>
            <span class="stock"> 0 of 1</span>
          </div>

          <div class="d-right">
            <el-popover
              class="right-item"
              placement="top"
              popper-class="more-menu-popover"
              title=""
              :width="100"
              v-model:visible="visible"
            >
              <div class="menus">
                <template v-if="isOwner">
                  <template v-if="asset.onsell">
                    <div
                      class="menu"
                      @click="showDialog('edit_sale', asset)"
                    >
                      <img src="@/assets/img/nft/sale.png" class="menu-icon" />
                      {{ $t("nftItem.editSale") }}
                    </div>
                    <div class="menu" @click="showDialog('cancel_sale', asset)" >
                      <img src="@/assets/img/nft/sale.png" class="menu-icon" />
                      {{ $t("nftItem.cancleSale") }}
                    </div>
                  </template>
                  <div v-else class="menu" @click="showDialog('sale', asset)" >
                    <img src="@/assets/img/nft/sale.png" class="menu-icon" />
                    {{ $t("nftItem.sale") }}
                  </div>

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
                  <div class="menu"
                    v-if="asset.onsell"
                    @click="showDialog('buy', asset)"
                  >
                    <img src="@/assets/img/nft/sale.png" class="menu-icon" />
                    {{ $t("nftItem.buy") }}
                  </div>
                  <div class="menu" @click="showDialog('bid')" v-if="!myBid">
                    <img src="@/assets/img/nft/bid.png" class="menu-icon" />
                    {{ $t("nftItem.bid") }}
                  </div>
                  <template v-else>
                    <div class="menu" @click="showDialog('edit_bid', myBid)">
                      <img src="@/assets/img/nft/bid.png" class="menu-icon" />
                      {{ $t("nftItem.editBid") }}
                    </div>
                    <div class="menu" @click="showDialog('cancel_bid', myBid)">
                      <img src="@/assets/img/nft/bid.png" class="menu-icon" />
                      {{ $t("nftItem.cancelBid") }}
                    </div>
                  </template>
                </template>
                <div
                  class="menu"
                  @click="showDialog('opensea')"
                >
                  {{ $t("nftDetail.viewOpensea") }}
                </div>
              </div>
              <template #reference>
                <span
                  @click="visible = true"
                  class="bfont iconfont icon-ellipsis"
                ></span>
              </template>
            </el-popover>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import BigNumber from "bignumber.js";
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
    isItem: {
      type: Boolean,
      default: false,
    },
    address: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      loading: true,
      visible: false,
    };
  },
  created() {
    this.$nextTick(function () {
      this.loading = false;
    });
  },
  mounted() {
    let that = this;
  },
  computed: {
    isOwner(){
      if(!this.asset) return false;
      if(!this.connected || this.user.coinbase != this.asset.address) return false;
      return true;
    },
    connected() {
      return this.$store.state.connected;
    },
    asset(){
      if(this.nft.owners && this.nft.owners.length) {
        return this.analysisAsset(this.nft.owners[0]);
      }
    },
    myBid() {
      if (!this.nft.bids) return;
      if (!this.connected) return;
      let myBid = this.getActiveAddressBid(this.nft.bids, this.user.coinbase);
      return myBid;
    },
    highestBid() {
      if (!this.nft.bids) return;
      let highestBid = this.getHighestBid(this.nft.bids);
      return highestBid;
    },
    nftURI() {
      if (this.nft.nft.metadataContent) {
        return this.$tools.analysis(this.nft.nft.metadataContent);
      }else if (this.nft.media) {
        return this.$tools.analysis(this.nft.media);
      }
      return {};
    },
    user() {
      return this.$store.state.user;
    },
    openseaUrl(){
      return this.$store.state.openseaUrl;
    },
  },
  methods: {
    thumbnail(url) {
      if (url) {
        if (url.endsWith(".gif")) return url;
        return url + "!300x300";
      } else {
        return "";
      }
    },
    goUser(address) {
      this.$router.push({ path: "/account/" + address });
    },
    clickLike() {
      let data = {
        address: this.nft.nft.address,
        tokenId: this.nft.nft.tokenId,
      };
      if (!this.nft.like) {
        this.$api("like.add", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.$emit("onLike", this.index, true);
          }
        });
      } else {
        this.$api("like.remove", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.$emit("onLike", this.index, false);
          }
        });
      }
    },
    showDialog(name, asset) {
      let nft = this.nft.nft;
      if (name == "opensea") {
        let url = this.openseaUrl + nft.address + "/" + nft.tokenId;
        window.open(url, "_blank");
        return;
      }
      if (!this.$tools.needLogin(this.$route.path)) return;
      let order = asset;
      this.$emit("showDialog", name, nft, order, this.asset, this.nftURI);
    },
    goDetail() {
      var ids = this.nft.nft.address + ":" + this.nft.nft.tokenId;
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
.nft-item {
  width: 25%;
  position: relative;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  margin-bottom: 20px;
  padding: 0 10px;
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
    padding-bottom: calc(var(--coverWidth) / 1.33);
  }
  .cover {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    cursor: pointer;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
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
    width: 23px;
    height: 23px;
    text-align: center;
    line-height: 23px;
    position: absolute;
    right: 0;
    top: 0;
    background: #fff;
    border-bottom-left-radius: 5px;
    font-size: 16px;
    color: #666;
    &.active {
      color: #ffd200;
    }
  }
  .avatar {
    margin-right: 3px;
  }
  .descript {
    padding: 5px 0;
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

@media screen and (max-width: 1320px) {
  .nft-item {
    width: 25%;
  }
}

@media screen and (max-width: 1040px) {
  .nft-item {
    width: 33.33%;
  }
}

@media screen and (max-width: 780px) {
  .nft-item {
    width: 50%;
  }
}

.nftItem-image-skeleton {
  width: 90%;
  height: 90%;
}
</style>
