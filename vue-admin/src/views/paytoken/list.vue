<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">{{$t('paytoken.create')}}</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" :element-loading-text="$t('payload.loadingText')" border fit highlight-current-row>
      <el-table-column align="center" label="Id" prop="id" sortable />
      <el-table-column align="center" :label="$t('paytoken.name')" prop="name" />
      <el-table-column align="center" :label="$t('paytoken.symbol')" prop="symbol" />
      <el-table-column align="center" :label="$t('paytoken.decimals')" prop="decimals" />
      <el-table-column align="center" :label="$t('paytoken.type')" prop="type" >
        <template slot-scope="scope">
          {{ getTokenType(scope.row.type) }}
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('paytoken.address')" prop="address" />
      <el-table-column align="center" :label="$t('paytoken.default')" >
        <template slot-scope="scope">
          <el-tag type="info" v-if="!scope.row.isDefault">{{$t('paytoken.no')}}</el-tag>
          <el-tag type="success" v-else>{{$t('paytoken.yes')}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('paytoken.operate')" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{$t('paytoken.edit')}}</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)" v-if="!scope.row.deleted">{{$t('paytoken.delete')}}</el-button>
        </template>
      </el-table-column>

    </el-table>


    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="120px" >

        <el-form-item :label="$t('paytoken.name')" prop="name">
          <el-input v-model="dataForm.name" />
        </el-form-item>

        <el-form-item :label="$t('paytoken.symbol')" prop="symbol">
          <el-input v-model="dataForm.symbol" />
        </el-form-item>

        <el-form-item :label="$t('paytoken.decimals')" prop="decimals">
          <el-input v-model="dataForm.decimals" />
        </el-form-item>

        <el-form-item :label="$t('paytoken.type')" prop="type">
          <el-select v-model="dataForm.type">
            <el-option
              v-for="(type, i) in types"
              :key="type.value"
              :label="type.name"
              :value="type.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('paytoken.address')" prop="address">
          <el-input v-model="dataForm.address" />
        </el-form-item>

        <el-form-item :label="$t('paytoken.default')" prop="isDefault" v-if="dataForm.type != 0">
          <el-checkbox v-model="dataForm.isDefault" :label="$t('paytoken.default')"></el-checkbox>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer" v-if="dialogStatus!='detail' ">
        <el-button @click="dialogFormVisible = false">{{$t('global.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('global.confirm')}}</el-button>
        <el-button v-else type="primary" @click="updateData">{{$t('global.confirm')}}</el-button>
      </div>
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
      listLoading: true,
      types: [
        { value: 0, name: "ETH" },
        { value: 1, name: "ERC20" },
      ],
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
      dataForm: {},
      rules: {
        name: [ { required: true, message: this.$t('paytoken.emptyName'), trigger: "blur" } ],
        symbol: [ { required: true, message: this.$t('paytoken.emptySymbol'), trigger: "blur" } ],
        decimals: [ { required: true, message: this.$t('paytoken.emptyDecimals'), trigger: "blur" } ],
        address: [ { required: true, message: this.$t('paytoken.emptyAddress'), trigger: "blur" } ],
      },
      textMap: {
        update: this.$t('global.edit'),
        detail: this.$t('global.detail'),
        create: this.$t('global.create'),
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getTokenType(type){
      for(var i = 0; i < this.types.length; i++){
        if(type == this.types[i].value){
          return this.types[i].name;
        }
      }
    },
    getList() {
      this.listLoading = true;
      this.$api("paytoken.list", this.listQuery).then((res) => {
        this.list = res.data.list;
        this.listLoading = false;
      }).catch((err) => {
        this.list = [];
        this.listLoading = false;
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    resetForm() {
      this.dataForm = {};
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
          var data = Object.assign({}, this.dataForm);
          data.isDefault = !data.isDefault ? 0: 1;
          if(data.type == 0) data.isDefault = 0;

          this.$api('paytoken.create', data)
            .then((res) => {
              if(this.$tool.checkResponse(res)){
                this.dialogFormVisible = false;
                this.$notify.success({
                  title: this.$t('global.success'),
                  message: this.$t('global.createSuccess'),
                });
                this.getList();
              }else{
                this.$notify.error({
                  title: this.$t('global.createFail'),
                  message: res.errmsg,
                });
              }
            })
            .catch((res) => {
              this.$notify.error({
                title: this.$t('global.createFail'),
                message:this.$t('response.' + res.data.errno)
              });
            });
        }
      });
    },
    handleUpdate(row) {
      this.dataForm = Object.assign({}, row);
      this.dataForm.isDefault = this.dataForm.isDefault == 1 ? true : false;
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    updateData() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          var data = {
            id: this.dataForm.id,
            name: this.dataForm.name,
            symbol: this.dataForm.symbol,
            decimals: this.dataForm.decimals,
            type: this.dataForm.type,
            address: this.dataForm.address,
            isDefault: !this.dataForm.isDefault ? 0 : 1,
          };
          this.$api('paytoken.update', data)
            .then((res) => {
              if(this.$tool.checkResponse(res)){
                this.dialogFormVisible = false;
                this.$notify.success({
                  title: this.$t('global.success'),
                  message: this.$t('global.editSuccess'),
                });
                this.getList();
              }else{
                this.$notify.error({
                  title: this.$t('global.fail'),
                  message: res.errmsg
                });
              }
            })
            .catch((res) => {
              this.$notify.error({
                title: this.$t('global.fail'),
                message:this.$t('response.'+ res.data.errno)
              });
            });
        }
      });
    },
    handleDelete(row) {
      this.$confirm(this.$t('global.deleteTip'), this.$t('global.tip'), {
        confirmButtonText: this.$t('global.confirm'),
        cancelButtonText: this.$t('global.cancel'),
        type: "warning",
      })
        .then(() => {
          this.$api('paytoken.delete',{ address: row.address})
            .then((res) => {
              if(this.$tool.checkResponse(res)){
                this.$notify.success({
                  title: this.$t('global.success'),
                  message: this.$t('global.deleteSuccess'),
                });
                this.getList();
              }else{
                this.$notify.error({
                  message: this.$t('global.deleteFail'),
                });
              }
            })
            .catch((res) => {
              this.$notify.error({
                title: this.$t('global.deleteFail'),
                message:this.$t('response.' + res.data.errno)
              });
            });
        })
        .catch(() => {});
    },
  },
};
</script>
<style lang="scss" scoped>
  .dataFormClass{
   width: 400px;
   margin-left:50px;
  }
</style>
