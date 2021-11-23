<template>
<div class="tab-content-list">
  <div :key="index" class="info-item">
    <div class="avatar">
      <avatar @click="goAccount(asset.address)" shape="circular" :imageUrl="$filters.fullImageUrl(asset.avatar)" :address="asset.address" :imgWidth="35" :imgHeight="35"></avatar>
    </div>
    <div v-if="!asset.onsell" class="info">
      <div class="label">1 {{$t('details.text1')}}</div>
      <div @click="goAccount(asset.address)" class="name">
        <span class="text">
          {{asset.nickname || $filters.ellipsisAddress(asset.address) }}
        </span>
        <span class="t">{{asset.time}}</span>
      </div>
    </div>
    <div v-else class="info">
      <div class="label flex">
        <div class="label-left">
          1 {{$t('details.text2')}} <span class="bfont"> {{ asset.price }}{{asset.payToken.symbol}} </span>
        </div>
        <span v-if="!user || user.coinbase == asset.address" class="nft-op cancel" @click="cancel">{{$t('details.cancel')}}</span>
        <span v-else class="nft-op" @click="buy(asset)">{{$t('details.buy')}}</span>
      </div>
      <div @click="goAccount(asset.address)" class="name">
        <span class="text" >
          {{ asset.nickname || $filters.ellipsisAddress( asset.address ) }}
        </span>
        <span class="t">{{ $filters.timeFormat(asset.createTime) }}</span>
      </div>
    </div>
  </div>
</div>
</template>
<script>
import { computed } from 'vue'

export default {
  name: "Owners",
  props: {
    asset: {
      type: Object,
      default: {},
    }
  },
  data(){
    return {}
  },
  computed:{
    user(){
      return this.$store.state.user;
    }
  },
  methods: {
    cancel(){
      this.$emit('cancel', this.asset);
    },
    buy(){
      this.$emit('buy', this.asset);
    },
    goAccount(address){
      this.$router.push({ path: "/account/"+address });
    }
  }
}
</script>
