<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        clearable
        class="filter-item width-200px mr-10"
        :placeholder="$t('systemManagement.placeholder2')"
      />
      <el-button
        v-permission="['GET /admin/log/list']"
        class="filter-item"
        type="primary"
        @click="handleFilter"
        ><el-icon><Search /></el-icon
        >{{ $t("systemManagement.select") }}</el-button
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
        :label="$t('systemManagement.operationsAdministrator')"
        prop="admin"
      />
      <el-table-column
        align="center"
        :label="$t('systemManagement.ipAddress')"
        prop="ip"
      />
      <el-table-column
        align="center"
        :label="$t('systemManagement.operatingTime')"
        prop="createTime"
      />
      <el-table-column
        align="center"
        :label="$t('systemManagement.operatingClass')"
        prop="type"
      >
        <template #default="scope">
          <!-- <el-tag>{{ scope.row.type | $t(typeFilter[scope.row.type]) }}</el-tag> -->
          <el-tag>{{ typeFilter(scope.row.type) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('systemManagement.operationAction')"
        prop="action"
      >
        <template #default="scope">
          {{ $t("systemManagement." + scope.row.action) }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('systemManagement.operatingState')"
        prop="status"
      >
        <template #default="scope">
          <el-tag :type="scope.row.status ? 'success' : 'danger'">
            {{
              scope.row.status ? $t("util.success") : $t("util.fail")
            }}</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('systemManagement.operationResult')"
        prop="result"
      />
      <el-table-column
        align="center"
        :label="$t('systemManagement.noteInformation')"
        prop="comment"
      />
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="listQuery.page"
      v-model:limit="listQuery.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>
  //  import { listLog } from '@/api/log'
  import Pagination from "@/components/Pagination";
  import i18n from "@/i18n/i18n.js";
  const typeMap = {
    0: "systemManagement.generalOperation",
    1: "systemManagement.safeOperation",
    2: "systemManagement.orderOperation",
    3: "systemManagement.otherOperations",
  };
  export default {
    name: "Log",
    components: { Pagination },
    data() {
      return {
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          name: undefined,
          sort: "create_time",
          order: "desc",
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
      };
    },
    created() {
      this.getList();
    },
    methods: {
      typeFilter(type) {
        return i18n.t(typeMap[type]);
      },
      getList() {
        this.listLoading = true;
        // listLog(this.listQuery)
        //   .then(response => {
        //     this.list = response.data.list
        //     this.total = response.data.total
        //     this.listLoading = false
        //   })
        //   .catch(() => {
        //     this.list = []
        //     this.total = 0
        //     this.listLoading = false
        //   })
        this.$api("log.list", this.listQuery)
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
    },
  };
</script>
<style scoped>
  .width-200px {
    width: 200px;
  }
</style>
