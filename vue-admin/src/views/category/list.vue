<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-button
        class="filter-item"
        type="primary"
        @click="handleCreate"
        v-permission="['POST /admin/category/create']"
        ><el-icon><Plus /></el-icon>{{ $t("sortManagement.create") }}</el-button
      >
    </div>

    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      :element-loading-text="$t('sortManagement.loadingText')"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        align="center"
        :label="$t('sortManagement.categoryId')"
        prop="id"
        sortable
      />

      <el-table-column
        align="center"
        :label="$t('sortManagement.categoryName')"
        prop="name"
      />

      <el-table-column
        align="center"
        :label="$t('sortManagement.weight')"
        prop="order"
      />

      <el-table-column
        align="center"
        :label="$t('sortManagement.state')"
        prop="deleted"
      >
        <template #default="scope">
          <el-tag type="success" v-if="!scope.row.deleted">{{
            $t("sortManagement.normal")
          }}</el-tag>
          <el-tag type="danger" v-else>{{
            $t("sortManagement.disabled")
          }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        :label="$t('sortManagement.operation')"
        width="300"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            v-permission="['POST /admin/category/update']"
            type="primary"
            size="small"
            @click="handleUpdate(scope.row)"
            >{{ $t("sortManagement.edit") }}</el-button
          >
          <el-button
            v-permission="['POST /admin/category/disable']"
            type="danger"
            size="small"
            @click="handleDisable(scope.row)"
            v-if="!scope.row.deleted"
            >{{ $t("sortManagement.disable") }}</el-button
          >
          <el-button
            v-permission="['POST /admin/category/enable']"
            type="success"
            size="small"
            @click="handleEnable(scope.row)"
            v-else
            >{{ $t("sortManagement.enable") }}</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="listQuery.page"
      v-model:limit="listQuery.limit"
      @pagination="getList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" v-model="dialogFormVisible">
      <el-form
        ref="dataFormRef"
        :rules="rules"
        :model="dataForm"
        status-icon
        label-position="left"
        label-width="120px"
      >
        <el-form-item
          :label="$t('sortManagement.categoryId')"
          prop="id"
          v-if="dialogStatus == 'detail'"
        >
          <span>{{ dataForm.id }}</span>
        </el-form-item>

        <el-form-item
          :label="$t('sortManagement.categoryName')"
          prop="name"
          v-if="dialogStatus == 'detail'"
        >
          <span>{{ dataForm.name }}</span>
        </el-form-item>

        <el-form-item
          :label="$t('sortManagement.weight')"
          prop="order"
          v-if="dialogStatus == 'detail'"
        >
          <span>{{ dataForm.order }}</span>
        </el-form-item>

        <el-form-item
          :label="$t('sortManagement.categoryName')"
          prop="name"
          v-if="dialogStatus != 'detail'"
        >
          <el-input v-model="dataForm.name" />
        </el-form-item>

        <el-form-item
          :label="$t('sortManagement.weight')"
          prop="order"
          v-if="dialogStatus != 'detail'"
        >
          <el-input v-model="dataForm.order" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer" v-if="dialogStatus != 'detail'">
          <el-button @click="dialogFormVisible = false">{{
            $t("sortManagement.cancel")
          }}</el-button>
          <el-button
            v-if="dialogStatus == 'create'"
            type="primary"
            @click="createData"
            >{{ $t("sortManagement.confirm") }}</el-button
          >
          <el-button v-else type="primary" @click="updateData">{{
            $t("sortManagement.confirm")
          }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import Pagination from "@/components/Pagination"; // Secondary package based on el-pagination
  export default {
    components: { Pagination },
    data() {
      return {
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10,
          username: undefined,
          mobile: undefined,
          sort: "create_time",
          order: "desc",
        },
        dialogFormVisible: false,
        dialogStatus: "",
        dataForm: {
          id: undefined,
          sort: undefined,
          name: undefined,
        },
        rules: {
          name: [
            {
              required: true,
              message: this.$t("sortManagement.message1"),
              trigger: "blur",
            },
          ],
          sort: [
            {
              required: true,
              message: this.$t("sortManagement.message2"),
              trigger: "blur",
            },
          ],
        },
        textMap: {
          update: this.$t("sortManagement.edit"),
          detail: this.$t("sortManagement.detail"),
          create: this.$t("sortManagement.create"),
        },
        dataFormRef: null,
      };
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.listLoading = true;
        this.$api("category.list", this.listQuery)
          .then((res) => {
            this.list = res.data.list;
            this.total = res.data.total;
            this.listLoading = false;
          })
          .catch(() => {
            this.list = [];
            this.total = 0;
            this.listLoading = false;
          });
      },
      handleFilter() {
        this.listQuery.page = 1;
        this.getList();
      },
      resetForm() {
        this.dataForm = {
          sort: undefined,
          name: undefined,
        };
      },
      handleCreate() {
        this.resetForm();
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs["dataFormRef"].clearValidate();
        });
      },
      createData() {
        this.$refs["dataFormRef"].validate((valid) => {
          if (valid) {
            this.$api("category.createCategory", this.dataForm)
              .then((response) => {
                this.list.unshift(response.data);
                this.dialogFormVisible = false;
                this.$notify.success({
                  title: this.$t("sortManagement.success"),
                  message: this.$t("sortManagement.createSuccess"),
                });
              })
              .catch((response) => {
                this.$notify.error({
                  title: this.$t("sortManagement.fail"),
                  message: this.$t("response." + response.data.errno),
                });
              });
          }
        });
      },
      handleUpdate(row) {
        this.dataForm = Object.assign({}, row);
        this.dialogStatus = "update";
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs["dataFormRef"].clearValidate();
        });
      },
      updateData() {
        this.$refs["dataFormRef"].validate((valid) => {
          if (valid) {
            this.$api("category.updateCategory", {
              id: this.dataForm.id,
              name: this.dataForm.name,
            })
              .then(() => {
                for (const v of this.list) {
                  if (v.id === this.dataForm.id) {
                    const index = this.list.indexOf(v);
                    this.list.splice(index, 1, this.dataForm);
                    break;
                  }
                }
                this.dialogFormVisible = false;
                this.$notify.success({
                  title: this.$t("sortManagement.success"),
                  message: this.$t("sortManagement.editSuccess"),
                });
              })
              .catch((response) => {
                this.$notify.error({
                  title: this.$t("sortManagement.fail"),
                  message: this.$t("response." + response.data.errno),
                });
              });
          }
        });
      },
      handleDisable(row) {
        this.$confirm(
          this.$t("sortManagement.tipContent"),
          this.$t("sortManagement.tip"),
          {
            confirmButtonText: this.$t("sortManagement.confirm"),
            cancelButtonText: this.$t("sortManagement.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            this.$api("category.disableCategory", { id: row.id })
              .then((response) => {
                if (response.errno == -1) {
                  this.$notify.error({
                    message: this.$t("sortManagement.cantDisabled"),
                  });
                } else {
                  this.$notify.success({
                    title: this.$t("sortManagement.success"),
                    message: this.$t("sortManagement.successfullyDisabled"),
                  });
                  this.getList();
                }
              })
              .catch((response) => {
                this.$notify.error({
                  title: this.$t("sortManagement.disablingFailed"),
                  message: this.$t("response." + response.data.errno),
                });
              });
          })
          .catch(() => {});
      },
      handleEnable(row) {
        this.$confirm(
          this.$t("sortManagement.operationEnabled"),
          this.$t("sortManagement.tip"),
          {
            confirmButtonText: this.$t("sortManagement.confirm"),
            cancelButtonText: this.$t("sortManagement.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            this.$api("category.enableCategory", { id: row.id })
              .then(() => {
                this.$notify.success({
                  title: this.$t("sortManagement.success"),
                  message: this.$t("sortManagement.enabledSuccessfully"),
                });
                this.getList();
              })
              .catch((response) => {
                this.$notify.error({
                  title: this.$t("sortManagement.activationFailed"),
                  message: this.$t("response." + response.data.errno),
                });
              });
          })
          .catch(() => {});
      },
    },
  };
</script>
<style lang="scss" scoped>
  .dataFormClass {
    width: 400px;
    margin-left: 50px;
  }
</style>
