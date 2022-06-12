<template>
<el-skeleton class="notify-item" :class="{big: size=='big'}" animated
    v-if="loadStatus != 'over'" :loading="loadStatus == 'loading'"
    v-for="i in limit" :key="i"
  >
  <template #template>
    <div class="notify-inner">
      <div class="cover cpointer">
        <el-skeleton-item class="nft-cover"/>
      </div>
      <div class="notify-content">
        <div class="title">
          <el-skeleton-item class="skeleton-list1"/><br>
        </div>
        <div class="desc">
          <el-skeleton-item class="skeleton-list2"/><br>
        </div>
        <div class="time c9">
          <el-skeleton-item class="skeleton-list3"/>
        </div>
      </div>
    </div>
  </template>
</el-skeleton>
<div class="cel-load-status" v-else>
  <div class="loading-over">{{$t('loadStatus.end')}}</div>
</div>
</template>
<script>
export default {
  name: "NotifyLoad",
  props:{
    loadStatus: {
      type: String,
      default: "",
    },
    size: {
      type: String,
      default: "",
    },
  },
  data(){
    return {
      limit: this.$store.state.pageLimit,
    };
  },
}
</script>

<style lang="scss" scoped>
.notify-item{
  background: #fff;
  display: flex;
  flex: 1;
  margin-bottom: 10px;
  border-radius: $borderRadius;
  &.big{
    margin-bottom: 20px;
    .notify-inner{
      padding: 20px;
    }
    .nft-cover{
      width: 80px;
      height: 80px;
    }
    .title{
      font-size: 18px;
    }
  }
  .notify-inner{
    display: flex;
    padding: 10px;
  }
  .notify-content{
    flex: 1;
    display: flex;
    flex-direction: column;
    padding-left: 10px;
    justify-content: center;
  }
  .nft-cover{
    width: 65px;
    height: 65px;
    border-radius: $borderRadius;
  }
  .title{
    line-height: 27px;
    padding-bottom: 5px;
  }
  .time{
    height: 25px;
    font-size: 12px;
  }
  .desc{
    height: 23px;
    display: flex;
    flex-direction: row;
    align-items: center;
    padding-bottom: 5px;
    .op{
      display: flex;
      align-items: center;
    }
    .c9{
      padding-right: 5px;
    }
    .op-name{
      padding-left: 5px;
    }
  }

}
.skeleton-list1{
  width:80px;
}
.skeleton-list2{
  width:200px;
  padding:5px 0;
}
.skeleton-list3{
  width:150px;
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

