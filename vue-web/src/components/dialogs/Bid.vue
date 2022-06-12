<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" @close="$emit('close')" @open="onOpen" @closed="closed" custom-class="fg-dialog" destroy-on-close>
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">
          <span v-if="uri">{{ uri.name }}</span>
        </div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png ">
        </div>
      </div>
    </template>
    <div class="fg-dialog-body" v-if="!confirm">
      <div class="all-error" v-if="formError.all"> {{ formError.all }} </div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.pab')}}</span>
          </div>
        </div>
        <div class="input-group">
          <div class="input-info">
            <div class="tip"><span class="text">{{$t('dialog.bidPrice')}}</span></div>
            <el-input class="fg-dialog-input" placeholder="0.0" v-if="!bid" type="number" v-model="form.price">
              <template #suffix>
                <el-select class="paytoken-selects" v-model="form.payAddress">
                  <el-option v-for="(token, i) in payTokens" :key="i" :label="token.symbol" :value="token.address">
                  </el-option>
                </el-select>
              </template>
            </el-input>
            <el-input class="fg-dialog-input" placeholder="0.0" type="number" v-else v-model="form.price">
              <template #suffix v-if="bid.payToken">
                <div class="paytoken-suffix">{{ bid.paytokenSymbol }}</div>
              </template>
            </el-input>
            <div class="input-error" v-if="formError.price">{{formError.price}}</div>
            <div class="stip">
              <div class="stip-item"> {{ $t('dialog.yourBalance')}} <span class="bfont">{{ pay.balance }}{{ payToken?.symbol }}</span> </div>
              <div class="stip-item"> {{ $t('dialog.serviceFee')}} <span class="bfont">{{serviceFee}}%</span> </div>
              <div class="stip-item">{{ $t('dialog.pay')}} <span class="bfont"> {{pay.pay}}{{ payToken?.symbol}}</span></div>

            </div>

          </div>

        </div>

        <div class="process-btn">
          <template v-if="pay && parseFloat(pay.pay) > parseFloat(pay.balance)">
            <el-button @click="onSubmit" disabled v-if="!bid" type="primary" :loading="confirm">
              {{$t('dialog.confirm')}}</el-button>
            <el-button @click="onSubmit" disabled v-else type="primary" :loading="confirm">
              {{$t('dialog.change')}}</el-button>

            <div class="input-error text-center">{{$t('dialog.noBalance')}}</div>
          </template>
          <template v-else>
            <el-button @click="onSubmit" v-if="!bid" type="primary" :loading="confirm">
              {{$t('dialog.confirm')}}</el-button>
            <el-button @click="onSubmit" v-else type="primary" :loading="confirm">
              {{$t('dialog.change')}}</el-button>
          </template>
        </div>

      </div>
    </div>

    <div class="fg-dialog-body" v-else>
      <div class="all-error" v-if="error.all"> {{ error.all }} </div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.approve')}} {{ payToken?.symbol }}</span>
          </div>
          <span v-if="step.approve != 1" :class="step.approve== 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>

        </div>
        <div class="process-btn">
          <el-button @click="onApprove" type="primary" v-if="step.approve == 0">
            {{$t('dialog.approve')}}</el-button>
          <el-button disabled type="info" v-else-if="step.approve == 1">
            {{$t('dialog.inProgress')}}...</el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.approve">{{ error.approve }}</div>
      </div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.signsBid')}}</span>
          </div>
          <span v-if="step.bid != 1" :class="step.bid == 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="step.approve != 2">{{$t('dialog.bid')}}
          </el-button>
          <el-button @click="onBid" type="primary" v-else-if="step.bid==0">{{$t('dialog.bid')}}
          </el-button>
          <el-button disabled type="info" v-else-if="step.bid == 1">{{$t('dialog.inProgress')}}...
          </el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.bid">{{ error.bid}}</div>
      </div>

    </div>

  </el-dialog>
</template>

