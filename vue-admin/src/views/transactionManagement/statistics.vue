<template>
  <div class="app-container">
    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" :element-loading-text="$t('util.loadingText')" border fit highlight-current-row>
      <el-table-column align="center" :label="$t('transactionManagement.oneDayCounts')" prop="oneDayCounts" />
      <el-table-column align="center" :label="$t('transactionManagement.userCounts')" prop="userCounts" />
      <el-table-column align="center" :label="$t('transactionManagement.accumulatedMoney')" prop="accumulatedMoney" />
    </el-table>
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
      listLoading: true,
      query:{
        // oneDayTransaction:true,
        // transactionUser:true,
        // transactionAmount:true,
        // order:''
      },
      list:[]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {

      this.listLoading = true;
      this.$api('order.transaction').then((res) => {
        console.log(res)
          this.list=this.list.concat(res.data);
          this.listLoading = false;
        })
    },

  },
};
</script>
