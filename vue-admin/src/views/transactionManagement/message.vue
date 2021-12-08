<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-date-picker
        class="filter-item ml-50 mr-10"
        v-model="datetime"
        type="datetime"
        :placeholder="$t('dashboard.selectDate')"
        :clearable="false"
        value-format="timestamp"
        @change="dateChange"
      >
      </el-date-picker>
      <el-input
        v-model="listQuery.address"
        clearable
        class="filter-item w-200 mr-10"
        :placeholder="$t('transactionManagement.placeholder')"
      />
      <el-button
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
        >{{ $t("transactionManagement.select") }}</el-button
      >
    </div>

    <!-- 查询结果 -->
    <el-table :data="statistics" border fit highlight-current-row class="mb-20 statistics">
      <el-table-column align="center" :label="$t('transactionManagement.oneDayCounts')" prop="oneDayCounts" />
      <el-table-column align="center" :label="$t('transactionManagement.userCounts')" prop="userCounts" />
      <el-table-column align="center" :label="$t('transactionManagement.accumulatedMoney')" prop="accumulatedMoney" />
    </el-table>
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
        :label="$t('transactionManagement.seller_address')"
        prop="from"
      />
      <el-table-column
        align="center"
        :label="$t('transactionManagement.buyer_address')"
        prop="to"
        sortable
      />
      <el-table-column
        align="center"
        :label="$t('transactionManagement.sellTokenAddress')"
        prop="sellToken"
      />
      <el-table-column
        align="center"
        :label="$t('transactionManagement.buyerTokenAddress')"
        prop="buyerToken"
      />
      <!-- <el-table-column
        align="center"
        label="24小时交易信息"
        prop="accumulatedMoney"
      >
        <el-table-column label="交易量">

        </el-table-column>
        <el-table-column label="交易用户数">
          
        </el-table-column>
        <el-table-column label="交易金额">
          
        </el-table-column>
      </el-table-column> -->
      <el-table-column
        align="center"
        :label="$t('transactionManagement.transactionCurrency')"
      >
        <template slot-scope="scope">
          <span>{{
            scope.row.type == 4 ? scope.row.buyerToken : scope.row.sellToken
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('transactionManagement.dealTime')"
        prop="createTime"
      >
        <template slot-scope="scope">
          <span>{{ formatDate(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>
// import { listOrder } from "@/api/order";
import Pagination from "@/components/Pagination";

export default {
  name: "User",
  components: { Pagination },
  data() {
    return {
      statistics: [{oneDayCounts: 0, userCounts: 0, accumulatedMoney: 0}],
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        address: undefined,
        mobile: undefined,
        sort: "create_time",
        order: "desc",
        staTime: parseInt(new Date().getTime() / 1000),
      },
      datetime: new Date().getTime(),
    };
  },
  computed: {
    formatDate() {
      return function (createTime) {
        createTime= new Date(createTime * 1000);
        let create_date = createTime.toLocaleDateString();
        let create_time = createTime.toTimeString().slice(0, 8);
        return create_date + " " + create_time
      };
    },
  },
  created() {
    this.getList();
    this.statisticsList();
  },
  methods: {
    statisticsList() {
      this.$api('order.transaction', {staTime: this.listQuery.staTime}).then((res) => {
          if(this.$tool.checkResponse(res)) {
            this.statistics = [res.data];
          }
        })
    },
    getList() {
      this.listLoading = true;
      this.$api("order.list", this.listQuery)
        .then((response) => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.listLoading = false;
        })
        .catch((err) => {
          this.list = [];
          this.total = 0;
          this.listLoading = false;
        });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
      this.statisticsList()
    },
    dateChange(val) {
      this.datetime = val;
      this.listQuery.staTime = parseInt(this.datetime / 1000);
      this.getList();
      this.statisticsList()
    },
  },
};
</script>

<style lang="scss" scoped>
  .statistics {
    >>> th {
      background-color: #f5f7fa;
    }
  }
</style>>
