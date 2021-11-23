<template>
  <div class="main-wrapper">
    <div class="left-section-wrapper">
      <el-image class="left-bg" :src="require('@/assets/img/home/connect.jpg')"></el-image>
    </div>
    <div class="connect-wrap">
      <div class="connect-section">
        <div class="flex-align-center">
          <div class="go-back" @click="goBack">
            <span class="iconfont icon-leftarrow"></span>
            <span class="text">{{$t('connect.goBack')}}</span>
          </div>
        </div>
        <div class="fontStyle1">{{$t('connect.text1')}}</div>
        <div class="fontStyle2">
          <div>{{$t('connect.text2')}}</div>
          <div>{{$t('connect.text3')}}</div>
        </div>

        <div class="wallet-item" @click="login('metamask')">
          <img src="@/assets/img/metamask.png" width="20" />
          <div class="text">{{$t('connect.text4')}}</div>
        </div>

        <div class="lastRow">
          {{$t('connect.text5')}}
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data: function () {
    return {};
  },
  computed: {
    config() {
      return this.$store.state.config;
    },
  },
  methods: {
    async login(value) {
      this.$store.dispatch("connectAndSign", value).then(res=>{
        if(res && this.$tools.checkResponse(res)){
          var query = this.$route.query;
          if (query && query.redirect) {
            this.$router.push(query.redirect);
          } else {
            this.$router.push("/");
          }
        }
      });
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>
<style lang="scss" scoped>
.wallet-item {
  cursor: pointer;
  width: 333px;
  height: 37px;
  background: #fff;
  border-radius: 7px;
  display: flex;
  align-items: center;
  margin-bottom: 35px;
  justify-content: center;
  position: relative;
  img {
    position: absolute;
    left: 20px;
  }
  .text {
    font-size: 9px;
    font-weight: 400;
    color: #333;
  }
}

.main-wrapper {
  display: flex;
}
.left-section-wrapper {
  width: 520px;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  background: url("../../assets/img/home/connect.jpg")
    no-repeat 0 0 / 100% 100%;
}
.left-logo {
  margin-top: 14px;
  margin-left: 15px;
}

.connect-wrap {
  flex: 1;
  display: flex;
  width: 100%;
  .connect-section{
    padding: 100px 0 200px 580px;
  }
}

.go-back {
  cursor: pointer;
  display: flex;
  align-items: center;
  font-weight: 400;
  .iconfont {
    font-weight: bold;
  }
  .text {
    margin-left: 5px;
  }
}

.leftSection_bottom {
  position: absolute;
  bottom: 0;
}
.fontStyle1 {
  font-size: 18px;
  font-weight: bold;
  color: #000000;
  margin-top: 23px;
}
.fontStyle2 {
  margin-top: 14px;
  margin-bottom: 35px;
  font-size: 9px;
  font-weight: 400;
  color: #999;
}

.lastRow {
  font-size: 9px;
  font-weight: 400;
  color: #999;
}
</style>
