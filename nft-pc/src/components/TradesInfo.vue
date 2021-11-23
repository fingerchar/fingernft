<template>
  <div class="trades-info">
    <div class="trades-inner flex align-center">
      <div class="cover cpointer" @click="goDetail">
        <el-image
          fit="contain"
          class="nft-cover"
          :src="$filters.fullImageUrl(info ? info.image : '')"
        ></el-image>
      </div>
      <div class="trades-content flex">
        <div class="title color-000 w100"><span>EXPERMENTAL LOGO (RUDE LABS)</span></div>
        <div class="info flex align-center w100 p15">
          <span class="info-title color-888">purchased by</span>
          <div class="info-detail flex align-center">
            <div class="userinfo flex align-center m10 cpointer">
              <avatar
                shape="circular"
                :imageUrl="$filters.fullImageUrl(info.avatar)"
                :address="info.owner"
                :imgWidth="30"
                :imgHeight="30"
              ></avatar>
              <span class="username color-000 ml-10">TRym</span>
            </div>
            <div class="divide color-888 m10">for</div>
            <div class="price color-349 m10">0.23 ETH</div>
            <div class="divide color-888 m10">from</div>
            <div class="userinfo flex align-center m10 cpointer">
              <avatar
                shape="circular"
                :imageUrl="$filters.fullImageUrl(info.avatar)"
                :address="info.owner"
                :imgWidth="30"
                :imgHeight="30"
              ></avatar>
              <span class="username color-000 ml-10">TRym</span>
            </div>
          </div>
        </div>
        <div class="info flex">
          <span class="info-title color-888">1 hours ago</span>
        </div>
      </div>
      <div class="icon-trade">
        <img src="@/assets/img/ETH.png" />
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "TradesInfo",
    props: {
      info: {
        type: Object,
        default: {
          image: 'https://ipfs.movemarket.org/ipfs/QmNNJQU4jGsdxPu8Mcwfyc5CtHf5GkESUaR8hRVLsAYhq5',
        }
      }
    },
    methods: {
      goDetail(){}
    }
  }
</script>

<style lang="scss" scoped>
.trades-info {
  width: 100%;
  overflow-x: auto;
}
.trades-info + .trades-info {
  margin-top: 40px;
}
.trades-inner {
  min-width: 800px;
  padding: 18px 30px;
  background-color: #fff;
}
.cover {
  width: 130px;
  min-width: 100px;
}
.trades-content {
  // display: flex;
  flex-wrap: wrap;
  font-size: 18px;
  margin-left: 20px;
}
.info-title {
  margin-right: 40px;
}
.color-000 {
  color: #000;
}
.color-888 {
  color: #888;
}
.color-349 {
  color: #349bed;
}
.align-center {
  align-items: center
}
.w100 {
  width: 100%;
}
.p15 {
  padding: 15px 0;
}
.m10 {
  margin: 0 10px;
}
.ml-10{
  margin-left: 10px;
}
.icon-trade{
  font-size: 0;
  margin-left: 30px;
  img{
    width: 30px;    
  }
}
</style>

