<template>
  <div class="config-group">
    <template v-if="!hasNetwork">
      <div align="center" style="padding: 40px 0">
        <el-button
          v-permission="['*']"
          @click="$emit('toggleTab', 'network')"
          >{{ $t("config.goNetwork") }}</el-button
        >
      </div>
    </template>
    <template v-else>
      <el-divider>{{ $t("config.deployConfig") }}</el-divider>
      <div style="text-align: right">
        <el-button
          v-permission="['*']"
          :loading="loading"
          type="danger"
          v-if="editKey !== 'edit'"
          @click="editConfig(dataKey2, 'edit')"
          >{{ $t("global.edit") }}</el-button
        >
        <el-button
          v-permission="['*']"
          :loading="loading"
          type="primary"
          v-if="editKey === 'edit'"
          @click="updateConfig(dataKey2)"
          >{{ $t("global.save") }}</el-button
        >
        <el-button
          v-permission="['*']"
          :loading="loading"
          v-if="editKey === 'edit'"
          @click="cancelConfig(1)"
          >{{ $t("global.cancel") }}</el-button
        >
      </div>
      <el-form
        ref="form2Ref"
        :model="editData2"
        :rules="rules2"
        label-position="top"
      >
        <el-form-item
          :label="$t('config.network_beneficiary')"
          prop="beneficiary"
        >
          <template v-if="editKey !== 'edit'">{{
            showData2.beneficiary
          }}</template>
          <template v-else>
            <el-input v-model="editData2.beneficiary" />
          </template>
        </el-form-item>
        <el-form-item :label="$t('config.network_miner')" prop="minerKey">
          <template v-if="editKey !== 'edit'">{{
            showData2.minerKey
          }}</template>
          <template v-else>
            <el-input v-model="editData2.minerKey" />
          </template>
        </el-form-item>
        <el-form-item
          :label="$t('config.network_buyerFeeSigner')"
          prop="buyerFeeKey"
        >
          <template v-if="editKey !== 'edit'">{{
            showData2.buyerFeeKey
          }}</template>
          <template v-else>
            <el-input v-model="editData2.buyerFeeKey" />
          </template>
        </el-form-item>
        <el-form-item :label="$t('config.contractName')" prop="contractName">
          <template v-if="editKey !== 'edit'">{{
            showData2.contractName
          }}</template>
          <template v-else>
            <el-input v-model="editData2.contractName" />
          </template>
        </el-form-item>
        <el-form-item
          :label="$t('config.contractSymbol')"
          prop="contractSymbol"
        >
          <template v-if="editKey !== 'edit'">{{
            showData2.contractSymbol
          }}</template>
          <template v-else>
            <el-input v-model="editData2.contractSymbol" />
          </template>
        </el-form-item>
      </el-form>

      <template v-if="isPreset">
        <el-divider>{{ $t("config.deployContract") }}</el-divider>
        <deploy-contract
          :label="$t('config.Multicall')"
          propKey="multicall"
          :dataKey="dataKey"
          :editKey="editKey"
          :showData="showData"
          @setEditKey="editKey = $event"
          :deployFn="deploy"
        />
        <deploy-contract
          :label="$t('config.NFT721')"
          propKey="nft721"
          :dataKey="dataKey"
          :editKey="editKey"
          :showData="showData"
          @setEditKey="editKey = $event"
          :deployFn="deploy"
        />
        <deploy-contract
          :label="$t('config.TransferProxy')"
          propKey="transferProxy"
          :dataKey="dataKey"
          :editKey="editKey"
          :showData="showData"
          @setEditKey="editKey = $event"
          :deployFn="deploy"
        />
        <deploy-contract
          :label="$t('config.TransferProxyForDeprecated')"
          propKey="transferProxyForDeprecated"
          :dataKey="dataKey"
          :editKey="editKey"
          :showData="showData"
          @setEditKey="editKey = $event"
          :deployFn="deploy"
        />
        <deploy-contract
          :label="$t('config.ERC20TransferProxy')"
          propKey="erc20TransferProxy"
          :dataKey="dataKey"
          :editKey="editKey"
          :showData="showData"
          @setEditKey="editKey = $event"
          :deployFn="deploy"
        />
        <deploy-contract
          :label="$t('config.ExchangeState')"
          propKey="exchangeState"
          :dataKey="dataKey"
          :editKey="editKey"
          :showData="showData"
          @setEditKey="editKey = $event"
          :deployFn="deploy"
        />
        <deploy-contract
          :label="$t('config.ExchangeOrdersHolder')"
          propKey="exchangeOrdersHolder"
          :dataKey="dataKey"
          :editKey="editKey"
          :showData="showData"
          @setEditKey="editKey = $event"
          :deployFn="deploy"
        />
        <deploy-contract
          :label="$t('config.NftExchange')"
          propKey="nftExchange"
          :dataKey="dataKey"
          :editKey="editKey"
          :showData="showData"
          @setEditKey="editKey = $event"
          :deployFn="deploy"
        />

        <el-divider>{{ $t("config.addOperators") }}</el-divider>
        <div class="config-item" v-for="(v, k) in operatorMap" :key="k">
          <div class="item-value" v-for="(v1, k1) in v" :key="k1">
            <el-button
              :loading="loadingMap[`${k}_${k1}`]"
              :type="v1 ? 'success' : 'warning'"
              :disabled="v1"
              v-permission="['POST /admin/config/update']"
              plain
              @click="_addOperator(k, k1)"
              >{{ $t("config.addOperator") }} {{ k }} {{ $t("config.to") }}
              {{ k1 }}</el-button
            >
          </div>
        </div>
      </template>
    </template>
  </div>
