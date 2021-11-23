<template>
<div class="tab-content-list">
  <div v-for="(his,index) in historys" :key="index" class="info-item">
    <div class="avatar">
      <avatar @click="goAccount(his.address)" :imageUrl="$filters.fullImageUrl(his.avatar)" :address="his.address" :imgWidth="35" :imgHeight="35" shape="circular"></avatar>
    </div>
    <div class="info">
      <template v-if="his.type == 1">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.putOnSale')}}<span class="bfont">{{ $tools.singlePrice(his.buyerValue, his.sellValue)}} {{his.payToken ? his.payToken.symbol : ""}}</span></div>
      </template>
      <template v-else-if="his.type == 2">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.editSaleFor')}} <span class="bfont">{{ $tools.singlePrice(his.buyerValue, his.sellValue) }} {{his.payToken ? his.payToken.symbol : ""}}</span></div>
      </template>
      <template v-else-if="his.type == 3">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.cancelSaleFor')}} <span class="bfont">{{ $tools.singlePrice(his.buyerValue, his.sellValue) }} {{his.payToken ? his.payToken.symbol : ""}}</span></div>
      </template>
      <template v-else-if="his.type == 4">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.boughtFor')}}<span class="bfont">{{$tools.singlePrice(his.buyerValue, his.sellValue)}} {{his.payToken ? his.payToken.symbol : ""}}</span></div>
      </template>
      <template v-else-if="his.type == 5">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.offered')}}<span class="bfont">{{ $tools.singlePrice(his.sellValue, his.buyerValue) }} {{his.payToken ? his.payToken.symbol: ""}}</span> {{$t('details.for')}} {{his.payToken ? his.payToken.symbol : ""}} {{$t('details.editions')}}</div>
      </template>
       <template v-else-if="his.type == 6">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.editOffered')}} <span class="bfont">{{$tools.singlePrice(his.sellValue, his.buyerValue)}} {{his.payToken ? his.payToken.symbol : ""}}</span> {{$t('details.for')}} {{his.buyerValue}} {{$t('details.editions')}}</div>
      </template>
      <template v-else-if="his.type == 7">
        <div class="label" :class="{'expired': his.expired}"> {{$t('details.cancelOffered')}} <span class="bfont">{{ $tools.singlePrice(his.sellValue, his.buyerValue) }} {{his.payToken ? his.payToken.symbol : ""}}</span> {{$t('details.for')}} {{his.buyerValue}} {{$t('details.editions')}}</div>
      </template>
      <template v-else-if="his.type == 8">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.accept')}} <span class="bfont">{{$tools.singlePrice(his.sellValue, his.buyerValue)}} {{his.payToken ? his.payToken.symbol : ""}}</span> {{$t('details.for')}} {{his.sells}} {{$t('details.editions')}}</div>
      </template> 
      <template v-else-if="his.type == 9">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.Mint')}}</div>
      </template> 
      <template v-else-if="his.type == 14">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.Burn')}}</div>
      </template>
      <template v-else-if="his.type == 15">
        <div class="label" :class="{'expired': his.expired}">{{$t('details.Transfer')}}</div>
      </template>
      <div @click="goAccount(his.address)" class="name">
        <span class="text">
          <span class="c9">{{$t('details.by')}}</span>
            {{his.nickname || $filters.ellipsisAddress(his.address) }}
        </span>
        <span class="t">{{ $filters.timeFormat(his.time) }}</span>
      </div>
    </div>
  </div>

</div>
</template>

<script>
export default {
  name: "History",
  props: {
    historys: {
      type: Array,
      default: [],
    },
  },
  data(){
    return {}
  },
  methods: {
    goAccount(address){
      this.$router.push({ path: "/account/"+address });
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
