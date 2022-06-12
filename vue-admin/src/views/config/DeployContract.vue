<template>
  <el-form :model="editData" ref="formRef" label-position="top">
    <el-form-item :label="label" :prop="addressKey" required>
      <div style="display: flex; flex:1">
        <div style="flex: 1">
          <template v-if="editKey === propKey">
            <el-input v-model="editData[addressKey]" />
          </template>
          <template v-else>
            {{ showData[addressKey] }}
          </template>
        </div>
        <div style="padding-left: 15px">
          <template v-if="editKey === propKey">
            <el-button
              v-permission="['*']"
              size="small"
              :loading="loading"
              plain
              class="mr-10"
              type="primary"
              @click="updateConfig(1)"
              >{{ $t("global.update") }}</el-button
            >
            <el-button
              size="small"
              :loading="loading"
              plain
              @click="$emit('setEditKey', '')"
              >{{ $t("global.cancel") }}</el-button
            >
          </template>
          <template v-else>
            <el-button
              v-permission="['*']"
              size="small"
              :loading="loading"
              plain
              class="mr-10"
              type="danger"
              @click="editConfig"
              >{{ $t("global.edit") }}</el-button
            >
            <el-button
              size="small"
              :loading="loading"
              plain
              type="success"
              v-permission="['POST /admin/config/update']"
              @click="deploy"
              >{{ $t("global.deploy") }}</el-button
            >
          </template>
        </div>
      </div>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    props: {
      label: {
        type: String,
        required: true,
      },
      dataKey: {
        type: String,
        required: true,
      },
      propKey: {
        type: String,
        required: true,
      },
      editKey: {
        type: String,
        required: true,
      },
      showData: {
        type: Object,
        required: true,
      },
      deployFn: {
        type: Function,
        required: true,
      },
    },
    computed: {
      addressKey() {
        return this.propKey + "Address";
      },
    },
    data() {
      return {
        loading: false,
        editData: {},
      };
    },
    methods: {
      editConfig() {
        this.editData = JSON.parse(JSON.stringify(this.showData));
        this.$emit("setEditKey", this.propKey);
      },
      async updateConfig(type) {
        let validRet = false;
        validRet = await this.$refs.formRef.validate();
        if (!validRet) return;

        if (type) {
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
        this.loading = true;
        const value = JSON.stringify(this.editData);
        this.$api("config.update", {
          key: this.dataKey,
          value,
        })
          .then((res) => {
            this.loading = false;
            if (this.$tool.checkResponse(res)) {
              this.$notify.success({
                title: this.$t("global.success"),
                message: this.$t("global.updateSuccess"),
              });
              this.$emit("setEditKey", "");
              this.$store.commit("UPDATE_CONFIG_ITEM", {
                key: this.dataKey,
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
      async deploy() {
        this.loading = true;
        await this.deployFn(this.propKey);
        this.loading = false;
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
