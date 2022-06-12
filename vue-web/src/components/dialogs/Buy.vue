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
            <span>{{ $t("dialog.buy") }}</span>
          </div>
        </div>
        <div class="input-group">
          <div class="input-info">
            <div class="tip">
              <span>{{ $t("dialog.salePrice") }}</span>
            </div>

            <el-input class="fg-dialog-input" placeholder="0.0" :disabled="true" type="number" v-model="price">
              <template #suffix>
                <div class="paytoken-suffix">{{ asset.paytokenSymbol }}</div>
              </template>
            </el-input>

            <div class="stip">
              <div class="stip-item">
                {{ $t("dialog.yourBalance") }}
                <span class="bfont">
                  {{ pay.balance }} {{ asset.paytokenSymbol }}
                </span>
              </div>
              <div class="stip-item">
                {{ $t("dialog.serviceFee")
                }}<span class="bfont">{{ serviceFee }}%</span>
              </div>
              <div class="stip-item">
                {{ $t("dialog.pay") }}
                <span class="bfont">
                  {{ pay.pay }} {{ asset.paytokenSymbol }}</span>
              </div>
            </div>
          </div>

        </div>

        <div class="button-group">
          <div class="process-btn">
            <el-button @click="$emit('close')">{{ $t("dialog.cancel") }}
            </el-button>
          </div>
          <div class="process-btn">
            <template v-if="pay && parseFloat(pay.pay) > parseFloat(pay.balance)">
              <el-button @click="onSubmit" disabled type="primary" :loading="confirm">{{ $t("dialog.buy") }}
              </el-button>
              <div class="input-error text-center">
                {{ $t("dialog.noBalance") }}
              </div>
            </template>
            <template v-else>
              <el-button @click="onSubmit" type="primary" :loading="confirm">{{ $t("dialog.buy") }}
              </el-button>
            </template>
          </div>
        </div>
      </div>
    </div>

    <div class="fg-dialog-body" v-else>
      <div class="all-error" v-if="error.all">{{ error.all }}</div>

      <div class="process" v-if="isERC20">
        <div class="step-info">
          <div class="text">
            <span>{{ $t("dialog.approve") }} {{ asset.paytokenSymbol }}</span>
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
            <span>{{ $t("dialog.stb") }}</span>
          </div>
          <span v-if="step.buy != 1" :class="step.buy == 2 ? 'finish' : ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>

        <div class="process-btn" v-if="!isERC20">
          <el-button @click="onBuy" type="primary" v-if="step.buy == 0">
            {{ $t("dialog.buy") }}
          </el-button>
          <el-button disabled type="info" v-else-if="step.buy == 1">
            {{ $t("dialog.inProgress") }}
          </el-button>
          <el-button disabled type="info" v-else>
            {{ $t("dialog.done") }}
          </el-button>
        </div>

        <div class="process-btn" v-else>
          <el-button disabled type="info" v-if="step.approve != 2">
            {{ $t("dialog.buy") }}
          </el-button>
          <el-button @click="onBuy" type="primary" v-else-if="step.buy == 0">
            {{ $t("dialog.buy") }}
          </el-button>
          <el-button disabled type="info" v-else-if="step.buy == 1">
            {{ $t("dialog.inProgress") }}
          </el-button>
          <el-button disabled type="info" v-else>
            {{ $t("dialog.done") }}
          </el-button>
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
    emits: ['confirm', 'close'],
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
      show (val) {
        this.visible = this.show;
      },
    },
    computed: {
      price () {
        return this.$tools.noDecimalsValue(this.asset.buyerValue, this.asset.sellValue, this.asset.paytokenDecimals);
      },
      user () {
        return this.$store.state.user;
      },
      config () {
        return this.$store.state.config;
      },
      isERC20 () {
        if (this.asset.paytokenAddress == this.$sdk.NULL_ADDRESS()) return false;
        return true;
      },
      serviceFee () {
        let fee = this.$tools.decimal(this.$store.state.config.buyerFee / 100, 2);
        return fee;
      },
      pay () {
        let price = this.$tools.str2num(this.price);
        if (!price) {
          return { pay: 0, balance: 0 };
        }

        let balance = this.$store.getters.getBalance(this.asset.paytokenAddress);
        balance = this.$tools.decimal(balance, 4);
        let pay = this.$tools.decimal(price + (price * this.serviceFee) / 100);
        return { pay, balance };
      },
    },
    methods: {
      async onSubmit () {
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
      async getOrder () {
        return new Promise((resolve, reject) => {
          var data = {
            caddress: this.nft.address,
            tokenId: this.nft.tokenId,
            owner: this.asset.itemOwner || this.asset.owner,
            type: this.$sdk.valueOrderType("SALE"),
          };
          var that = this;
          this.$api("order.get", data).then((res) => {
            if (that.$tools.checkResponse(res)) {
              that.order = res.data;
              that.order.payToken = {
                address: res.data.paytokenAddress,
                symbol: res.data.paytokenSymbol,
                decimals: res.data.paytokenDecimals,
                name: res.data.paytokenName
              }
              resolve(res.data);
            } else {
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      async onApprove () {
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
      async approvePayToken () {
        let order = {
          address: this.asset.buyerToken,
          type: this.asset.buyerType,
        };
        let isAllowance = await this.$sdk.allowancePayToken(
          order,
          this.user.coinbase,
          this.config.contract.erc20TransferProxyAddress,
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
          this.config.contract.erc20TransferProxyAddress,
        );
      },
      async onBuy () {
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
      async buyOrder () {
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
          type: this.order.orderType,
          amount: 1,
          buyerFee: this.order.buyFee,
          sellerFee: this.order.sellFee,
          buyer: "0x0000000000000000000000000000000000000000",
        };
        return await this.exchangeToken(order);
      },
      async exchangeToken (data) {
        return new Promise((resolve, reject) => {
          var that = this;
          this.$api("order.buyerFee", {
            sellToken: data.caddress,
            sellTokenId: data.tokenId,
            owner: data.owner,
            type: data.type,
            salt: data.salt,
          }).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              data.buyerFee = res.data.buyFee;
              let value = "0";
              if (that.$sdk.keyAssetType(data.buyType) == "ETH") {
                let realValue = new BigNumber(data.buyValue).dividedBy(new BigNumber(10).exponentiatedBy(that.order.paytokenDecimals));
                realValue = realValue.dividedBy(new BigNumber(data.sellValue));

                let fee = realValue.multipliedBy(new BigNumber(data.buyerFee));
                fee = fee.dividedBy(new BigNumber(10000));
                value = realValue.plus(fee).toFixed();
              }
              data.value = value

              data.buyerFeeSig = {
                r: res.data.r,
                s: res.data.s,
                v: res.data.v,
              };
              data.exchangeAddress = that.config.contract.nftExchangeAddress;
              let result = await that.$sdk.exchangeOrder(
                that.user.coinbase,
                data
              );
              if (result.error) {
                return resolve(result);
              }
              var payToken = {
                address: that.asset.buyerToken,
                type: that.asset.buyerType,
                decimals: that.asset.paytokenDecimals
              }
              that.$store.dispatch("updatePayToken", payToken);
              resolve(result);
            } else {
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      closed () {
        this.confirm = false;
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

