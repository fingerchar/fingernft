<template>
<div class="tab-info">
    <div class="tab-title">{{$t('404.title')}}</div>
    <div class="tab-intro">{{$t('404.tip')}}</div>
    <el-button class="tab-btn" @click="goHome">{{$t('404.browse')}}</el-button>
 
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
    font-weight: bold;
    margin: 45px auto 23px;
  }
  .tab-intro {
    width: 313px;
    font-size: 16px;
    font-weight: 400;
    color: #999;
    line-height: 22px;
  }
  .tab-btn {
    padding: 20px 40px;
    background: $primaryColor;
    border-radius: 8px;
    color: #ffffff;
    margin-top: 21px;
  }
}

</style>
