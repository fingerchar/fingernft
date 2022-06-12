<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <li class="mb-20">{{ $t("userData.title") }}</li>
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        clearable
        class="filter-item w-200 mr-10"
        :placeholder="$t('userData.placeholder1')"
      />
      <el-input
        v-model="listQuery.address"
        clearable
        class="filter-item w-200 mr-10"
        :placeholder="$t('userData.placeholder2')"
      />
      <el-select
        v-model="listQuery.verify"
        class="filter-item w-200 mr-10"
        :placeholder="$t('userData.placeholder3')"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>

      <!-- 登录时间
      <el-date-picker class="picker" v-model="value1" type="daterange" range-separator="至"
        start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker> -->

      <el-button class="filter-item mt-10" type="primary" @click="handleFilter"
        ><el-icon style="vertical-align: middle"> <Search /> </el-icon
        >{{ $t("userData.select") }}</el-button
      >
    </div>

    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      :element-loading-text="$t('global.loadingText')"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        align="center"
        :label="$t('userData.address')"
        prop="address"
      />
      <el-table-column
        align="center"
        :label="$t('userData.nickname')"
        prop="nickname"
      />
      <el-table-column
        align="center"
        :label="$t('userData.avatar')"
        prop="avatar"
      >
        <template #default="scope">
          <media
            v-if="scope.row.avatar"
            :isPreview="true"
            :url="scope.row.avatar"
            type="image"
          ></media>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('userData.certificationStatus')"
        prop="userVerify"
      >
        <template #default="scope">
          <el-tag
            v-if="scope.row.userVerify == 0 || !scope.row.userVerify"
            type="info"
            >{{ $t("userData.unauthenticated") }}</el-tag
          >
          <el-tag v-if="scope.row.userVerify == 1" type="success">{{
            $t("userData.certificationPassed")
          }}</el-tag>
          <el-tag v-if="scope.row.userVerify == 2" type="danger">{{
            $t("userData.authenticationFailed")
          }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        :label="$t('userData.operation')"
        width="250"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            type="primary"
            size="small"
            @click="handleDetail(scope.row)"
            >{{ $t("userData.details") }}</el-button
          >
          <el-button
            type="success"
            size="small"
            class="widthFit"
            v-permission="['POST /admin/user/verify']"
            @click="(verifyForm = true), (activeId = scope.row.id)"
            v-if="scope.row.userVerify != 1"
          >
            {{ $t("userData.immediateCertification") }}</el-button
          >
          <el-button
            type="success"
            size="small"
            class="widthFit"
            v-permission="['POST /admin/user/verify']"
            @click="handleCertification(scope.row)"
            v-else
            >{{ $t("userData.cancelCertification") }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      :total="total"
      v-model:page="listQuery.page"
      v-model:limit="listQuery.limit"
      @pagination="getList"
    />

    <!-- 对话框 -->
    <el-dialog :title="$t('userData.details')" v-model="dialogFormVisible">
      <el-descriptions
        class="margin-top"
        direction="vertical"
        :column="3"
        border
      >
        <el-descriptions-item>
          <template v-slot:label>
            <div class="centerClass">{{ $t("userData.address") }}</div>
          </template>
          <div class="centerClass">
            {{ details.address }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <div class="centerClass">{{ $t("userData.nickname") }}</div>
          </template>
          <div class="centerClass">
            {{ details.nickname }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <div class="centerClass">{{ $t("userData.avatar") }}</div>
          </template>
          <div class="centerClass">
            {{ details.avatar }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template v-slot:label>
            <div class="centerClass">
              <div class="centerClass">
                {{ $t("userData.certificationStatus") }}
              </div>
            </div>
          </template>
          <div class="centerClass">
            <el-tag v-if="details.userVerify == 0" type="info">{{
              $t("userData.unauthenticated")
            }}</el-tag>
            <el-tag v-if="details.userVerify == 1" type="success">{{
              $t("userData.certificationPassed")
            }}</el-tag>
            <el-tag v-if="details.userVerify == 2" type="danger">{{
              $t("userData.authenticationFailed")
            }}</el-tag>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    <el-dialog :title="$t('userData.authentication')" v-model="verifyForm">
      <el-radio v-model="verifyStatus" :label="1">{{
        $t("userData.certificationPassed")
      }}</el-radio>
      <el-radio v-model="verifyStatus" :label="2">{{
        $t("userData.authenticationFailed")
      }}</el-radio>
      <br />
      <div class="verifyClass">
        <el-button type="info" size="small" @click="verifyForm = false">{{
          $t("userData.cancel")
        }}</el-button>
        <el-button type="success" size="small" @click="verifyFunction">{{
          $t("userData.confirm")
        }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import Pagination from "@/components/Pagination";
  import Media from "@/components/Media";
  export default {
    components: { Media, Pagination },
    data() {
      return {
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: this.$store.state.app.setting.limit,
          name: undefined,
          address: undefined,
          verify: undefined,
          start: undefined,
          end: undefined,
          sort: "create_time",
          order: "desc",
        },
        downloadLoading: false,
        dialogFormVisible: false,
        details: {},
        value1: "",
        options: [
          {
            value: 0,
            label: this.$t("userData.unauthenticated"),
          },
          {
            value: 1,
            label: this.$t("userData.authenticated"),
          },
          {
            value: 2,
            label: this.$t("userData.authenticationFailed"),
          },
        ],
        verifyForm: false,
        verifyStatus: 1,
        activeId: "",
      };
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.listLoading = true;
        this.$api("user.list", this.listQuery)
          .then((response) => {
            if (this.$tool.checkResponse(response)) {
              this.list = response.data.list;
              this.total = response.data.total;
            }
            this.listLoading = false;
          })
          .catch(() => {
            this.list = [];
            this.total = 0;
            this.listLoading = false;
          });
      },
      handleFilter() {
        if (this.value1) {
          this.listQuery.start = this.GMTToStr(this.value1[0]);
          this.listQuery.end = this.GMTToStr(this.value1[1]);
        }
        this.listQuery.page = 1;
        this.getList();
      },
      handleCertification(row) {
        this.$confirm(this.$t("userData.tipContent"), this.$t("userData.tip"), {
          confirmButtonText: this.$t("userData.confirm"),
          cancelButtonText: this.$t("userData.cancel"),
          type: "warning",
        })
          .then(() => {
            this.$api("user.userVerify", { id: row.id, userVerify: 0 })
              .then(() => {
                this.$notify.success({
                  title: this.$t("authentication.success"),
                  message: this.$t("authentication.completed"),
                });
                this.getList();
              })
              .catch((response) => {
                this.$notify.error({
                  title: this.$t("authentication.fail"),
                  message: this.$t("response." + response.data.errno),
                });
              });
          })
          .catch(() => {});
      },
      verifyFunction() {
        this.listLoading = true;
        this.$api("user.userVerify", {
          id: this.activeId,
          userVerify: this.verifyStatus,
        })
          .then(() => {
            this.getList();
            this.verifyForm = false;
            this.$notify.success({
              title: this.$t("authentication.success"),
              message: this.$t("authentication.completed"),
            });
          })
          .catch((response) => {
            this.listLoading = false;
            this.$notify.error({
              title: this.$t("authentication.fail"),
              message: this.$t("response." + response.data.errno),
            });
          });
      },
      handleDetail(row) {
        // window.open('http://testnet.yuumi.io/account/0x80dd336deb7eea1923780a7383c806870c9ce27d');

        this.details = row;

        this.dialogFormVisible = true;

        // this.$api('user.userDetail',{ id: row.address })
        //   .then((response) => {
        //     this.details = response.data;
        //     this.dialogFormVisible = true;
        //   })
        //   .catch((response) => {
        //     this.$notify.error({
        //       title: "存在错误",
        //       message: response.data.errmsg,
        //     });
        //   });
      },
      // 时间转化
      GMTToStr(time) {
        let date = new Date(time);
        let month = date.getMonth() + 1;
        let datas = date.getDate();
        if (month < 10) {
          month = "0" + month;
        }
        if (datas < 10) {
          datas = "0" + datas;
        }
        let Str = date.getFullYear() + "-" + month + "-" + datas;
        return Str;
      },
    },
  };
</script>

<style lang="scss" scoped>
  .mb-20 {
    margin-bottom: 20px;
  }
  .table-cell {
    padding: 10px;
    font-size: 14px;
    text-align: center;
    overflow: hidden;
  }
  .title {
    font-style: 16px;
    font-weight: 700;
  }
  .picker {
    margin: 0px 10px;
  }
  .centerClass {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .verifyClass {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }
  .widthFit {
    width: fit-content;
  }
</style>
