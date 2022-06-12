<template>
  <div class="tab-content-list">
    <div class="info-item-group">
      <div class="info-item" v-if="creator && creator.address">
        <div class="avatar">
          <avatar @click="goAccount(creator.address)" shape="circular" :imageUrl="$filters.fullImageUrl(creator.avatar)" :address="creator.address" :imgWidth="35" :imgHeight="35"></avatar>
        </div>
        <div class="info">
          <div class="label">{{$t('details.creator')}}</div>
          <div @click="goAccount(creator.address)" class="name" v-if="creator.nickname">{{ creator.nickname }}</div>
          <div @click="goAccount(creator.address)" class="name" v-else>{{ ellipsisAddress(creator.address) }}</div>
        </div>
      </div>
      <div class="info-item" v-if="info.contract && info.contract.address">
        <div class="avatar">
          <avatar @click="goContract(info.contract.address)" shape="circular" :imageUrl="$filters.fullImageUrl(info.contract.cover)" :address="info.contract.address" :imgWidth="35" :imgHeight="35"></avatar>
        </div>
        <div class="info">
          <div class="label">{{$t('details.collection')}}</div>
          <div @click="goContract(info.contract.address)" class="name" v-if="info.contract">{{info.contract.name || $filters.ellipsisAddress(info.contract.address) }}</div>
        </div>
      </div>
    </div>
    <div class="propertys">
      <div v-for="(item, index) in attributes" :key="index" class="property-item">
        <div class="lalbel">{{item?item.key:''}}</div>
        <div class="name">{{item?item.value:''}}</div>
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    name: "Info",
    props: {
      info: {
        type: Object,
        default: {},
      },
      creator: {
        type: Object,
        default: {},
      },
      attributes: {
        type: Array,
        default: {},
      }
    },
    data: function () {
      return {};
    },
    computed: {
      address () {
        if (!this.info.creator) {
          return ""
        }
        if (this.info.address) {
          return this.info.address.slice(0, 11) + "..." + this.info.address.slice(-4)
        }
      },
      properties () {
        if (this.info.properties) {
          return JSON.parse(this.info.properties);
        } else {
          return [];
        }
      },
    },
    methods: {
      ellipsisAddress (address) {
        return address.slice(0, 11) + "..." + address.slice(-4)
      },
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
      goContract (address) {
        this.$router.push({ path: "/collection/" + address });
      }
    },
  };
</script>

<style lang="scss" scoped>
  .info-item-group {
    display: flex;
    border-bottom: $border;
    .info-item {
      flex: 1;
      padding-bottom: 0;
      .info {
        border-bottom: none;
      }
    }
  }
</style>
