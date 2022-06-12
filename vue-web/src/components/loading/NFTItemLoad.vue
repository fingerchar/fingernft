<template>
<el-skeleton class="nft-item" :class="size" v-if="loadStatus != 'over'"
  :loading="loadStatus == 'loading'" v-for="i in limit" :key="i" animated>
 <template #template>
   <div class="inner">
      <div class="cover-padding">
      </div>
      <div class="descript">
        <div class="d">
          <el-skeleton-item variant="h3"/>
        </div>
        <div class="d">
          <el-skeleton-item variant="h3"/>
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
  name: "NFTItemLoad",
  props: {
    loadStatus: {
      type: String,
      default: "",
    },
    size: {
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
.loading-over{
  text-align: center;
  padding: 30px 0;
  color: $grayColor;
}

.nft-item {
  width: 25%;
  position: relative;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  margin-bottom: 20px;
  padding: 0 10px;
  &.big{
    width: 33.33%
  }
  .inner {
    background: #fff;
    position: relative;
    border-radius: $borderRadius;
    overflow: hidden;
    --coverWidth:100%;
  }
  .like {
    position: absolute;
    z-index: 1;
    right: 5px;
    top: 5px;
    .iconfont {
      cursor: pointer;
      margin-right: 5px;
      margin-top: 6px;
      font-size: 18px;
      border-radius: 18px;
      color: #aaa;
      &.active {
        color: $redColor;
      }
    }
  }
  .cover-padding{
    position: relative;
    padding-bottom: calc(var(--coverWidth) / 1.33);
  }
  .cover {
    position: absolute;
    top: 10px;
    left: 10px;
    right: 10px;
    bottom: 10px;
    width: 100%;
    height: 100%;
    cursor: pointer;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    iconfont{
    }
  }
  .collection-btn{
    width: 23px;
    height: 23px;
    text-align: center;
    line-height: 23px;
    position: absolute;
    right: 0;
    top: 0;
    background: #fff;
    border-bottom-left-radius: 5px;
    font-size: 16px;
    color: #666;
    &.active{
      color: #ffd200;
    }
  }
  .cover-image {
  }
  .avatar {
    margin-right: 3px;
  }
  .descript {
    padding: 5px 0;
  }
  .d {
    line-height: 28px;
    padding: 5px;
    display: flex;
  }
  .d-left {
    display: flex;
    flex: 1;
    align-items: center;
  }
  .d-right {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: flex-end;
  }
  .bid {
    white-space: nowrap;
    color: $primaryColor;
    cursor: pointer;
    font-weight: 400;
    font-size: 12px;
  }
  .price {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    font-size: 12px;
    font-family: Montserrat-Regular;
    font-weight: 400;
    color: #1d1e22;
    margin-right: 5px;
  }
  .nosale {
    text-overflow: ellipsis;
    font-size: 12px;
    font-weight: 400;
    color: $grayColor;
  }
  .stock {
    display: flex;

    flex: 1;
    margin-left: 4px;
    margin-right: 6px;
    white-space: nowrap;
    font-size: 12px;
    font-weight: 400;
    color: #999;
    text-align: center;
  }
}
.bfont {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.d-top {
  width: 50%;
  flex: 0 !important;
}

.more-menu-popover {
  .menu {
    cursor: pointer;
    padding: 8px 7px;
    font-size: 13px;
    font-weight: normal;
    border-radius: $borderRadius;
    &:hover,
    &:active {
      background: #f0f0f0;
    }
  }
}

.sale-bid {
  display: flex;
  flex-direction: column;
  button {
    border: none;
    background: none;
    font-size: 14px;
    font-family: Montserrat-Regular;
    font-weight: 500;
    color: #1d1e22;
    line-height: 26px;
    cursor: pointer;
    &:hover {
      color: red;
      border-color: inherit;
      background-color: inherit;
    }
    &:focus {
      color: red;
      border-color: inherit;
      background-color: inherit;
    }
  }
}

.skeleton-image{
  width: 100%; height: 218px;
  display:flex;
  justify-content: center;
  align-items: center;
}
.skeleton-text{
  margin-top:10px;height:56px;
  .row1{
    width: 50%;
  }
  .row2{
    display: flex; align-items: center; justify-items: space-between; margin-top: 8px; height: 16px;
    .text1{
      margin-right: 16px;
    }
    .text2{
      width: 30%;
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
