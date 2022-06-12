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
            <span>{{$t('dialog.putOnSale')}}</span>
          </div>
        </div>
        <div class="input-group">
          <div class="input-info">
            <div class="tip"><span class="text">{{ $t('dialog.salePrice')}}</span></div>
            <el-input class="fg-dialog-input" placeholder="0.0" type="number" v-if="!asset.onsell" v-model="form.price">
              <template #suffix>
                <el-select class="paytoken-selects" v-model="form.payAddress">
                  <el-option v-for="(token, i) in payTokens" :key="i" :label="token.symbol" :value="token.address">
                  </el-option>
                </el-select>
              </template>
            </el-input>
            <el-input class="fg-dialog-input" placeholder="0.0" type="number" v-else v-model="form.price">
              <template #suffix>
                <div class="paytoken-suffix">{{asset.payToken ? asset.payToken.symbol:''}}</div>
              </template>
            </el-input>
            <div class="input-error" v-if="formError.price">{{ formError.price}}</div>
            <div class="stip">
              <div class="stip-item"> {{ $t('dialog.serviceFee') }} <span class="bfont">{{serviceFee}}%</span> </div>
              <div class="stip-item">{{ $t('dialog.receive') }}
                <span class="bfont"> {{receive.receive}}{{ payToken ? payToken.symbol:'' }}</span>
              </div>
            </div>
          </div>

        </div>
        <div class="process-btn">
          <el-button @click="onSubmit" v-if="!asset.onsell" type="primary" :loading="confirm">{{ $t('dialog.confirm') }}</el-button>
          <el-button @click="onSubmit" v-else type="primary" :loading="confirm">{{ $t('dialog.change') }}</el-button>
        </div>
      </div>
    </div>
    <div class="fg-dialog-body" v-else>
      <div class="all-error" v-if="error.all"> {{ error.all }} </div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t('dialog.approveCollection') }}</span>
          </div>
          <span v-if="step.approve != 1" :class="step.approve== 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>

        </div>
        <div class="process-btn">
          <el-button @click="onApprove" type="primary" v-if="step.approve == 0">{{ $t('dialog.approve') }}</el-button>
          <el-button disabled type="info" v-else-if="step.approve == 1">{{ $t('dialog.inProgress') }}</el-button>
          <el-button disabled type="info" v-else>{{ $t('dialog.done') }}</el-button>
        </div>
        <div class="process-error" v-if="error.approve">{{ error.approve }}</div>
      </div>

      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{ $t('dialog.signsSale') }}</span>
          </div>
          <span v-if="step.sale != 1" :class="step.sale == 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="step.approve != 2">{{ $t('dialog.sale') }}</el-button>
          <el-button @click="onSale" type="primary" v-else-if="step.sale ==0">{{ $t('dialog.sale') }}</el-button>
          <el-button disabled type="info" v-else-if="step.sale == 1"> {{ $t('dialog.inProgress') }}</el-button>
          <el-button disabled type="info" v-else>{{ $t('dialog.done') }}</el-button>
        </div>
        <div class="process-error" v-if="error.sale">{{ error.sale }}</div>
      </div>

    </div>

  </el-dialog>
</template>

