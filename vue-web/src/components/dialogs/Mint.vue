<template>
  <el-dialog :model-value="visible" :show-close="false" :close-on-click-modal="false" :modal="false" @close="$emit('close')" @closed="closed" custom-class="fg-dialog" destroy-on-close>
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">
          <span>{{$t('dialog.followSteps')}}</span>
        </div>
        <div class="right" @click="$emit('close')">
          <img class="close-img" src="@/assets/create-img/pop_shut.png ">
        </div>
      </div>
    </template>
    <div class="fg-dialog-body">
      <div class="all-error" v-if="error.all"> {{ error.all }} </div>
      <div class="process" v-if="nft.form && nft.form.onSale">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.approveCollection')}}</span>
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
            {{$t('dialog.inProgress')}}</el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.approve">{{ error.approve }}</div>
      </div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.fileUpload')}}</span>
          </div>
          <span v-if="step.fileUp != 1" :class="step.fileUp== 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="this.nft.form.onSale && step.approve != 2">{{$t('dialog.upload')}}
          </el-button>
          <el-button @click="fileUp" type="primary" v-else-if="step.fileUp ==0">{{$t('dialog.upload')}}
          </el-button>
          <el-button disabled type="info" v-else-if="step.fileUp == 1">{{$t('dialog.inProgress')}}
          </el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.fileUp">{{ error.fileUp }}</div>
      </div>
      <div class="process">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.cyn')}}</span>
          </div>
          <span v-if="step.mint != 1" :class="step.mint== 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="step.fileUp != 2">{{$t('dialog.mint')}}
          </el-button>
          <el-button @click="onMint" type="primary" v-else-if="step.mint ==0">{{$t('dialog.mint')}}
          </el-button>
          <el-button disabled type="info" v-else-if="step.mint == 1">{{$t('dialog.inProgress')}}
          </el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.mint">{{ error.mint }}</div>
      </div>
      <div class="process" v-if="nft.form.onSale">
        <div class="step-info">
          <div class="text">
            <span>{{$t('dialog.signsSale')}}</span>
          </div>
          <span v-if="step.sale != 1" :class="step.sale == 2 ? 'finish': ''" class="step iconfont icon-seleted"></span>
          <div class="step loading" v-else>
            <img class="loading-animation" src="@/assets/img/loading.png" />
          </div>
        </div>
        <div class="process-btn">
          <el-button disabled type="info" v-if="step.mint != 2">{{$t('dialog.sale')}}</el-button>
          <el-button @click="onSale" type="primary" v-else-if="step.sale ==0">{{$t('dialog.sale')}}
          </el-button>
          <el-button disabled type="info" v-else-if="step.sale == 1">{{$t('dialog.inProgress')}}
          </el-button>
          <el-button disabled type="info" v-else>{{$t('dialog.done')}}</el-button>
        </div>
        <div class="process-error" v-if="error.sale">{{ error.sale }}</div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
  import BigNumber from 'bignumber.js'

  export default {
    data: function () {
      return {
        visible: this.show,
        step: {
          approve: 0,
          mint: 0,
          sale: 0,
          fileUp: 0,
        },
        error: {
          all: "",
          approve: "",
          mint: "",
          sale: "",
          fileUp: "",
        },
        asset: {},
        storeAsset: {},
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
      file: {
        type: String,
        default: "",
      },
      animFile: {
        type: String,
        default: ''
      }
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
      async fileUp () {
        if (this.nft.form.onSale && this.step.approve != 2) return;
        this.step.fileUp = 1;
        let asset = this.asset;
        let result = null
        if (!this.animFile) {
          result = await this.uploadStorage(this.file);
          if (result.error) {
            this.error.fileUp = result.error;
            this.step.mint = 0;
            this.step.fileUp = 0;
            return;
          }
          this.step.fileUp = 2;
          asset.storageId = result.id;
          asset.url = result.url;
          this.storeAsset = asset;
        } else {
          let files = [this.animFile, this.file];
          result = await this.multiUploadStorage(files);
          if (result.error) {
            this.error.fileUp = result.error;
            this.step.mint = 0;
            this.step.fileUp = 0;
            return;
          }
          this.error.fileUp = "";
          this.step.fileUp = 2;

          let animResult = result[0];
          let fileResult = result[1];

          asset.animStorageId = animResult.id;
          asset.animUrl = animResult.url;
          asset.storageId = fileResult.id;
          asset.url = fileResult.url;
          this.storeAsset = asset;
        }
      },
      async start () {
        this.asset = this.nft.contract;
        if (this.nft.form.onSale) {
          await this.onApprove();
        } else {
          await this.fileUp();
        }
      },
      async onApprove () {
        if (!this.nft.form.onSale) return;
        this.step.approve = 1;
        let asset = this.asset;
        let approved = await this.setApproveAll(asset);
        if (approved.error) {
          this.error.approve = approved.error;
          this.step.approve = 0;
          return;
        }
        this.asset = asset;
        this.error.approve = "";
        this.step.approve = 2;
      },
      async onMint () {
        let asset = this.storeAsset;
        this.step.mint = 1;
        let result = await this.mintToken(asset);
        if (result.error) {
          this.error.mint = result.error;
          this.step.mint = 0;
          return;
        }
        this.asset = Object.assign(this.asset);
        this.error.mint = "";
        this.step.mint = 2;
        if (!this.nft.form.onSale) {
          this.confirm({});
        }
      },
      async onSale () {
        if (!this.nft.form.onSale) return;
        this.step.sale = 1;
        let asset = {
          ...this.asset,
          price: this.nft.form.price,
          sellerFee: this.config.sellerFee,
        };
        let result = await this.saleToken(asset);
        if (result.error) {
          this.error.sale = result.error;
          this.step.sale = 0;
          return;
        }
        this.step.sale = 2;
        this.confirm({});
      },
      confirm (data) {
        this.$emit("confirm", data);
      },
      close () {
        this.$emit("close");
      },
      closed () {
        this.step = {
          approve: 0,
          mint: 0,
          sale: 0,
          fileUp: 0,
        };
        this.error = {
          all: "",
          approve: "",
          mint: "",
          sale: "",
          fileUp: "",
        };
      },
      async setApproveAll (asset) {
        let isApproved = await this.$sdk.isApprovedForAll(
          asset,
          this.user.coinbase,
          this.config.contract.transferProxyAddress,
        );
        if (typeof isApproved == "object" && isApproved.error) {
          return isApproved;
        }
        if (isApproved) return true;
        let result = await this.$sdk.setApprovalForAll(
          asset,
          this.user.coinbase,
          this.config.contract.transferProxyAddress,
          true
        );
        return result;
      },
      async uploadStorage (file) {
        return new Promise((resolve, reject) => {
          const formData = new FormData();
          formData.append("file", file)
          var that = this;
          this.$api("storage.upload", formData).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              resolve(res.data);
            } else {
              resolve({
                error: res.errmsg,
              });
            }
          });
        });
      },
      async multiUploadStorage (files) {
        return new Promise((resolve, reject) => {
          const formData = new FormData();
          for (var i = 0;i < files.length;i++) {
            formData.append("files", files[i]);
          }
          var that = this;
          this.$api("storage.multiupload", formData).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              resolve(res.data);
            } else {
              resolve({
                error: res.errmsg,
              });
            }
          });
        });
      },
      async mintToken (asset) {
        return new Promise((resolve, reject) => {
          let data = { address: asset.address };
          var that = this;
          this.$api("dapp.sign", data).then(async function (res) {
            if (that.$tools.checkResponse(res)) {
              let signature = res.data;
              asset.signature = {
                v: signature.v.toLocaleLowerCase(),
                r: signature.r.toLocaleLowerCase(),
                s: signature.s.toLocaleLowerCase(),
              };
              asset.tokenId = signature.tokenId;
              asset.fees = [];
              if (that.nft.form.royalties) {
                asset.fees.push({
                  recipient: that.user.coinbase,
                  value: that.nft.form.royalties,
                  tokenId: asset.tokenId,
                })
              }
              that.asset = asset;
              var properties = "";
              if (that.nft.form.properties.length) {
                properties = JSON.stringify(that.nft.form.properties);
              }
              var _data = {
                address: asset.address,
                storageId: asset.storageId,
                imgUrl: asset.url,
                tokenId: asset.tokenId,
                description: that.nft.form.description,
                royalties: JSON.stringify([that.nft.form.royalties]),
                categoryId: that.nft.form.category,
                properties: properties,
                type: asset.type,
                name: that.nft.form.name,
                animUrl: asset.animUrl,
                animStorageId: asset.animStorageId
              };
              that.$api("nft.add", _data).then(async function (res) {
                if (that.$tools.checkResponse(res)) {
                  asset.tokenURI = res.data.metadataUrl;
                  asset.tokenURI = asset.tokenURI.replace('ipfs:/', '')

                  let result;
                  result = await that.$sdk.mintToken(
                    that.user.coinbase,
                    asset
                  );
                  resolve(result);
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
      async saleToken (asset) {
        return new Promise((resolve, reject) => {
          var paytoken = this.nft.payToken;

          let buyValue = new BigNumber(this.nft.form.price);
          buyValue = buyValue.multipliedBy(
            new BigNumber(10).exponentiatedBy(paytoken.decimals)).toFixed();
 
          var data = {
            owner: this.user.coinbase,
            sellToken: asset.address,
            sellTokenId: asset.tokenId,
            sellValue: 1,
            sellType: this.$sdk.valueAssetType("ERC721"),
            buyToken: paytoken.address,
            buyTokenId: "0",
            buyValue: buyValue,
            buyType: paytoken.type,
            salt: "0",
            type: this.$sdk.valueCommonType("SALE")
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
                type: that.$sdk.valueCommonType("SALE"),
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
              };
              that.$api("order.add", _data).then(function (res) {
                if (that.$tools.checkResponse(res)) {
                  that.$tools.message(that.$t('request.mintSuccess'), "success");
                  resolve(true);
                } else {
                  that.$tools.message(res.errmsg);
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
    },
  };
</script>
<style lang="scss" scoped>
</style>

