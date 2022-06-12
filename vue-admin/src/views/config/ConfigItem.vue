<template>
  <div class="config-item">
    <div class="item-label">{{ label }}</div>
    <div class="item-value" v-if="dataKey !== editKey">
      <div class="show-value">{{ config[dataKey] }}</div>
      <el-link
        type="danger"
        @click="editConfig"
        v-permission="['POST /admin/config/update']"
        >{{ $t("global.edit") }}</el-link
      >
    </div>
    <div class="item-value" v-else>
      <div class="show-value input-value">
        <slot :config="editData" />
      </div>
      <el-link class="mr-10" type="primary" @click="updateConfig">{{
        $t("global.update")
      }}</el-link>
      <el-link type="info" @click="cancelConfig()">{{
        $t("global.cancel")
      }}</el-link>
    </div>
  </div>
</template>

<script>
  import { mapState } from "vuex";
  export default {
    data() {
      return {
        editData: {},
      };
    },
    props: {
      label: {
        type: String,
        required: true,
      },
      dataKey: {
        type: String,
        required: true,
      },
      editKey: {
        type: String,
        required: true,
      },
    },
    computed: {
      ...mapState({
        config: (state) => {
          return state.app.config || {};
        },
      }),
    },
    methods: {
      editConfig() {
        this.editData = JSON.parse(JSON.stringify(this.config));
        this.$emit("setEditKey", this.dataKey);
      },
      cancelConfig() {
        this.$emit("clearEditKey");
      },
      async updateConfig() {
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
        var data = {
          key: this.dataKey,
          value: this.editData[this.dataKey],
        };
        this.$api("config.update", data)
          .then((res) => {
            if (this.$tool.checkResponse(res)) {
              this.$notify.success({
                title: this.$t("global.success"),
                message: this.$t("global.updateSuccess"),
              });
              this.cancelConfig();
              this.$store.commit("UPDATE_CONFIG_ITEM", {
                key: this.dataKey,
                value: this.editData[this.dataKey],
              });
              // this.originValue = this.config[this.dataKey]
            } else {
              this.$notify.error({
                title: this.$t("global.fail"),
                message: res.errmsg,
              });
            }
          })
          .catch((res) => {
            this.$notify.error({
              title: this.$t("global.fail"),
              message: this.$t("response." + res.data.errno),
            });
          });
      },
    },
  };
</script>

<style lang="scss" scoped></style>
