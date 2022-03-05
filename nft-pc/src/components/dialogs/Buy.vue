<template>
  <el-dialog
    :model-value="visible"
    :show-close="false"
    :close-on-click-modal="false"
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
          <img class="close-img" src="@/assets/create-img/pop_shut.png " />
        </div>
      </div>
    </template>
    <div class="cel-dialog-body" v-if="!confirm">
      <div class="all-error" v-if="formError.all">{{ formError.all }}</div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.buy") }}</span>
          </div>
        </div>
        <div class="input-group">
          <div class="input-info">
            <div class="tip">
              <span>{{ $t("dialog.salePrice") }}</span>
            </div>
            <el-input
              class="cel-dialog-input"
              placeholder="0.0"
              :disabled="true"
              type="number"
              v-model="price"
            >
              <template #suffix v-if="asset.payToken">
                <div class="paytoken-suffix">{{ asset.payToken.symbol }}</div>
              </template>
            </el-input>
          </div>
          <div class="stip" v-if="asset.payToken">
            <div class="stip-item">
              {{ $t("dialog.yourBalance") }}
              <span class="bfont"
                >{{ pay.balance }} {{ asset.payToken.symbol }}</span
              >
            </div>
            <div class="stip-item">
              {{ $t("dialog.serviceFee")
              }}<span class="bfont">{{ serviceFee }}%</span>
            </div>
            <div class="stip-item">
              {{ $t("dialog.pay") }}
              <span class="bfont">
                {{ pay.pay }} {{ asset.payToken.symbol }}</span
              >
              ${{ pay.usdt }}
            </div>
          </div>
        </div>
        <div class="button-group">
          <div class="process-btn">
            <el-button @click="$emit('close')"
              >{{ $t("dialog.cancel") }}
            </el-button>
          </div>
          <div class="process-btn">
            <template
              v-if="pay && parseFloat(pay.pay) > parseFloat(pay.balance)"
            >
              <el-button
                @click="onSubmit"
                disabled
                type="primary"
                :loading="confirm"
                >{{ $t("dialog.buy") }}
              </el-button>
              <div class="input-error center-font">
                {{ $t("dialog.noBalance") }}
              </div>
            </template>
            <template v-else>
              <el-button @click="onSubmit" type="primary" :loading="confirm"
                >{{ $t("dialog.buy") }}
              </el-button>
            </template>
          </div>
        </div>
      </div>
    </div>

    <div class="cel-dialog-body" v-else>
      <div class="all-error" v-if="error.all">{{ error.all }}</div>

      <div class="process" v-if="isERC20">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.approveToken") }}</span>
          </div>
          <span
            v-if="step.approve != 1"
            :class="step.approve == 2 ? 'finish' : ''"
            class="step iconfont icon-seleted"
          ></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button
            @click="onApprove"
            type="primary"
            v-if="step.approve == 0"
            >{{ $t("dialog.approve") }}</el-button
          >
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
            <span>{{ $t("dialog.stb") }}</span>
          </div>
          <span
            v-if="step.buy != 1"
            :class="step.buy == 2 ? 'finish' : ''"
            class="step iconfont icon-seleted"
          ></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>

        <div class="process-btn" v-if="!isERC20">
          <el-button @click="onBuy" type="primary" v-if="step.buy == 0">{{
            $t("dialog.buy")
          }}</el-button>
          <el-button disabled type="info" v-else-if="step.buy == 1">{{
            $t("dialog.inProgress")
          }}</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>

        <div class="process-btn" v-else>
          <el-button disabled type="info" v-if="step.approve != 2">{{
            $t("dialog.buy")
          }}</el-button>
          <el-button @click="onBuy" type="primary" v-else-if="step.buy == 0">{{
            $t("dialog.buy")
          }}</el-button>
          <el-button disabled type="info" v-else-if="step.buy == 1">{{
            $t("dialog.inProgress")
          }}</el-button>
          <el-button disabled type="info" v-else>{{
            $t("dialog.done")
          }}</el-button>
        </div>
        <div class="process-error" v-if="error.buy">{{ error.buy }}</div>
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
      formError: {
        all: "",
      },
      step: {
        approve: 0,
        buy: 0,
      },
      error: {
        all: "",
        approve: "",
        buy: "",
      },
      order: {},
    };
  },
  props: {
    show: {
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
  watch: {
    show(val) {
      this.visible = this.show;
    },
  },
  computed: {
    price() {
      return this.$tools.singlePrice(this.asset.price, this.asset.sellValue);
    },
    user() {
      return this.$store.state.user;
    },
    config() {
      return this.$store.state.config;
    },
    isERC20() {
      let payToken = this.asset.payToken;
      if (!payToken) return false;
      return payToken.erc20;
    },
    serviceFee() {
      let fee = this.$tools.decimal(this.$store.state.config.buyerFee / 100, 2);
      return fee;
    },
    pay() {
      let price = this.$tools.str2num(this.price);
      let payToken = this.asset.payToken;
      if (!price || !payToken) {
        return { pay: 0, usdt: 0, balance: 0 };
      }

      let balance = this.$store.getters.getBalance(payToken.address);
      balance = this.$tools.decimal(balance, 4);
      let pay = this.$tools.decimal(price + (price * this.serviceFee) / 100);
      let usdt = this.$tools.decimal(payToken.rate * pay);
      return { pay, usdt, balance };
    },
  },
  methods: {
    async onSubmit() {
      this.confirm = true;
      let that = this;
      setTimeout(async function () {
        if (that.isERC20) {
          await that.onApprove();
        } else {
          await that.onBuy();
        }
      }, 100);
    },
    async getOrder() {
      return new Promise((resolve, reject) => {
        var data = {
          caddress: this.nft.address,
          tokenId: this.nft.tokenId,
          owner: this.asset.address,
          type: this.$sdk.valueOrderType("SALE"),
        };
        var that = this;
        this.$api("order.get", data).then((res) => {
          if (that.$tools.checkResponse(res)) {
            that.order = res.data;
            that.order.payToken = that.$store.getters.payToken(
              res.data.buyerToken
            );
            resolve(res.data);
          } else {
            resolve({ error: res.errmsg });
          }
        });
      });
    },
    async onApprove() {
      this.step.approve = 1;
      let result = await this.approvePayToken();
      if (result.error) {
        this.error.approve = result.error;
        this.step.approve = 0;
      } else {
        this.error.approve = "";
        this.step.approve = 2;
      }
    },
    async approvePayToken() {
      let order = {
        address: this.asset.payToken.address,
        type: this.asset.payToken.type,
      };
      let isAllowance = await this.$sdk.allowancePayToken(
        order,
        this.user.coinbase,
        this.config.ERC20TransferProxy
      );
      if (isAllowance.error) {
        return isAllowance;
      }
      isAllowance = isAllowance.toString();
      if (isAllowance != "0" && isAllowance) {
        return true;
      }
      return await this.$sdk.approvePayToken(
        order,
        this.user.coinbase,
        this.config.ERC20TransferProxy
      );
    },
    async onBuy() {
      if (this.isERC20 && this.step.approve != 2) return;
      this.step.buy = 1;
      let order = await this.getOrder();
      if (order.error) {
        this.error.all = order.error;
        this.step.buy = 0;
        return;
      }
      let result = await this.buyOrder();
      if (result.error) {
        this.error.buy = result.error;
        this.step.buy = 0;
      } else {
        this.error.buy = "";
        this.step.buy = 2;
        this.$emit("confirm");
      }
    },
    async buyOrder() {
      let value = "0";
      if (this.$sdk.keyAssetType(this.order.buyerType) == "ETH") {
        let realValue = new BigNumber(this.order.buyerValue);
        realValue = realValue.dividedBy(new BigNumber(this.order.sellValue));

        let fee = realValue.multipliedBy(new BigNumber(this.config.sellerFee));
        fee = fee.dividedBy(new BigNumber(10000));
        value = realValue.plus(fee).toFixed();
      }
      let buyerValue = new BigNumber(this.order.buyerValue);
      buyerValue = buyerValue.multipliedBy(
        new BigNumber(10).exponentiatedBy(this.order.payToken.decimals)
      );
      var order = {
        owner: this.order.owner,
        sellToken: this.order.sellToken,
        sellTokenId: this.order.sellTokenId,
        sellType: this.order.sellType,
        sellValue: this.order.sellValue,

        buyToken: this.order.buyerToken,
        buyTokenId: this.order.buyerTokenId,
        buyType: this.order.buyerType,
        buyValue: buyerValue.toFixed(),
        salt: this.order.salt,
        signature: this.order.signature,
        caddress: this.nft.address,
        tokenId: this.nft.tokenId,
        type: this.$sdk.valueOrderType("SALE"),
        amount: 1,
        buyerFee: this.config.buyerFee,
        sellerFee: this.config.sellerFee,
        buyer: "0x0000000000000000000000000000000000000000",
        value: value,
      };
      return await this.exchangeToken(order);
    },
    async exchangeToken(data) {
      return new Promise((resolve, reject) => {
        var that = this;
        this.$api("order.buyerFee", {
          token: data.caddress,
          tokenId: data.tokenId,
          owner: data.owner,
          type: data.type,
          salt: data.salt,
        }).then(async function (res) {
          if (that.$tools.checkResponse(res)) {
            data.buyerFeeSig = {
              r: res.data.r,
              s: res.data.s,
              v: res.data.v,
            };
            data.exchangeAddress = that.config.NftExchange;
            let result = await that.$sdk.exchangeOrder(
              that.user.coinbase,
              data
            );
            if (result.error) {
              return resolve(result);
            }
            that.$store.dispatch("updatePayToken", that.asset.payToken);
            resolve(result);
          } else {
            resolve({ error: res.errmsg });
          }
        });
      });
    },
    closed() {
      this.confirm = false;
      this.formError = {
        all: "",
      };
      this.step = {
        approve: 0,
        buy: 0,
      };
      this.error = {
        all: "",
        approve: "",
        buy: "",
      };
      this.order = {};
    },
  },
};
</script>
<style lang="scss" scoped>
</style>

