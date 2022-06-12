<template>
  <div class="tab-content-list">
    <div v-for="(his,index) in historys" :key="index" class="info-item">
      <div class="avatar" v-if="his.type != 4 && his.type != 8">
        <avatar @click="goAccount(his.from)" :imageUrl="$filters.fullImageUrl(his?.fromUser?.avatar)" :address="his.from" :imgWidth="35" :imgHeight="35" shape="circular"></avatar>
      </div>
      <div class="avatar" v-else>
        <avatar @click="goAccount(his.to)" :imageUrl="$filters.fullImageUrl(his?.toUser?.avatar)" :address="his.to" :imgWidth="35" :imgHeight="35" shape="circular"></avatar>
      </div>
      <div class="info">
        <template v-if="his.type == 1">
          <div class="label" :class="{'expired': his.expired}">{{$t('details.putOnSale')}}<span class="bfont">{{ $tools.singlePrice(his.content.buyerValue, his.content.sellValue,his.payToken)}}
              {{his.content.paytokenSymbol}}</span></div>
        </template>
        <template v-else-if="his.type == 2">
          <div class="label" :class="{'expired': his.expired}">{{$t('details.editSaleFor')}} <span class="bfont">{{ $tools.singlePrice(his.content.buyerValue, his.content.sellValue,his.payToken) }}
              {{his.content.paytokenSymbol}}</span></div>
        </template>
        <template v-else-if="his.type == 3">
          <div class="label" :class="{'expired': his.expired}">{{$t('details.cancelSaleFor')}} <span class="bfont">{{ $tools.singlePrice(his.content.buyerValue, his.content.sellValue,his.payToken) }}
              {{his.content.paytokenSymbol}}</span></div>
        </template>
        <template v-else-if="his.type == 4">
          <div class="label" :class="{'expired': his.expired}">{{$t('details.boughtFor')}}<span class="bfont">{{$tools.singlePrice(his.content.buyerValue, his.content.sellValue,his.payToken)}}
              {{his.content.paytokenSymbol}}</span></div>
        </template>
        <template v-else-if="his.type == 5">
          <div class="label" :class="{'expired': his.expired}">{{$t('details.offered')}}<span class="bfont">{{ $tools.singlePrice(his.content.sellValue, his.content.buyerValue,his.payToken) }}
              {{his.content.paytokenSymbol}}</span> {{$t('details.for')}} {{his.content.buyerValue}} {{$t('details.editions')}}</div>
        </template>
        <template v-else-if="his.type == 6">
          <div class="label" :class="{'expired': his.expired}">{{$t('details.editOffered')}} <span class="bfont">{{$tools.singlePrice(his.content.sellValue, his.content.buyerValue,his.payToken)}}
              {{his.content.paytokenSymbol}}</span> {{$t('details.for')}} {{his.content.buyerValue}} {{$t('details.editions')}}</div>
        </template>
        <template v-else-if="his.type == 7">
          <div class="label" :class="{'expired': his.expired}"> {{$t('details.cancelOffered')}} <span class="bfont">{{ $tools.singlePrice(his.content.sellValue, his.content.buyerValue,his.payToken) }}
              {{his.content.paytokenSymbol}}</span> {{$t('details.for')}} {{his.content.buyerValue}} {{$t('details.editions')}}</div>
        </template>
        <template v-else-if="his.type == 8">
          <div class="label" :class="{'expired': his.expired}">{{$t('details.accept')}} <span class="bfont">{{$tools.singlePrice(his.content.sellValue, his.content.buyerValue,his.payToken)}}
              {{his.content.paytokenSymbol}}</span> {{$t('details.for')}} {{ his.content.sells }} {{$t('details.editions')}}</div>
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
        <div @click="goAccount(his.from)" class="name" v-if="his.type != 4 && his.type != 8">
          <span class="text">
            <span class="c9">{{$t('details.by')}}</span>
            {{his?.fromUser?.nickname || $filters.ellipsisAddress(his.from) }}
            <span v-if="his.txHash" @click.stop="toTx(his.txHash)" class="tx-link iconfont icon-link"></span>
          </span>
          <span class="t">{{ $filters.timeFormat(his.createTime) }}</span>
        </div>
        <div @click="goAccount(his.to)" class="name" v-else>
          <span class="text">
            <span class="c9">{{$t('details.by')}}</span>
            {{his?.toUser?.nickname || $filters.ellipsisAddress(his.to) }}
            <span v-if="his.txHash" @click.stop="toTx(his.txHash)" class="tx-link iconfont icon-link"></span>
          </span>
          <span class="t">{{ $filters.timeFormat(his.createTime) }}</span>
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
    data () {
      return {}
    },
    methods: {
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
      toTx (tx) {
        var explorer = this.$store.state.config.network.explorer;
        if (!explorer) return;
        var url = explorer + "/tx/" + tx;
        window.open(url);
      }
    }
  }
</script>

<style lang="scss" scoped>
  .tx-link {
    font-weight: bold;
    font-size: 18px;
    color: #666;
    cursor: pointer;
  }
</style>
