<template>
  <div class="app-container">
      <div class="filter-container">
          <el-input v-model="listQuery.address" clearable class="filter-item width-200px" :placeholder="$t('whiteUser.placeholder')" />
            <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handlerFilter">{{$t("whiteUser.select")}}</el-button>
          <el-button
        class="filter-item"
        type="primary"
        icon="el-icon-edit"
        @click="handlerBtn('add')"
        >{{$t("whiteUser.add")}}</el-button
      >
      </div>
        <el-table v-loading="listLoading"
      :data="list"
      :element-loading-text="$t('util.loadingText')"
      border>
<el-table-column label="ID" prop="id" sortable></el-table-column>
<el-table-column :label="$t('whiteUser.userName')" prop="address"></el-table-column>
 <el-table-column
        align="center"
        :label="$t('whiteUser.operation')"
        width="300"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            type="danger"
            size="mini"
            @click="handlerBtn('del', scope.row)"
            >{{$t('whiteUser.delete')}}</el-button
          >
        </template>
      </el-table-column>
  </el-table>

  <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogFormVisible"
      :width="dialogStatus === 'del' ? '30%' : '50%'"
    >
      <template v-if="dialogStatus === 'add'">
        <el-form
          ref="formData"
          :model="formData"
          status-icon
          label-position="left"
          label-width="120px"
          :rules="rules"
        >
          <el-form-item :label="$t('whiteUser.userName')" prop="address">
            <el-input v-model="formData.address"></el-input>
          </el-form-item>
        </el-form>
      </template>
      <template v-if="dialogStatus === 'del'">
        <span>{{$t('whiteUser.deleteTip')}}</span>
      </template>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="handlerCancel">{{$t('whiteUser.cancel')}}</el-button>
        <el-button :loading="apiLoading" type="primary" @click.native.prevent="handlerConfirm">{{$t('whiteUser.confirm')}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from "@/components/Pagination";
export default {
  name: "wihteUser",
  components: { Pagination },
  data() {
    return {
      apiLoading: false,
      listLoading: true,
      list: null,
      total: 0,
      listQuery: {
        page: 1,
        limit: 10,
        address: undefined,
        sort: "create_time",
        order: "desc",
      },
      //   表单
      formData: {
        address: "",
      },
      rules: {
        address: [
          {
            required: true,
            message: this.$t('whiteUser.emptyName'),
            trigger: "blur",
          },
        ],
      },
      dialogFormVisible: false,
      dialogStatus: "",
      delAddress: "",
    };
  },
  computed: {
    dialogTitle() {
      let dialogTitle = "";
      switch (this.dialogStatus) {
        case "add":
          dialogTitle = this.$t("whiteUser.add");
          break;
        case "del":
          dialogTitle = this.$t("whiteUser.delete");
          break;
        default:
          break;
      }
      return dialogTitle;
    },
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.listLoading = true;
      this.$api("user.white", this.listQuery)
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
    resetForm() {
      this.formData = {
        address: "",
      };
    },
    handlerFilter() {
        this.listQuery.page = 1
        this.getList()
    },
    handlerBtn(btnType, row = {}) {
      if (btnType === "add") {
        this.resetForm();
        this.$nextTick(function () {
          this.$refs["formData"] ? this.$refs["formData"].clearValidate() : "";
        });
      }
      if (btnType === "del") {
        this.delAddress = row.address;
      }
      this.dialogFormVisible = true;
      this.dialogStatus = btnType;
    },
    // 确定按钮回调
    handlerConfirm() {
      if (this.dialogStatus === "add") {
        this.addData();
      }
      if (this.dialogStatus === "del") {
        this.delData();
      }
    },
    // 取消按钮回调
    handlerCancel() {
      this.dialogFormVisible = false;
    },
    addData() {
      this.$refs["formData"].validate((valid) => {
        if (valid && !this.apiLoading) {
          this.apiLoading = true;
          this.$api("user.addwhite", this.formData)
            .then((res) => {
              if (this.$tool.checkResponse(res)) {
                this.successCallback()
                this.getList();
              } else {
                this.failCallback()
              }
            })
            .catch((err) => {
              this.failCallback(err)
            });
        }
      });
    },
    delData() {
      if (!this.apiLoading) {
        this.apiLoading = true;
      } else {
        return;
      }
      this.$api("user.deletewhite", { address: this.delAddress })
        .then((res) => {
          if (this.$tool.checkResponse(res)) {
            this.successCallback()
            let { page, limit } = this.listQuery;
            if (page > 1 && this.total - 1 - (page - 1) * limit <= 0) {
              this.listQuery.page -= 1;
            }
            this.total -= 1;
            this.getList();
          } else {
            this.failCallback()
          }
        })
        .catch((err) => {
          this.failCallback(err)
        });
    },
    // 请求成功回调
    successCallback() {
      this.$notify.success({
        title: this.$t("blind.success"),
        message: this.$t("blind.messageSuccess"),
      });
      this.handlerCancel();
      this.apiLoading = false;
    },
    // 请求失败回调
    failCallback(err) {
        this.$notify.error({
        title: this.$t("blind.fail"),
        message: err
          ? this.$t("response." + err.data.errno)
          : this.$t("blind.messageFail"),
      });
      if(this.dialogStatus == "del") {
        this.handlerCancel()
      }
      this.apiLoading = false;
    },
  },
};
</script>

<style scoped>
.width-200px{
    width: 200px
}
</style>