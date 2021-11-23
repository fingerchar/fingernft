<template>
<div class="cel-load-status">
  <div class="loading-status" v-if="loadStatus!='over'" v-loading="loadStatus == 'loading'"></div>
  <div class="loading-over" v-else>{{$t('loadStatus.end')}}</div>
</div>
</template>

<script>
export default{
  name: 'LoadStatus',
  props: {
    loadStatus: {
      type: String,
      default: "",
    }
  },
}
</script>

<style lang="scss" scoped>
.cel-load-status{
  padding: 10px 0;
  width: 100%;
  .loading-status{
    height: 60px;
  }
  .loading-over{
    text-align: center;
    padding: 30px 0;
    color: $grayColor;
  }
}
</style>
