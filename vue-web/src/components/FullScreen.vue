<template>
<div class="fullScreen-wrapper">
  <el-dialog  :model-value="visible" :fullscreen="true" :show-close="false" :close-on-click-modal="false" custom-class="fullScreen-class">
    <span @click="exitClick" class="iconfont icon-fullscreen-exit"></span>
    <nft-preview :preview="true" v-if="visible" :image="uri.image" :animation_url="uri.animation_url"></nft-preview>
    <div class="name">{{uri.name}}</div>
  </el-dialog>
</div>
</template>
<script>
import NftPreview from '@/components/NFTPreview'

export default {
  components: {
    NftPreview
  },
  data: function(){
    return {
      visible: this.isFull,
    }
  },
  watch:{
    isFull(val){
      this.visible = this.isFull;
    }
  },
  props:{
    isFull:{
      type: Boolean,
      default: false,
    },
    uri:{
      type:Object,
      default:{}
    },
  },
  methods:{
    exitClick(){
      this.$emit("exitClick")
    }
  }
}
</script>
<style lang="scss" scoped>
  .nft-image{
    max-width: 80%;
    height: auto;
    border-radius: $borderRadius;
    align-self: center;
  }
  @media only screen and (max-width:992px) {
    .nft-image{
      width:90%;
    }
    .iconfont.icon-fullscreen-exit{
    }
  }
</style>
<style lang="scss">
.fullScreen-wrapper{
  .el-overlay{
    z-index:10001!important
  }
}
.fullScreen-class{
  padding:0!important;
  height:100vh!important;
  border-radius:0!important;
  background: rgba(0,0,0,0)!important;
}
.fullScreen-class .el-dialog__header{
  display: none;
}
.fullScreen-class .el-dialog__body{
  height:100%!important;
  padding:60px 20px 20px !important;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  .name{
    position: absolute;
    left: 20px;
    bottom: 20px;
    align-self: center;
    color:white;
    margin-top:20px;
  }
}
.iconfont.icon-fullscreen-exit{
  position: absolute;
  right: 40px;
  top: 61px;
  cursor: pointer;
  font-size:24px;
  color:white;
  align-self: flex-end;
  border-radius: 50%;
  border:1px solid #FFF;
  padding:5px;
  z-index: 100;
}
</style>
