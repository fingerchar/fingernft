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
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.accept") }}</span>
          </div>
        </div>
        <div class="input-group">
          <div class="input-info">
            <div class="tip">
              <span class="text">{{ $t("dialog.bidPrice") }}</span>
            </div>
            <el-input class="fg-dialog-input" placeholder="0.0" :disabled="true" type="number" v-model="price">
              <template #suffix v-if="payToken">
                <div class="paytoken-suffix">{{ payToken.symbol }}</div>
              </template>
            </el-input>
          </div>
          <div class="stip" v-if="payToken">
            <div class="stip-item">
              {{ $t("dialog.serviceFee") }}
              <span class="bfont">{{ serviceFee }}%</span>
            </div>
            <div class="stip-item">
              {{ $t("dialog.receive") }}
              <span class="bfont"> {{ receive.receive }} {{ payToken.symbol }}</span>
            </div>
          </div>

        </div>

        <div class="button-group">
          <div class="process-btn">
            <el-button @click="$emit('close')">{{
              $t("dialog.cancel")
            }}</el-button>
          </div>
          <div class="process-btn">
            <el-button @click="onSubmit" type="primary" :loading="confirm">{{
              $t("dialog.accept")
            }}</el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="fg-dialog-body" v-else>
      <div class="all-error" v-if="error.all">{{ error.all }}</div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.approveCollection") }}</span>
          </div>
          <span v-if="step.approve != 1" :class="step.approve == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button @click="onApprove" type="primary" v-if="step.approve == 0">{{ $t("dialog.approve") }}</el-button>
          <el-button disabled type="info" v-else-if="step.approve == 1">{{
            $t("dialog.inProgress")
          }}</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.approve">
          {{ error.approve }}
        </div>
      </div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.signAccept") }}</span>
          </div>
          <span v-if="step.accept != 1" :class="step.accept == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="step.approve != 2">{{
            $t("dialog.accept")
          }}</el-button>
          <el-button @click="onAccept" type="primary" v-else-if="step.accept == 0">{{ $t("dialog.accept") }}</el-button>
          <el-button disabled type="info" v-else-if="step.accept == 1">{{
            $t("dialog.inProgress")
          }}</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.accept">{{ error.accept }}</div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import BigNumber from "bignumber.js";
  export default {
    data: function () {
      return {
        visible: this.show,
        confirm: false,
        step: {
          approve: 0,
          accept: 0,
        },
        error: {
          all: "",
          approve: 0,
          accept: "",
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
      price () {
        return this.$tools.singlePrice(this.bid.sellValue, this.bid.buyerValue, this.bid.payToken);
      },
      user () {
        return this.$store.state.user;
      },
      config () {
        return this.$store.state.config;
      },
      serviceFee () {
        let fee = this.$tools.decimal(this.$store.state.config.sellerFee / 100, 2);
        return fee;
      },
      payToken () {
        if (!this.bid.sellToken) return "";
        return this.bid.payToken;
      },
      receive () {
        let price = this.$tools.str2num(this.price);
        if (!price || !this.payToken) {
          return { receive: 0, usdt: 0 };
        }

        let receive = this.$tools.decimal(price - (price * this.serviceFee) / 100, 6);
        return { receive };
      },
    },
    methods: {
      onSubmit () {
        this.confirm = true;
        let that = this;
        setTimeout(async function () {
          await that.onApprove();
        }, 100);
      },
      async onApprove () {
        this.step.approve = 1;
        let order = await this.getOrder();
        if (order.error) {
          this.error.all = order.error;
          this.step.approve = 0;
          return;
        }
        let approved = await this.setApproveAll();
        if (approved.error) {
          this.error.approve = approved.error;
          this.step.approve = 0;
        } else {
          this.error.approve = "";
          this.step.approve = 2;
        }
      },
      async setApproveAll () {
        let order = {
          type: this.order.buyerType,
          address: this.order.buyerToken,
          tokenId: this.order.buyerTokenId,
        };
        let isApproved = await this.$sdk.isApprovedForAll(
          order,
          this.user.coinbase,
          this.config.contract.transferProxyAddress,
        );
        if (typeof isApproved == "object" && isApproved.error) {
          return isApproved;
        }
        if (isApproved) return true;
        let result = await this.$sdk.setApprovalForAll(
          order,
          this.user.coinbase,
          this.config.contract.transferProxyAddress,
          true
        );
        return result;
      },
      async onAccept () {
        this.step.accept = 1;
        let result = await this.acceptOrder();
        if (result.error) {
          this.error.accept = result.error;
          this.step.accept = 0;
        } else {
          this.error.accept = "";
          this.step.accept = 2;
          this.$emit("confirm");
        }
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
      async acceptOrder () {
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
          signature: this.order.signature,
          caddress: this.nft.address,
          tokenId: this.nft.tokenId,
          type: this.$sdk.valueOrderType("BID"),
          amount: this.order.sellValue,
          buyerFee: this.order.buyFee,
          sellerFee: this.order.sellFee,
          buyer: "0x0000000000000000000000000000000000000000",
          value: "0",
        };
        return await this.exchangeToken(order);
      },
      async exchangeToken (data) {
        return new Promise((resolve, reject) => {
          var that = this;
          this.$api("order.buyerFee", {
            buyToken: data.caddress,
            buyTokenId: data.tokenId,
            owner: data.owner,
            type: data.type,
            salt: data.salt,
          }).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              data.buyerFee = res.data.buyFee;
              data.buyerFeeSig = {
                r: res.data.r,
                s: res.data.s,
                v: res.data.v,
              };
              data.exchangeAddress = that.config.contract.nftExchangeAddress;
              let result = await that.$sdk.exchangeOrder(that.user.coinbase, data);
              if (result.error) {
                return resolve(result);
              }
              resolve(result);
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
        this.step = {
          approve: 0,
          accept: 0,
        };
        this.error = {
          all: "",
          approve: "",
          accept: "",
        };
        this.order = {};
      },
    },
  };
</script>
<style lang="scss" scoped>
</style>

