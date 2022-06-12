<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        clearable
        class="filter-item width-200px mr-10"
        :placeholder="$t('systemManagement.placeholder3')"
      />
      <el-button
        v-permission="['GET /admin/role/list']"
        class="filter-item"
        type="primary"
        @click="handleFilter"
        ><el-icon><Search /></el-icon
        >{{ $t("systemManagement.select") }}</el-button
      >
      <el-button
        v-permission="['POST /admin/role/create']"
        class="filter-item"
        type="primary"
        @click="handleCreate"
        ><el-icon><Plus /></el-icon>{{ $t("systemManagement.add") }}</el-button
      >
    </div>

    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      :element-loading-text="$t('util.loadingText')"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        align="center"
        :label="$t('systemManagement.roleName')"
        prop="name"
        sortable
      />

      <el-table-column
        align="center"
        :label="$t('systemManagement.description')"
        prop="desc"
      />

      <el-table-column
        align="center"
        :label="$t('systemManagement.operation')"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            v-permission="['POST /admin/role/update']"
            type="primary"
            size="small"
            @click="handleUpdate(scope.row)"
            >{{ $t("systemManagement.edit") }}</el-button
          >
          <el-button
            v-permission="['POST /admin/role/delete']"
            type="danger"
            size="small"
            @click="handleDelete(scope.row)"
            >{{ $t("systemManagement.delete") }}</el-button
          >
          <el-button
            v-permission="['POST /admin/role/permissions']"
            type="primary"
            size="small"
            @click="handlePermission(scope.row)"
            >{{ $t("systemManagement.authorization") }}</el-button
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
        ref="dataForm"
        :rules="rules"
        :model="dataForm"
        status-icon
        label-position="left"
        label-width="100px"
        class="dataFormClass"
      >
        <el-form-item :label="$t('systemManagement.roleName')" prop="name">
          <el-input v-model="dataForm.name" />
        </el-form-item>
        <el-form-item :label="$t('systemManagement.description')" prop="desc">
          <el-input v-model="dataForm.desc" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">{{
            $t("util.cancel")
          }}</el-button>
          <el-button
            v-if="dialogStatus == 'create'"
            type="primary"
            @click="createData"
            >{{ $t("util.confirm") }}</el-button
          >
          <el-button v-else type="primary" @click="updateData">{{
            $t("util.confirm")
          }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 权限配置对话框 -->
    <el-dialog
      v-model="permissionDialogFormVisible"
      :title="$t('systemManagement.permissionConfiguration')"
    >
      <el-tree
        ref="tree"
        :data="systemPermissions"
        :default-checked-keys="assignedPermissions"
        show-checkbox
        node-key="id"
        highlight-current
      >
        <template #default="{ data }">
          <span class="custom-tree-node">
            <span>{{ data.label }}</span>
            <el-tag v-if="data.api" type="success" size="small">{{
              data.api
            }}</el-tag>
          </span>
        </template>
      </el-tree>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="permissionDialogFormVisible = false">{{
            $t("util.cancel")
          }}</el-button>
          <el-button type="primary" @click="updatePermission">{{
            $t("util.confirm")
          }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import Pagination from "@/components/Pagination";
  export default {
    name: "Role",
    components: { Pagination },
    data() {
      return {
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          name: "",
          sort: "create_time",
          order: "desc",
        },
        dataForm: {
          id: undefined,
          name: undefined,
          desc: undefined,
        },
        dialogFormVisible: false,
        dialogStatus: "",
        textMap: {
          update: this.$t("systemManagement.edit"),
          create: this.$t("systemManagement.create"),
        },
        rules: {
          name: [
            {
              required: true,
              message: this.$t("systemManagement.rule3"),
              trigger: "blur",
            },
          ],
        },
        permissionDialogFormVisible: false,
        systemPermissions: null,
        assignedPermissions: null,
        permissionForm: {
          roleId: undefined,
          permissions: [],
        },
      };
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.listLoading = true;
        this.$api("role.list", this.listQuery)
          .then((response) => {
            this.list = response.data.list;
            this.total = response.data.total;
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
          id: undefined,
          name: undefined,
          desc: undefined,
        };
      },
      handleCreate() {
        this.resetForm();
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs["dataForm"].clearValidate();
        });
      },
      createData() {
        this.$refs["dataForm"].validate((valid) => {
          if (valid) {
            this.$api("role.createRole", this.dataForm)
              .then((response) => {
                this.list.unshift(response.data.data);
                this.dialogFormVisible = false;
                this.$notify.success({
                  title: this.$t("util.success"),
                  message: this.$t("systemManagement.roleAddedSuccessfully"),
                });
                this.getList();
              })
              .catch((response) => {
                this.$notify.error({
                  title: this.$t("util.fail"),
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
          this.$refs["dataForm"].clearValidate();
        });
      },
      updateData() {
        this.$refs["dataForm"].validate((valid) => {
          if (valid) {
            this.$api("role.updateRole", this.dataForm)
              .then((response) => {
                for (const v of this.list) {
                  if (v.id === this.dataForm.id) {
                    const index = this.list.indexOf(v);
                    this.list.splice(index, 1, this.dataForm);
                    break;
                  }
                }
                this.dialogFormVisible = false;
                if (!response.errno) {
                  this.$notify.success({
                    title: this.$t("util.success"),
                    message: this.$t(
                      "systemManagement.updateAdministratorSucceeded"
                    ),
                  });
                } else {
                  this.$notify.success({
                    title: this.$t("util.fail"),
                    message: this.$t(
                      "systemManagement.updateAdministratorFailed"
                    ),
                  });
                }
              })
              .catch((response) => {
                this.$notify.error({
                  title: this.$t("util.fail"),
                  message: this.$t("response." + response.data.errno),
                });
              });
          }
        });
      },
      handleDelete(row) {
        this.$api("role.deleteRole", { id: row.id })
          .then((response) => {
            if (!response.errno) {
              this.$notify.success({
                title: this.$t("util.success"),
                message: this.$t(
                  "systemManagement.administratorDeletedSuccessfully"
                ),
              });
            } else {
              this.$notify.success({
                title: this.$t("util.fail"),
                message: this.$t(
                  "systemManagement.failedToDeleteAdministrator"
                ),
              });
            }
            this.getList();
          })
          .catch((response) => {
            this.$notify.error({
              title: this.$t("util.fail"),
              message: this.$t("response." + response.data.errno),
            });
          });
      },
      handlePermission(row) {
        this.permissionDialogFormVisible = true;
        this.permissionForm.roleId = row.id;
        this.$api("role.getPermission", { roleId: row.id }).then((response) => {
          this.systemPermissions = response.data.systemPermissions;
          this.assignedPermissions = response.data.assignedPermissions;
        });
      },
      updatePermission() {
        this.permissionForm.permissions = this.$refs.tree
          .getCheckedKeys(true)
          .join(",");
        this.$api("role.updatePermission", this.permissionForm)
          .then((response) => {
            this.permissionDialogFormVisible = false;
            if (!response.errno) {
              this.$notify.success({
                title: this.$t("util.success"),
                message: this.$t("systemManagement.authorizationSucceeded"),
              });
            } else {
              this.$notify.success({
                title: this.$t("util.fail"),
                message: this.$t("systemManagement.authorizationFailed"),
              });
            }
          })
          .catch((response) => {
            this.$notify.error({
              title: this.$t("util.fail"),
              message: this.$t("response." + response.data.errno),
            });
          });
      },
    },
  };
</script>
<style scoped>
  .width-200px {
    width: 200px;
  }
  .dataFormClass {
    width: 400px;
    margin-left: 50px;
  }
</style>