<script>
  import BigNumber from 'bignumber.js'
  export default {
    name: "Sale",
    data: function () {
      let paytoken = this.$store.getters.defaultSalePayToken();
      return {
        visible: this.show,
        confirm: false,
        form: {
          price: '',
          payAddress: paytoken ? paytoken.address : "",
        },
        formError: {
          all: "",
          price: "",
        },
        step: {
          approve: 0,
          sale: 0,
        },
        error: {
          all: "",
          approve: "",
          sale: "",
        },
        order: {},
      }
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
        default: null
      },
    },
    emits: ["confirm"],
    watch: {
      show (val) {
        this.visible = this.show;
      },
    },
    computed: {
      payToken () {
        let payToken = this.$store.getters.payToken(
          this.form.payAddress
        );
        return payToken;
      },
      user () {
        return this.$store.state.user;
      },
      config () {
        return this.$store.state.config;
      },
      payTokens () {
        return this.$store.state.payTokens;
      },
      serviceFee () {
        let fee = this.$tools.decimal(this.$store.state.config.sellerFee / 100, 2);
        return fee;
      },
      receive () {
        let price = this.$tools.str2num(this.form.price);
        if (!price) return { receive: 0 }
        let receive = this.$tools.decimal(price - (price * this.serviceFee / 100), 6);
        return { receive }
      },
    },
    methods: {
      onOpen () {
        if (!this.asset.onsell) {
          this.asset.payToken = this.$store.getters.payToken(
            this.form.payAddress
          );
        } else {
          this.form.payAddress = this.asset.paytokenAddress;
          this.form.price = this.$tools.singlePrice(this.asset.price, this.asset.sellValue, this.asset.payToken);
        }
      },
      checkForm () {
        var error = false;
        if (!parseFloat(this.form.price)) {
          error = true;
          this.formError.price = this.$t("form.noPrice");
        }
        if (this.asset.onsell && (parseFloat(this.form.price) >= parseFloat(
          this.$tools.singlePrice(this.asset.price, this.asset.sellValue, this.asset.payToken)
        ))) {
          error = true
          this.formError.price = this.$t("form.granterPrice");
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
      async getOrder () {
        return new Promise((resolve, reject) => {
          var data = {
            caddress: this.nft.address,
            tokenId: this.nft.tokenId,
            owner: this.asset.itemOwner,
            type: this.$sdk.valueOrderType("SALE"),
          }
          let that = this;
          this.$api("order.get", data).then(res => {
            if (that.$tools.checkResponse(res)) {
              that.order = {
                address: res.data.sellToken,
                tokenId: res.data.sellTokenId,
                type: res.data.sellType,
                payToken: {
                  address: res.data.buyerToken,
                  name: res.data.paytokenName,
                  symbol: res.data.paytokenSymbol,
                  decimals: res.data.paytokenDecimals,
                  type: res.data.buyerType,
                },
                salt: res.data.salt,
              }
              resolve(res.data)
            } else {
              resolve({ error: res.errmsg });
            }
          });
        });
      },
      async onApprove () {
        this.step.approve = 1;
        if (!this.asset.onsell) {
          let payToken = this.$store.getters.payToken(this.form.payAddress);
          this.order = {
            address: this.nft.address,
            tokenId: this.nft.tokenId,
            payToken: payToken,
            salt: '0',
          }
        } else {
          let result = await this.getOrder();
          if (result.error) {
            this.error.all = result.error;
            this.step.approve = 0;
            return;
          }
        }
        let approved = await this.setApproveAll();
        if (approved.error) {
          this.error.approve = approved.error;
          this.step.approve = 0;
        } else {
          this.error.approve = ""
          this.step.approve = 2;
        }
      },
      async setApproveAll () {
        let order = {
          type: this.order.type,
          address: this.order.address,
          tokenId: this.order.tokenId,
        }
        let isApproved = await this.$sdk.isApprovedForAll(
          order,
          this.user.coinbase,
          this.config.contract.transferProxyAddress,
        );
        if (typeof isApproved == 'object' && isApproved.error) {
          return isApproved;
        }
        if (isApproved) return true;
        let result = await this.$sdk.setApprovalForAll(
          order,
          this.user.coinbase,
          this.config.contract.transferProxyAddress,
          true);
        return result;
      },
      async onSale () {
        this.step.sale = 1;
        let order = {
          ...this.order,
          price: this.form.price,
          sellerFee: this.config.sellerFee
        }
        let result = await this.saleToken(order)
        if (result.error) {
          this.error.sale = result.error;
          this.step.sale = 0;
        } else {
          this.error.sale = "";
          this.step.sale = 2;
          this.$emit("confirm")
        }
      },
      async saleToken (order) {
        return new Promise((resolve, reject) => {
          let buyValue = new BigNumber(this.form.price);
          buyValue = buyValue.multipliedBy(
            new BigNumber(10).exponentiatedBy(order.payToken.decimals)).toFixed();
          var data = {
            owner: this.user.coinbase,
            sellToken: order.address,
            sellTokenId: order.tokenId,
            sellValue: 1,
            sellType: order.type,
            buyToken: order.payToken.address,
            buyTokenId: "0",
            buyValue: buyValue,
            buyType: order.payToken.type,
            salt: order.salt,
            type: this.$sdk.valueCommonType("SALE")
          }
          let that = this;
          this.$api("order.prepare", data).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              let message = res.data.message;
              if (message.startsWith('0x')) {
                message = message.slice(2, message.length)
              }
              let salt = res.data.salt;
              let signature = "";
              try {
                signature = await that.$web3.sign(message, that.user.coinbase);
                if (signature.error) {
                  return resolve(signature);
                }
              } catch (e) {
                return resolve({ error: e.message })
              }
              var _data = {
                type: !that.asset.onsell ? that.$sdk.valueCommonType("SALE") : that.$sdk.valueCommonType('EDIT_SALE'),
                owner: data.owner,
                sellToken: data.sellToken,
                sellTokenId: data.sellTokenId,
                sellValue: data.sellValue,
                sellType: data.sellType,
                buyToken: data.buyToken,
                buyTokenId: data.tokenId ? data.tokenId : 0,
                buyValue: data.buyValue,
                buyType: data.buyType,
                salt: salt,
                message: message,
                signature: signature,
              }
              that.$api("order.add", _data).then(function (res) {
                if (that.$tools.checkResponse(res)) {
                  that.$tools.message(that.$t('request.saleSuccess'), "success");
                  resolve(res);
                } else {
                  resolve({ error: res.errmsg });
                }
              });
            } else {
              that.$tools.message(res.errmsg);
              resolve({ error: res.errmsg })
            }
          });
        });
      },
      close () {
        this.$emit("close");
      },
      closed () {
        let paytoken = this.$store.getters.defaultSalePayToken();
        this.step = {
          approve: 0,
          sale: 0,
        }
        this.form = {
          price: '',
          payAddress: paytoken ? paytoken.address : "",
        }
        this.formError = {
          all: "",
          price: "",
        }
        this.error = {
          all: "",
          approve: "",
          sale: "",
        }
        this.confirm = false;
      },
    },
  }

</script>
<style lang="scss" scoped>
</style>

