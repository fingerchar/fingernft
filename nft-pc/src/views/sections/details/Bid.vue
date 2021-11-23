<template>
<div class="tab-content-list">
  <div class="owner-bid-switch" v-if="connected && myBid">
    <el-switch v-model="showMyBid"
      :activeText="$t('details.showBid')"></el-switch>
  </div>

  <div v-for="(bid, index) in validBids" :key="index" class="info-item">
    <div class="avatar">
      <avatar @click="goAccount(bid.address)" :imageUrl="$filters.fullImageUrl(bid.avatar)" :address="bid.address" :imgWidth="35" :imgHeight="35" shape="circular"></avatar>
    </div>
    <div class="info">
      <div class="flex justify-between">
        <template v-if="bid.type == 5">
          <div class="label" :class="{'expired': bid.expired}">{{$t('details.offered')}} <span class="bfont">{{ $tools.singlePrice(bid.sellValue, bid.buyerValue)}} {{bid.payToken && bid.payToken.symbol ? bid.payToken.symbol : ""}}</span> {{$t('details.for')}} {{bid.buyerValue}} {{$t('details.editions')}}</div>
        </template>
        <template v-if="bid.type == 6">
          <div class="label" :class="{'expired': bid.expired}">{{$t('details.editOffered')}}<span class="bfont">{{ $tools.singlePrice(bid.sellValue, bid.buyerValue) }} {{bid.payToken && bid.payToken.symbol ? bid.payToken.symbol : ""}}</span> {{$t('details.for')}} {{bid.buyerValue}} {{$t('details.editions')}}</div>
        </template>
        <template v-if="bid.type == 7">
          <div class="label" :class="{'expired': bid.expired}">{{$t('details.cancelOffered')}}<span class="bfont">{{ $tools.singlePrice(bid.sellValue, bid.buyerValue) }} {{bid.payToken && bid.payToken.symbol ? bid.payToken.symbol : ""}}</span> {{$t('details.for')}} {{bid.buyerValue}} {{$t('details.editions')}}</div>
        </template>
        <template v-else-if="bid.type == 8">
          <div class="label" :class="{'expired': bid.expired}">{{$t('details.accept')}} <span class="bfont">{{ $tools.singlePrice(bid.sellValue, bid.buyerValue) }} {{bid.payToken && bid.payToken.symbol ? bid.payToken.symbol : ""}}</span> {{$t('details.for')}} {{bid.sells}} {{$t('details.editions')}}</div>
        </template>
        <div class="right" v-if="bid.active">
          <span v-if="bid.isOwner" @click.stop="cancelBid(bid)" plain type="success" class="nft-op cancel" >{{$t('details.cancel')}}</span>
          <span v-else-if="isOwner" @click.stop="acceptBid(bid)" plain type="success" class="nft-op cancel" >{{$t('details.accept')}}</span>
        </div>
      </div>
      
      <div class="name" @click="goAccount(bid.address)">
        <span class="text">
          <span class="c9">{{$t('details.by')}}</span>
          {{bid.nickname || $filters.ellipsisAddress(bid.address) }}
        </span>
        <span class="t">{{ $filters.timeFormat(bid.time) }}</span>
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
    isOwner: {
      type: Object,
      default: {},
    },
  },
  data(){
    return {
      showMyBid: false,
    }
  },
  computed:{
    connected(){
      return this.$store.state.connected;
    },
    user(){
      return this.$store.state.user;
    },
    validBids(){
      let bids = this.analysisBid(this.bids);
      if(!this.showMyBid)  return bids;
      return bids.filter(function(bid){
        return bid.isOwner;
      });
    },
  },
  methods: {
    goAccount(address){
      this.$router.push({ path: "/account/"+address });
    },
    analysisBid(bids){
      let _bids = [];
      for(var i = 0; i < bids.length; i++){
        let bid = bids[i];
        bid.payToken = this.$store.getters.payToken(bid.sellToken);
        if( (bid.type == this.$sdk.valueCommonType("BID")
            || bid.type == this.$sdk.valueCommonType("EDIT_BID") )
          && !bid.expired){
          bid.active = true;
        }
        if(this.connected &&
            bid.address.toLocaleLowerCase() == this.user.coinbase.toLocaleLowerCase() ){
          bid.isOwner = true;
        }
        _bids.push(bid);
      }
      return _bids;
    },
    cancelBid(bid){
      this.$emit("cancel", bid);
    },
    acceptBid(bid){
      this.$emit('accept', bid);
    },
  }
}
</script>

<style lang="scss" scoped>
.owner-bid-switch{
  padding-bottom: 10px;
  margin-bottom: 20px;
  border-bottom: $border;
}

</style>

<style lang="scss">
.owner-bid-switch{
  .el-switch__label{
    &.is-active{
      color: $primaryColor;
    }
  }
}

</style>