</template>
<script>
  import DeployContract from "./DeployContract.vue";
  import deploy from "@/utils/sdk/deploy.js";
  import operator from "@/utils/sdk/operator.js";
  import util_web3 from "@/utils/web3/index.js";
  import { mapState } from "vuex";
  const dataKey = "configContract";
  const dataKey2 = "configDeploy";

  export default {
    components: {
      DeployContract,
    },
    data() {
      return {
        dataKey,
        dataKey2,
        editKey: "",
        operatorMap: {
          nftExchange: {
            transferProxy: false,
            transferProxyForDeprecated: false,
            erc20TransferProxy: false,
            exchangeState: false,
          },
        },
        loading: false,
        loadingMap: {},
        editData: {},
        editData2: {},
        rules2: {
          beneficiary: {
            required: true,
            message:
              this.$t("network_beneficiary") + this.$t("global.isRequired"),
            trigger: "blur",
          },
          minerKey: {
            required: true,
            message: this.$t("network_minerKey") + this.$t("global.isRequired"),
            trigger: "blur",
          },
          buyerFeeKey: {
            required: true,
            message:
              this.$t("network_buyerFeeKey") + this.$t("global.isRequired"),
            trigger: "blur",
          },
          contractName: {
            required: true,
            message:
              this.$t("network_contractName") + this.$t("global.isRequired"),
            trigger: "blur",
          },
          contractSymbol: {
            required: true,
            message:
              this.$t("network_contractSymbol") + this.$t("global.isRequired"),
            trigger: "blur",
          },
        },
      };
    },
    computed: {
      ...mapState({
        config: (state) => state.app.config,
      }),
      showData() {
        if (this.config) {
          return this.config[dataKey] || {};
        } else {
          return {};
        }
      },
      showData2() {
        if (this.config) {
          return this.config[dataKey2] || {};
        } else {
          return {};
        }
      },
      isPreset() {
        return (
          this.showData2.minerKey &&
          this.showData2.beneficiary &&
          this.showData2.buyerFeeKey &&
          this.showData2.contractName &&
          this.showData2.contractSymbol
        );
      },
      hasNetwork() {
        if (!this.config || !this.config.configNetwork) return false;
        const nw = this.config.configNetwork;
        return nw.chainId && nw.name && nw.symbol && nw.rpc;
      },
    },
    methods: {
      editConfig(key, ek) {
        this.editKey = ek;
        if (key === dataKey) {
          this.editData = JSON.parse(JSON.stringify(this.showData));
        } else {
          this.editData2 = JSON.parse(JSON.stringify(this.showData2));
        }
      },
      cancelConfig() {
        this.$refs.form2Ref.clearValidate();
        this.editKey = "";
      },
      async updateConfig(type) {
        if (type) {
          let validRet = false;
          if (type === dataKey2) {
            await this.$refs.form2Ref.validate((valid) => {
              validRet = valid;
            });
          }
          if (!validRet) return;
          const ret = await this.$confirm(
            this.$t("global.updateTip"),
            this.$t("global.tip"),
            {
              confirmButtonText: this.$t("global.confirm"),
              cancelButtonText: this.$t("global.cancel"),
              type: "warning",
            }
          ).catch(() => {
            return false;
          });
          if (!ret) return;
        }
        let value = JSON.stringify(this.editData);
        if (type === dataKey2) {
          value = JSON.stringify(this.editData2);
        }
        const key = type === dataKey2 ? dataKey2 : dataKey;
        var data = {
          key,
          value,
        };
        this.loading = true;
        this.$api("config.update", data)
          .then((res) => {
            this.loading = false;
            if (this.$tool.checkResponse(res)) {
              this.$notify.success({
                title: this.$t("global.success"),
                message: this.$t("global.updateSuccess"),
              });
              this.cancelConfig();
              if (type === dataKey2) {
                this.$store.commit("UPDATE_CONFIG_ITEM", {
                  key: dataKey2,
                  value: this.editData2,
                });
              } else {
                this.$store.commit("UPDATE_CONFIG_ITEM", {
                  key: dataKey,
                  value: this.editData,
                });
              }
            } else {
              this.$notify.error({
                title: this.$t("global.fail"),
                message: res.errmsg,
              });
            }
          })
          .catch((res) => {
            this.loading = false;
            this.$notify.error({
              title: this.$t("global.fail"),
              message: this.$t("response." + res.data.errno),
            });
          });
      },
      async deploy(type) {
        if (!util_web3.checkNetwork()) return;
        this.editData2 = JSON.parse(JSON.stringify(this.showData2));
        this.editData = JSON.parse(JSON.stringify(this.showData));

        let args = [];
        const minerAddress = util_web3.ethersAddress(this.editData2.minerKey);
        if (!util_web3.checkMiner(minerAddress)) return;
        const buyerFeeAddress = util_web3.ethersAddress(
          this.editData2.buyerFeeKey
        );
        const beneficiary = this.editData2.beneficiary;
        const contractName = this.editData2.contractName;
        const contractSymbol = this.editData2.contractSymbol;
        switch (type) {
          case "nft721":
            args = [
              contractName,
              contractSymbol,
              minerAddress,
              "ipfs:/",
              "ipfs:/",
            ];
            break;
          case "nftExchange":
            if (
              !this.editData.transferProxyAddress ||
              !this.editData.transferProxyForDeprecatedAddress ||
              !this.editData.erc20TransferProxyAddress ||
              !this.editData.exchangeStateAddress ||
              !this.editData.exchangeOrdersHolderAddress
            ) {
              this.$notify.error({
                title: this.$t("global.fail"),
                message:
                  this.$t("config.pleaseFirstSet") +
                  "transferProxy, transferProxyForDeprecated, erc20TransferProxy, exchangeState, exchangeOrdersHolder",
              });
              return;
            }
            args = [
              this.editData.transferProxyAddress,
              this.editData.transferProxyForDeprecatedAddress,
              this.editData.erc20TransferProxyAddress,
              this.editData.exchangeStateAddress,
              this.editData.exchangeOrdersHolderAddress,
              beneficiary,
              buyerFeeAddress,
            ];
            break;
        }
        const result = await deploy(type, args);
        if (result) {
          if (result.error) {
            this.$notify.error({
              title: this.$t("global.fail"),
              message: result.error.message || result.error,
            });
          } else {
            // save address
            this.editData[`${type}Address`] = result.address;
            await this.updateConfig();
          }
        }
      },
      async _addOperator(operatorKey, addressKey) {
        if (!util_web3.checkNetwork()) return;
        this.loadingMap[`${operatorKey}_${addressKey}`] = true;
        this.editData = JSON.parse(JSON.stringify(this.showData));
        // checkIsOperator
        let result = null;
        result = await operator.isOperator(
          this.editData[`${operatorKey}Address`],
          this.editData[`${addressKey}Address`]
        );
        if (result.error) {
          this.$notify.error({
            title: this.$t("global.fail"),
            message: result.error.message || result.error,
          });
          return;
        }
        if (result) {
          this.operatorMap[operatorKey][addressKey] = true;
          this.loadingMap[`${operatorKey}_${addressKey}`] = false;
          return;
        }

        result = await operator.addOperator(
          this.editData[`${operatorKey}Address`],
          this.editData[`${addressKey}Address`]
        );
        if (result.error) {
          this.$notify.error({
            title: this.$t("global.fail"),
            message: result.error.message || result.error,
          });
        } else {
          this.operatorMap[operatorKey][addressKey] = true;
        }
        this.loadingMap[`${operatorKey}_${addressKey}`] = false;
      },
    },
  };
</script>

<style lang="scss" scoped>
  .el-form-item ::v-deep .el-form-item__label {
    font-size: 18px;
    color: #333;
  }
</style>
