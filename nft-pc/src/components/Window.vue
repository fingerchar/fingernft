<template>
  <div>
    <el-dialog v-if="controlWindow" v-model="controlWindow" :show-close="false" :close-on-click-modal="false"
      custom-class="reportWindowClass">
      <div class="reportWrapper">
        <div class="m-head">
          <div class="m-head-title">{{bigTitle}}</div>
          <span @click="closeFunction" class="iconfont icon-reeor"></span>
        </div>
        <div class="m-describe">
          {{describe}}
        </div>
        <div class="m-message">
          <div class="m-message-title">{{smallTitle}}</div>
          <el-input v-model="value" v-if="placeholder!=''" :placeholder='placeholder'></el-input>
        </div>
        <el-button type="primary" class="u-button">{{buttonText}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data:function(){
    return {
      value:""
    }
  },
  props: {
    controlWindow: {
      type: Boolean,
      default: false,
    },
    bigTitle: {
      type: String,
      default: "Why are you reporting",
    },
    describe: {
      type: String,
      default:
        "Describe why you think this item should be removed from marketplace",
    },
    smallTitle: {
      type: String,
      default: "Message",
    },
    placeholder: {
      type: String,
      default: "Tell us some details",
    },
    buttonText: {
      type: String,
      default: "Report",
    },
  },
  methods: {
    closeFunction() {
      this.$emit("closeFunction");
    },
  },
};
</script>
<style lang="scss">
.reportWrapper .m-head {
  display: flex;
  justify-content: space-between;
  span {
    cursor: pointer;
    align-self: flex-start;
    font-size: 36px;
  }
}
.reportWrapper .m-head-title {
  // width: 173px;
  font-size: 27px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #1d1e22;
}
.reportWrapper .m-describe {
  font-size: 11px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #333333;
  opacity: 0.6;
  margin-top: 29px;
}
.reportWrapper .m-message {
  margin-top: 33px;
  input {
    margin-top: 7px;
    width: 301px;
    height: 33px;
    background: #eeeeee;
    border-radius: 5px;
    padding-left: 10px;
    border: none;
  }
  input::placeholder {
    font-size: 11px;
    font-family: Montserrat-Regular;
    font-weight: 400;
    color: #333333;
    opacity: 0.5;
  }
}
.reportWrapper .m-message-title {
  font-size: 11px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #333333;
}
.reportWrapper .u-button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 313px;
  height: 36px;
  background: $primaryColor;
  border-radius: 5px;
  margin-top: 44px;
  font-size: 11px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #ffffff;
}
.reportWrapper .m-message input {
  width: 100%;
}
.reportWrapper .u-button {
  width: 100%;
}
</style>
