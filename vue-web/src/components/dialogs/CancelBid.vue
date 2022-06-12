<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" @close="$emit('close')" @closed="closed" custom-class="fg-dialog" destroy-on-close>
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">
          <span v-if="uri">{{ uri.name }}</span>
        </div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png " />
        </div>
      </div>
    </template>
    <div class="fg-dialog-body" v-if="!confirm">
      <div class="all-error" v-if="error.all">{{ error.all }}</div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.cancelABid") }}</span>
          </div>
        </div>
        <div class="confirm-text">
          {{ $t("dialog.sureCancelBid") }}
        </div>
        <div class="button-group">
          <div class="process-btn">
            <el-button @click="$emit('close')">
              {{$t("dialog.cancel")}}
            </el-button>
          </div>
          <div class="process-btn">
            <el-button @click="onSubmit" type="primary" :loading="step == 1">
              {{$t("dialog.confirm")}}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="fg-dialog-body" v-else>
      <div class="all-error" v-if="error.all">{{ error.all }}</div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.cancelBid") }}</span>
          </div>
          <span v-if="step.cancel != 1" :class="step.cancel == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button @click="onCancel" v-if="step.cancel == 0">{{
            $t("dialog.cancel")
          }}</el-button>
          <el-button disabled type="info" v-else-if="step.cancel == 1">{{
            $t("dialog.inProgress")
          }}</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.cancel">{{ error.cancel }}</div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import BigNumber from "bignumber.js";
  export default {
    name: "CancelBid",
    data: function () {
      return {
        visible: this.show,
        confirm: false,
        formError: {
          all: "",
          cancel: "",
        },
        step: {
          cancel: 0,
        },
        error: {
          all: "",
          cancel: "",
        },
        order: {},
      };
    },
    emits: ['confirm'],
    props: {
      show: {
        type: Boolean,
        default: false,
      },
      nft: {
        type: Object,
        default: {},
      },
      bid: {
        type: Object,
        default: {},
      },
      uri: {
        type: Object,
        default: null,
      },
    },
    watch: {
      show (val) {
        this.visible = this.show;
      },
    },
    computed: {
      user () {
        return this.$store.state.user;
      },
      config () {
        return this.$store.state.config;
      },
    },
    methods: {
      async onSubmit () {
        this.confirm = true;
        let that = this;
        setTimeout(async function () {
          await that.onCancel();
        }, 100);
      },
      async onCancel () {
        this.step.cancel = 1;
        let order = await this.getOrder();
        if (order.error) {
          this.error.all = order.error;
          this.step.cancel = 0;
          return;
        }
        let result = await this.cancelBid();
        if (result.error) {
          this.error.cancel = result.error;
          this.step.cancel = 0;
        } else {
          this.error.cancel = "";
          this.step.cancel = 2;
          this.$emit("confirm");
        }
      },
      async cancelBid () {
        var order = {
          owner: this.order.owner,
          sellToken: this.order.sellToken,
          sellTokenId: this.order.sellTokenId,
          sellType: this.order.sellType,
          sellValue: this.order.sellValue,
          buyToken: this.order.buyerToken,
          buyTokenId: this.order.buyerTokenId,
          buyType: this.order.buyerType,
          buyValue: this.order.buyerValue,
          salt: this.order.salt,
          exchangeAddress: this.config.contract.nftExchangeAddress,
        };
        return await this.cancelOrder(order);
      },
      async cancelOrder (order) {
        let that = this;
        return new Promise(async function (resolve, reject) {
          let result = await that.$sdk.cancelOrder(that.user.coinbase, order);
          resolve(result);
        });
      },
      async getOrder () {
        return new Promise((resolve, reject) => {
          var data = {
            caddress: this.nft.address,
            tokenId: this.nft.tokenId,
            owner: this.bid.owner,
            type: this.$sdk.valueOrderType("BID"),
          };
          var that = this;
          this.$api("order.get", data).then((res) => {
            if (that.$tools.checkResponse(res)) {
              that.order = res.data;
              resolve(res.data);
            } else {
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      close () {
        this.$emit("close");
      },
      closed () {
        this.confirm = false;
        this.formError = {
          all: "",
          cancel: "",
        };
        this.step = {
          cancel: 0,
        };
        this.error = {
          all: "",
          cancel: "",
        };
        this.order = {};
      },
    },
  };
</script>
<style lang="scss" scoped>
</style>