<script>
  import BigNumber from 'bignumber.js'
  export default {
    name: "Bid",
    data: function () {
      let paytoken = this.$store.getters.defaultBidPayToken();
      return {
        visible: this.show,
        confirm: false,
        form: {
          price: "",
          payAddress: paytoken ? paytoken.address : "",
        },
        formError: {
          all: "",
          price: "",
        },
        step: {
          approve: 0,
          bid: 0,
        },
        error: {
          all: "",
          approve: "",
          bid: "",
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
      bid: {
        type: Object,
        default: null,
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
      payTokens () {
        var payTokens = this.$store.state.payTokens;
        let tokens = [];
        for (var i = 0;i < payTokens.length;i++) {
          let payToken = payTokens[i];
          if (payToken.address.toLocaleLowerCase() == this.$sdk.NULL_ADDRESS().toLocaleLowerCase()) continue;
          tokens.push(payToken);
        }
        return tokens;
      },
      serviceFee () {
        let fee = this.$tools.decimal(this.$store.state.config.buyerFee / 100, 2);
        return fee;
      },
      payToken () {
        let payToken = this.$store.getters.payToken(
          this.form.payAddress
        );
        return payToken;
      },
      pay () {
        let balance = this.$store.getters.getBalance(this.form.payAddress)
        let price = this.$tools.str2num(this.form.price)
        if (!price) return { balance: balance || 0, pay: 0 }
        let pay = this.$tools.decimal(price + (price * this.serviceFee / 100), 6)
        return { pay, balance }
      }
    },
    methods: {
      onOpen () {
        if (this.bid) {
          let paytoken = this.$store.getters.payToken(this.bid.sellToken)
          this.form.payAddress = paytoken?.address.toLocaleLowerCase();
          this.form.price = this.$tools.singlePrice(this.bid.sellValue, this.bid.buyerValue, this.bid.payToken);
        }
      },
      checkForm () {
        let error = false;
        if (!parseFloat(this.form.price)) {
          error = true;
          this.formError.price = this.$t("form.noPrice");
        }
        if (this.bid && (parseFloat(this.form.price) <= parseFloat(
          this.$tools.singlePrice(this.bid.sellValue, this.bid.buyerValue, this.bid.payToken)
        ))) {
          error = true
          this.formError.price = this.$t("form.lessPrice")
        }

        return !error;
      },
      onSubmit () {
        if (!this.checkForm()) return;
        this.confirm = true;
        let that = this;
        setTimeout(async function () {
          await that.onApprove();
        }, 100);
      },
      async onApprove () {
        this.step.approve = 1;
        if (!this.bid) {
          let payToken = this.$store.getters.payToken(this.form.payAddress);
          this.order = {
            address: this.nft.address,
            tokenId: this.nft.tokenId,
            payToken: payToken,
            salt: "0",
          };
        } else {
          let result = await this.getOrder();
          if (result.error) {
            this.error.all = result.error;
            this.step.approve = 0;
            return;
          }
        }
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
          address: this.order.payToken.address,
          type: this.order.payToken.type,
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
      async getOrder () {
        return new Promise((resolve, reject) => {
          var data = {
            caddress: this.nft.address,
            tokenId: this.nft.tokenId,
            owner: this.bid.address,
            type: this.$sdk.valueOrderType("BID"),
          };
          let that = this;
          this.$api("order.get", data).then((res) => {
            if (that.$tools.checkResponse(res)) {
              that.order = {
                address: res.data.buyerToken,
                tokenId: res.data.buyerTokenId,
                type: res.data.buyerType,
                payToken: {
                  address: res.data.sellToken,
                  name: res.data.paytokenName,
                  symbol: res.data.paytokenSymbol,
                  decimals: res.data.paytokenDecimals,
                  type: res.data.sellType,
                },
                salt: res.data.salt,
              };
              resolve(res.data);
            } else {
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      async onBid () {
        if (this.step.approve != 2) return;
        this.step.bid = 1;
        let order = {
          ...this.order,
          price: this.form.price,
          sellerFee: this.config.sellerFee,
        };
        let result = await this.bidToken(order);
        if (result.error) {
          this.error.bid = result.error;
          this.step.bid = 0;
        } else {
          this.error.bid = "";
          this.step.bid = 2;
          this.$emit("confirm");
        }
      },
      async bidToken (order) {
        return new Promise((resolve, reject) => {
          let sellValue = new BigNumber(this.form.price);
          sellValue = sellValue.multipliedBy(
            new BigNumber(10).exponentiatedBy(order.payToken.decimals)).toFixed();
          var data = {
            owner: this.user.coinbase,
            sellToken: order.payToken.address,
            sellTokenId: "0",
            sellValue: sellValue,
            sellType: order.payToken.type,
            buyToken: order.address,
            buyTokenId: order.tokenId,
            buyValue: 1,
            buyType: 3,
            salt: order.salt,
            type: this.$sdk.valueCommonType("BID")
          };
          let that = this;
          this.$api("order.prepare", data).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              let message = res.data.message;
              if (message.startsWith("0x")) {
                message = message.slice(2, message.length);
              }
              let salt = res.data.salt;
              let signature = "";
              try {
                signature = await that.$web3.sign(message, that.user.coinbase);
                if (signature.error) {
                  return resolve(signature);
                }
              } catch (e) {
                return resolve({ error: e.message });
              }
              var _data = {
                type: !that.bid
                  ? that.$sdk.valueCommonType("BID")
                  : that.$sdk.valueCommonType("EDIT_BID"),
                owner: data.owner,
                sellToken: data.sellToken,
                sellTokenId: data.sellTokenId,
                sellValue: data.sellValue,
                sellType: data.sellType,
                buyToken: data.buyToken,
                buyTokenId: data.buyTokenId,
                buyValue: data.buyValue,
                buyType: data.buyType,
                salt: salt,
                message: message,
                signature: signature,
              };
              that.$api("order.add", _data).then(function (res) {
                if (that.$tools.checkResponse(res)) {
                  that.$tools.message(that.$t('request.bidSuccess'), "success");
                  resolve(res);
                } else {
                  resolve({ error: res.errmsg });
                }
              });
            } else {
              that.$tools.message(res.errmsg);
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      closed () {
        let paytoken = this.$store.getters.defaultBidPayToken();
        this.confirm = false;
        this.form = {
          price: "",
          payAddress: paytoken ? paytoken.address : "",
        };
        this.formError = {
          all: "",
          price: "",
        };
        this.step = {
          approve: 0,
          bid: 0,
        };
        this.error = {
          all: "",
          approve: "",
          bid: "",
        };
        this.order = {};
      },
    },
  };
</script>
<style lang="scss" scoped>
</style>

