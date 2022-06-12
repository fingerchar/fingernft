<template>
  <div class="tab-content-list">
    <div class="owner-bid-switch" v-if="connected && myBid">
      <el-switch v-model="showMyBid" :activeText="$t('details.showBid')"></el-switch>
    </div>
    <div v-for="(bid, index) in validBids" :key="index" class="info-item">
      <div class="avatar" v-if="bid.type != 8">
        <avatar @click="goAccount(bid.fromUser.address)" :imageUrl="$filters.fullImageUrl(bid.fromUser.avatar)" :address="bid.fromUser.address" :imgWidth="35" :imgHeight="35" shape="circular"></avatar>
      </div>
      <div class="avatar" v-else>
        <avatar @click="goAccount(bid.toUser.address)" :imageUrl="$filters.fullImageUrl(bid.toUser.avatar)" :address="bid.toUser.address" :imgWidth="35" :imgHeight="35" shape="circular"></avatar>
      </div>
      <div class="info">
        <div class="flex justify-between">
          <template v-if="bid.type == 5">
            <div class="label" :class="{'expired': bid.expired}">{{$t('details.offered')}} <span class="bfont">{{ $tools.singlePrice(bid.content.sellValue, bid.content.buyerValue, bid.payToken)}}
                {{bid.paytokenSymbol}}</span> {{$t('details.for')}} {{bid.content.buyerValue}} {{$t('details.editions')}}</div>
          </template>
          <template v-if="bid.type == 6">
            <div class="label" :class="{'expired': bid.expired}">{{$t('details.editOffered')}}<span class="bfont">{{ $tools.singlePrice(bid.content.sellValue, bid.content.buyerValue, bid.payToken) }}
                {{bid.paytokenSymbol}}</span> {{$t('details.for')}} {{bid.content.buyerValue}} {{$t('details.editions')}}</div>
          </template>
          <template v-if="bid.type == 7">
            <div class="label" :class="{'expired': bid.expired}">{{$t('details.cancelOffered')}}<span class="bfont">{{ $tools.singlePrice(bid.content.sellValue, bid.content.buyerValue, bid.payToken) }}
                {{bid.paytokenSymbol}}</span> {{$t('details.for')}} {{bid.content.buyerValue}} {{$t('details.editions')}}</div>
          </template>
          <template v-else-if="bid.type == 8">
            <div class="label" :class="{'expired': bid.expired}">{{$t('details.accept')}} <span class="bfont">{{ $tools.singlePrice(bid.content.sellValue, bid.content.buyerValue, bid.payToken) }}
                {{bid.paytokenSymbol}}</span> {{$t('details.for')}} {{bid.content.sells }} {{$t('details.editions')}}</div>
          </template>
          <div class="right" v-if="isOwner && !bid.expired && bid.type != 8">
            <span v-if="bid.isOwner " @click.stop="cancelBid(bid)" plain type="success" class="nft-op cancel">{{$t('details.cancel')}}</span>
            <span v-else-if="isOwner" @click.stop="acceptBid(bid)" plain type="success" class="nft-op cancel">{{$t('details.accept')}}</span>
          </div>
        </div>

        <div class="name" @click="goAccount(bid.fromUser.address)" v-if="bid.type != 8">
          <span class="text">
            <span class="c9">{{$t('details.by')}}</span>
            {{bid.fromUser.nickname || $filters.ellipsisAddress(bid.fromUser.address) }}
          </span>
          <span class="t">{{ $filters.timeFormat(bid.createTime) }}</span>
        </div>
        <div class="name" @click="goAccount(bid.toUser.address)" v-else>
          <span class="text">
            <span class="c9">{{$t('details.by')}}</span>
            {{bid.toUser.nickname || $filters.ellipsisAddress(bid.toUser.address) }}
          </span>
          <span class="t">{{ $filters.timeFormat(bid.createTime) }}</span>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    name: "Bid",
    props: {
      bids: {
        type: Array,
        default: []
      },
      myBid: {
        type: Object,
        default: {}
      },
      asset: {
        type: Object,
        default: {},
      },
    },
    data () {
      return {
        showMyBid: false,
      }
    },
    computed: {
      connected () {
        return this.$store.state.connected;
      },
      user () {
        return this.$store.state.user;
      },
      isOwner(){
        if(!this.connected || !this.asset || this.user.coinbase != this.asset.itemOwner) return false;
        return true;
      },
      validBids () {
        let bids = this.analysisBid(this.bids);
        if (!this.showMyBid) return bids;
        return bids.filter(function (bid) {
          return bid.isOwner;
        });
      },
    },
    methods: {
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
      analysisBid (bids) {
        let _bids = [];
        for (var i = 0;i < bids.length;i++) {
          let bid = bids[i];
          if ((bid.type == this.$sdk.valueCommonType("BID")
            || bid.type == this.$sdk.valueCommonType("EDIT_BID"))
            && !bid.expired) {
            bid.active = true;
          }
          if (bid.type == 8) {
            if (this.connected &&
              bid.to.toLocaleLowerCase() == this.user.coinbase.toLocaleLowerCase()) {
              bid.isOwner = true;
            }
          } else {
            if (this.connected &&
              bid.from.toLocaleLowerCase() == this.user.coinbase.toLocaleLowerCase()) {
              bid.isOwner = true;
            }
          }

          _bids.push(bid);
        }
        return _bids;
      },
      cancelBid (bid) {
        let order = bid.content
        this.$emit("cancel", order);
      },
      acceptBid (bid) {
        let order = bid.content
        order.payToken = {
          address: order.paytokenAddress,
          symbol: order.paytokenSymbol,
          decimals: order.paytokenDecimals,
          name: order.paytokenName
        }
        order.singlePrice = this.$tools.singlePrice(
          order.sellValue,
          order.buyerValue,
          order.payToken
        );
        this.$emit('accept', order);
      },
    }
  }
</script>

<style lang="scss" scoped>
  .owner-bid-switch {
    padding-bottom: 10px;
    margin-bottom: 20px;
    border-bottom: $border;
  }
</style>

<style lang="scss">
  .owner-bid-switch {
    .el-switch__label {
      &.is-active {
        color: $primaryColor;
      }
    }
  }
</style>
