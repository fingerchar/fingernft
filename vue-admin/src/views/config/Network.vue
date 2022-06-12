<template>
  <div class="config-group">
    <div style="text-align: right">
      <el-button
        :loading="loading"
        v-permission="['*']"
        type="danger"
        v-if="editKey !== 'edit'"
        @click="editConfig"
        >{{ $t("global.edit") }}</el-button
      >
      <el-button
        :loading="loading"
        v-permission="['*']"
        type="primary"
        v-if="editKey === 'edit'"
        @click="updateConfig"
        >{{ $t("global.save") }}</el-button
      >
      <el-button
        :loading="loading"
        v-permission="['*']"
        v-if="editKey === 'edit'"
        @click="cancelConfig()"
        >{{ $t("global.cancel") }}</el-button
      >
    </div>
    <el-divider />
    <el-form
      ref="formRef"
      :model="editData"
      :rules="rules"
      label-position="top"
    >
      <el-form-item :label="$t('config.network')" prop="network">
        <template v-if="editKey !== 'edit'">{{ showData.network }}</template>
        <template v-else>
          <el-select v-model="editData.network" @change="onSelectChange">
            <el-option-group v-for="(g, i) in groups" :key="i" :label="g.label">
              <el-option
                v-for="(o, j) in g.options"
                :key="j"
                :label="o.network"
                :value="o.network"
              />
            </el-option-group>
          </el-select>
        </template>
      </el-form-item>
      <el-form-item :label="$t('config.network_name')" prop="name">
        <template v-if="editKey !== 'edit'">{{ showData.name }}</template>
        <template v-else-if="editData.network !== 'Other'">
          {{ editData.name }}
        </template>
        <template v-else>
          <el-input v-model="editData.name" />
        </template>
      </el-form-item>

      <el-form-item :label="$t('config.network_symbol')" prop="symbol">
        <template v-if="editKey !== 'edit'">{{ showData.symbol }}</template>
        <template v-else-if="editData.network !== 'Other'">
          {{ editData.symbol }}
        </template>
        <template v-else>
          <el-input v-model="editData.symbol" />
        </template>
      </el-form-item>

      <el-form-item :label="$t('config.network_rpc')" prop="rpc">
        <template v-if="editKey !== 'edit'">{{ showData.rpc }}</template>
        <template v-else>
          <el-input v-model="editData.rpc" />
        </template>
      </el-form-item>

      <el-form-item :label="$t('config.network_chainId')" prop="chainId">
        <template v-if="editKey !== 'edit'">{{ showData.chainId }}</template>
        <template v-else-if="editData.network !== 'Other'">
          {{ editData.chainId }}
        </template>
        <template v-else>
          <el-input v-model="editData.chainId" />
        </template>
      </el-form-item>

      <el-form-item :label="$t('config.network_explorer')" prop="explorer">
        <template v-if="editKey !== 'edit'">{{ showData.explorer }}</template>
        <template v-else-if="editData.network !== 'Other'">
          {{ editData.explorer }}
        </template>
        <template v-else>
          <el-input v-model="editData.explorer" />
        </template>
      </el-form-item>

      <el-form-item :label="$t('config.network_blockTime')" prop="blockTime">
        <template v-if="editKey !== 'edit'">{{ showData.blockTime }}</template>
        <template v-else-if="editData.network !== 'Other'">
          {{ editData.blockTime }}
        </template>
        <template v-else>
          <el-input-number
            v-model="editData.blockTime"
            :min="0"
            controls-position="right"
            style="width: 100%"
          />
        </template>
      </el-form-item>

      <el-form-item :label="$t('config.network_opensea')" prop="opensea">
        <template v-if="editKey !== 'edit'">{{ showData.opensea }}</template>
        <template v-else-if="editData.network !== 'Other'">
          {{ editData.opensea }}
        </template>
        <template v-else>
          <el-input v-model="editData.opensea" />
        </template>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import networkOptions from "@/utils/network-options.js";
  import { mapState } from "vuex";
  const dataKey = "configNetwork";
  export default {
    data() {
      return {
        dataKey,
        groups: networkOptions,
        editData: {},
        editKey: "",
        loading: false,
        rules: {
          network: {
            required: true,
            message:
              this.$t("config.network") + " " + this.$t("global.isRequired"),
            trigger: "change",
          },
          name: {
            required: true,
            message:
              this.$t("config.network_name") +
              " " +
              this.$t("global.isRequired"),
            trigger: "blur",
          },
          symbol: {
            required: true,
            message:
              this.$t("config.network_symbol") +
              " " +
              this.$t("global.isRequired"),
            trigger: "blur",
          },
          rpc: {
            required: true,
            message:
              this.$t("config.network_rpc") +
              " " +
              this.$t("global.isRequired"),
            trigger: "blur",
          },
          chainId: {
            required: true,
            message:
              this.$t("config.network_chainId") +
              " " +
              this.$t("global.isRequired"),
            trigger: "blur",
          },
          explorer: {
            required: true,
            message:
              this.$t("config.network_explorer") +
              " " +
              this.$t("global.isRequired"),
            trigger: "blur",
          },
          blockTime: {
            required: true,
            type: "number",
            message:
              this.$t("config.network_blockTime") +
              " " +
              this.$t("global.isRequired"),
            trigger: "change",
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
    },
    methods: {
      editConfig() {
        this.editKey = "edit";
        this.editData = JSON.parse(JSON.stringify(this.showData));
        if (!this.editData.network) {
          this.editData.network = "Other";
        }
      },
      onSelectChange(val) {
        for (let i = 0; i < this.groups.length; i++) {
          const group = this.groups[i];
          for (let j = 0; j < group.options.length; j++) {
            const option = group.options[j];
            if (option.network === val) {
              this.editData = JSON.parse(JSON.stringify(option));
              return;
            }
          }
        }
      },
      cancelConfig() {
        this.$refs.formRef.clearValidate();
        this.editKey = "";
      },
      async updateConfig() {
        let validRet = await this.$refs.formRef.validate()
        if (!validRet) return;

        const confirmRet = await this.$confirm(
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
        if (!confirmRet) return;

        const value = JSON.stringify(this.editData);
        var data = {
          key: dataKey,
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
              this.$store.commit("UPDATE_CONFIG_ITEM", {
                key: dataKey,
                value: this.editData,
              });
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
    },
  };
</script>

<style lang="scss" scoped>
  .config-group {
    .el-select {
      width: 100%;
    }
  }

  .el-form-item ::v-deep .el-form-item__label {
    font-size: 18px;
    color: #333;
  }
</style>
