<template>
  <div class="highest">
    <div class="title">{{ $t('nftDetail.highestBid')}}</div>
    <div class="highest-body">
      <div class="avatar cpointer">
        <avatar @click="goAccount(bid.user.address)" :imageUrl="$filters.fullImageUrl(bid.user.avatar)" :address="bid.user.address" :imgWidth="35" :imgHeight="35" shape="circular">
        </avatar>
      </div>
      <div class="info">
        <div class="label">
          <span class="c3 cpointer" @click="goAccount(bid.user.address)">
            {{ bid.user.nickname || $filters.ellipsisAddress(bid.user.address) }}
          </span>
        </div>

        <div class="name">
          <span class="text">
            <span class="cprimary m-right-5">
              {{ bid.singlePrice || "" }}
              {{ bid?.payToken?.symbol }}
            </span>
          </span>
        </div>
      </div>

      <div class="info-right" v-if="isOwner && bid.owner != user.coinbase">
        <el-button size="mini" @click="acceptBid" plain type="primary">
          {{ $t("nftDetail.accept") }}
        </el-button>
      </div>

    </div>
  </div>

</template>
<script>

  export default {
    name: "HighestBid",
    props: {
      bid: {
        type: Object,
        default: {},
      },
      asset: {
        type: Object,
        default: null,
      },
    },
    computed: {
      user () {
        let user = this.$store.state.user;
        return user;
      },
      connected(){
        return this.$store.state.connected;
      },
      isOwner(){
        if(!this.connected || !this.asset || this.user.coinbase != this.asset.itemOwner) return false;
        return true;
      },
    },
    methods: {
      goAccount (address) {
        this.$router.push({ path: "/account/" + address });
      },
      acceptBid () {
        this.$emit("accept", this.bid);
      },
    },
  }

</script>

<style lang="scss" scoped>
  .highest {
    flex: 1;
    &.left {
      border-right: $border;
      padding-right: 5px;
    }
    .title {
      display: flex;
      font-size: 14px;
      color: #aaa;
      margin-bottom: 5px;
      justify-content: space-between;
    }
    .op {
      background: $secondPrimaryColor;
      font-weight: normal;
      font-size: 13px;
      padding: 2px 5px;
      border-radius: $borderRadius;
      margin-left: 5px;
    }

    .highest-body {
      display: flex;
    }
    .info {
      margin-left: 5px;
      flex: 1;
    }
    .label {
      margin-bottom: 2px;
    }
  }
</style>


