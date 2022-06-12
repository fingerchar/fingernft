<template>
  <div class="account-item" @click="goUser">
    <div class="inner">

      <div class="m-right-10 cpointer">
        <avatar :imageUrl="$filters.fullImageUrl(account.avatar)" :address="account.address"
          :imgWidth="88" :imgHeight="88">
        </avatar>
      </div>
      <div class="content">
        <div class="d">
          <div class="bfont c6 f15 cpointer" v-if="account.nickname">{{ account.nickname }}</div>
        </div>
        <div class="d">
          <div class="bfont c9 cpointer">{{ $filters.ellipsisAddress(account.address) }}</div>
        </div>
      </div>

    </div>
  </div>
</template>
<script>
export default {
  name: "AccountItem",
  data: function(){
    return {};
  },
  props: {
    account: {
      type: Object,
      default: {},
    },
  },
  methods:{
    goUser(){
      this.$router.push({ path: "/account/" + this.account.address });
    },
  },
}
</script>

<style lang="scss" scoped>
.account-item{
  margin-bottom: 10px;
  .inner{
    display: flex;
    flex-direction: row;
    padding: 10px;
    margin: 0 5px;
    background: #fff;
    border-radius: $borderRadius;
  }
  .content{
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: space-around;
  }

}

</style>
