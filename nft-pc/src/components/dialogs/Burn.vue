<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false"
    @close="$emit('close')"
    @closed="closed"
    custom-class="cel-dialog"
    destroy-on-close
  >
    <template #title>
      <div class="cel-dialog-title">
        <div class="left">
          <span v-if="uri">{{ uri.name }}</span>
        </div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src = "@/assets/create-img/pop_shut.png ">
        </div>
      </div>
    </template>
    <div class="cel-dialog-body" v-if="!confirm">
      <div class="all-error" v-if="formError.all"> {{ formError.all }} </div>
      <div class="process"  >
        <div class="step-info" >
          <div class="text" >
              <span>{{$t('dialog.burnToken')}}</span>
          </div>
        </div>
        <div class="confirm-text">
          {{$t('dialog.sureBurn')}}
        </div>

        <div class="button-group">
          <div class="process-btn">
          <el-button @click="$emit('close')">{{$t('dialog.cancel')}}</el-button>
        </div>
        <div class="process-btn">
          <el-button @click="onSubmit" type="primary" :loading="confirm">{{$t('dialog.burn')}}</el-button>
        </div>
        </div>
      </div>
    </div>

    <div class="cel-dialog-body" v-else>
      <div class="all-error" v-if="error.all"> {{ error.all }} </div>
      <div class="process">
        <div class="step-info">
          <div class="text" >
              <span>{{$t('dialog.burnToken')}}</span>
          </div>
          <span
            v-if="step.burn != 1"
            :class="step.burn == 2 ? 'finish': ''"
            class="step iconfont icon-seleted"
          ></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>

        </div>
        <div class="process-btn">
          <el-button @click="onSubmit" type="primary" v-if="step.burn== 0">{{$t('dialog.burn')}}</el-button>
          <el-button disabled type="info" v-else-if="step.burn== 1">{{$t('dialog.inProgress')}}</el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.burn">{{ error.burn}}</div>
      </div>
    </div>

  </el-dialog>
</template>

<script>
export default {
  data: function(){
    return {
      visible: this.show,
      confirm: false,
      formError: {
        all: "",
      },
      step:{
        burn: 0,
      },
      error:{
        all: "",
        burn: "",
      },
    }
  },
  props: {
    show:{
      type: Boolean,
      default: false,
    },
    nft: {
      type: Object,
      default: {},
    },
    asset: {
      type: Object,
      default: {},
    },
    uri: {
      type: Object,
      default: null,
    },
  },
  watch:{
    show(val){
      this.visible = this.show;
    },
  },
  computed: {
    user(){
      return this.$store.state.user;
    },
  },
  methods: {
    onSubmit(){
      this.confirm = true;
      let that = this;
      setTimeout(async function(){
        await that.onBurn()
      }, 100);
    },
    async onBurn(){
      this.step.burn = 1
      let asset = {
        address: this.nft.address,
        type: this.nft.type,
        tokenId: this.nft.tokenId,
        owner: this.user.coinbase,
        quantity: 1,
      }
      let result = await this.burnToken(asset);
      if(result.error){
        this.error.burn = result.error;
        this.step.burn = 0;
      }else{
        this.error.burn = ""
        this.step.burn = 2;
        this.$emit("confirm");
      }
    },
    async burnToken(asset){
      if(asset.type==3){
        return await this.$sdk.burnAsset(asset)
      }else if(asset.type==2){
        return await this.$sdk.burnAsset1155(asset)
      }
    },
    close(){
      this.$emit("close");
    },
    closed(){
      this.confirm = false;
      this.formError = {
        all: "",
      }

      this.step = {
        burn: 0,
      }
      this.error = {
        all: "",
        burn: ""
      }
    },
  },
}

</script>
<style lang="scss" scoped>
</style>

