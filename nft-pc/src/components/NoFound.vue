<template>
  <div class="tab-info">
        <div class="tab-title">{{$t('items.nif')}}</div>
        <div class="tab-intro">{{$t('items.text1')}}</div>
        <el-button class="tab-btn" @click="goHome">{{$t('items.browse')}}</el-button>
  </div>
</template>
<script>
export default {
  methods:{
    goHome(){
      this.$router.push("/");
    }
  }
}
</script>
<style lang="scss" scoped>
  .tab-info {
  display:flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
      .tab-title {
        font-size: 24px;
        font-family: "Montserrat-Regular";
        font-weight: bold;
        color: #1d1e22;
        margin: 45px auto 23px;
      }
      .tab-intro {
        width: 313px;
        /* height: 38px; */
        font-size: 16px;
        font-family: "Montserrat-Regular";
        font-weight: 400;
        color: #1d1e22;
        line-height: 22px;
        opacity: 0.5;
      }
      .tab-btn {
        width: 250px;
        height: 54px;
        background: $primaryColor;
        border-radius: 8px;
        color: #ffffff;
        font-family: "Montserrat-Regular";
        margin-top: 21px;
      }
    }
</style>