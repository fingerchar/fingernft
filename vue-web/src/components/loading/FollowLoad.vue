<template>
<el-skeleton class="follow-item" v-if="loadStatus != 'over'"
  :loading="loadStatus == 'loading'" v-for="i in limit" :key="i" animated>
 <template #template>
   <div class="avatar">
      <el-skeleton-item class="avatar-inner" variant="h3"/>
   </div>
   <div class="dialog-u-info">
    <el-skeleton-item class="u u1" variant="h3"/>
    <el-skeleton-item class="u u2" variant="h3"/>
   </div>
  </template>
</el-skeleton>
<div class="cel-load-status" v-else>
  <div class="loading-over">{{$t('loadStatus.end')}}</div>
</div>

</template>
<script>
export default {
  name: "FollowLoad",
  props: {
    loadStatus: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      limit: this.$store.state.pageLimit,
    };
  },
  created(){
  },
  methods: {
  },
};
</script>

<style lang="scss" scoped>
.follow-item{
  display: flex;
  margin: 10px auto;
  align-items: center;
  .avatar{
    cursor: pointer;
  }
  .u{
    margin-bottom: 10px;
    &.u1{
      width: 40%;
    }
    &.u2{
      width: 60%;
    }
  }
  .avatar-inner{
    width: 60px;
    height: 60px;
  }
  .dialog-u-info{
    display: flex;
    flex: 1;
    flex-direction: column;
    padding-left: 10px;
    cursor: pointer;
  }
  .dialog-u-detail{
    display: flex;
    width: 160px;
    justify-content: space-between;
    .dialog-u-header{
        width: 58px;
        height: 58px;
        border-radius: 50%;
    }
  }
}

.cel-load-status{
  padding: 10px 0;
  width: 100%;
  .loading-over{
    text-align: center;
    padding: 30px 0;
    color: $grayColor;
  }
}


</style>
