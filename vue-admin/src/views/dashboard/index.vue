<template>
  <div class="dashboard-editor-container">
    <!-- <el-row>
      <el-tag type="success">欢迎，{{ name }}-{{ roles }}</el-tag>
    </el-row> -->
    <div class="head-container">
      <el-date-picker
        class="ml-50"
        v-model="datetime"
        type="datetime"
        :placeholder="$t('dashboard.selectDate')"
        :clearable="false"
        value-format="timestamp"
        @change="dateChange"
      >
      </el-date-picker>
      <i class="el-icon-download download"></i>
    </div>
    <div class="line"></div>
    <li>{{ $t("dashboard.userData") }}</li>
    <div class="ml-40 mb-20">
      <div class="title-text">{{ $t("dashboard.activeUser") }}:</div>
      <el-table :data="userActive" border>
        <el-table-column prop="today" :label="$t('dashboard.todayLogin')">
        </el-table-column>
        <el-table-column
          prop="yesterday"
          :label="$t('dashboard.yesterdayLogin')"
        >
        </el-table-column>
        <el-table-column prop="growth" :label="$t('dashboard.growth')">
        </el-table-column>
      </el-table>
      <div class="title-text">{{ $t("dashboard.userAdd") }}:</div>
      <el-table :data="userCreate" border>
        <el-table-column prop="todayAdd" :label="$t('dashboard.todayAdd')">
        </el-table-column>
        <el-table-column prop="beforeAdd" :label="$t('dashboard.yesterdayAdd')">
        </el-table-column>
        <el-table-column
          prop="growth"
          :label="$t('dashboard.growth')"
        ></el-table-column>
      </el-table>
    </div>
    <li>{{ $t("dashboard.revenueData") }}</li>
    <div class="ml-40 mb-20">
      <div class="title-text">{{ $t("dashboard.statement") }}</div>
      <div class="revenue-container">
        <div class="revenue">
          {{ $t("dashboard.totalFlow") }}：<span class="red">{{
            totalMoney
          }}</span
          >ETH
        </div>
        <div class="revenue">
          {{ $t("dashboard.platform") }}：<span>{{ totalPlatform }}</span
          >ETH
        </div>
        <div class="revenue">
          {{ $t("dashboard.profit") }}：<span class="green">{{
            totalProfit
          }}</span
          >ETH
        </div>
        <div class="revenue">
          {{ $t("dashboard.gas") }}：<span class="red">{{ totalGas }}</span
          >ETH
        </div>
      </div>
    </div>
    <el-table :data="revenue" border>
      <el-table-column prop="name" :label="$t('dashboard.nftName')">
      </el-table-column>
      <el-table-column prop="address" :label="$t('dashboard.nftType')">
      </el-table-column>
      <el-table-column prop="salesVolume" :label="$t('dashboard.sales')">
      </el-table-column>
      <el-table-column prop="sumMoney" :label="$t('dashboard.totalMoney')">
      </el-table-column>
      <el-table-column
        prop="platformMoney"
        :label="$t('dashboard.platform')"
      ></el-table-column>
      <el-table-column
        prop="nowProfit"
        :label="$t('dashboard.profit')"
      ></el-table-column>
      <el-table-column
        prop="gasMoney"
        :label="$t('dashboard.gas')"
      ></el-table-column>
    </el-table>
    <div class="ml-40 mb-20">
      <div class="title-text">{{ $t("dashboard.information") }}:</div>
      <div class="ml-40 p-5 red">{{$t("dashboard.information1")}}</div>
      <div class="ml-40 p-5 red">{{$t("dashboard.information2")}}</div>
    </div>
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <chart-pie
          v-if="revenue.length > 0"
          :title="$t('dashboard.profitRatio')"
          :data="chartProfit"
        ></chart-pie>
        <chart-pie v-else :title="$t('dashboard.profitRatio')"></chart-pie>
      </el-col>
      <el-col :span="12">
        <chart-pie
          v-if="revenue.length > 0"
          :title="$t('dashboard.salesRatio')"
          :data="chartSalesVolume"
        ></chart-pie>
        <chart-pie v-else :title="$t('dashboard.salesRatio')"></chart-pie>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <chart-bar
          v-if="revenue.length > 0"
          :title="$t('dashboard.salesContrast')"
          :legendData="contrastSalesVolumeLegend"
          :xData="xRevenue"
          :yBefore="ySalesVolumeBefore"
          :yAfter="ySalesVolumeAfter"
          type="line"
        ></chart-bar>
        <chart-bar
          :title="$t('dashboard.salesContrast')"
          :legendData="contrastSalesVolumeLegend"
          v-else
        ></chart-bar>
      </el-col>
      <el-col :span="12">
        <chart-bar
          v-if="revenue.length > 0"
          :title="$t('dashboard.moneyContrast')"
          :legendData="contrastSumMoneyLegend"
          :xData="xRevenue"
          :yBefore="ySumMoneyBefore"
          :yAfter="ySumMoneyAfter"
          type="line"
        ></chart-bar>
        <chart-bar
          :title="$t('dashboard.moneyContrast')"
          :legendData="contrastSumMoneyLegend"
          type="line"
          v-else
        ></chart-bar>
      </el-col>
    </el-row>
    <li>{{ $t("dashboard.nftManagement") }}</li>
    <div class="revenue-container ml-40 mb-20">
      <div class="revenue">{{ $t("dashboard.totalNft") }}：{{ countNFT }}</div>
      <div class="revenue">
        {{ $t("dashboard.addNft") }}：<span class="green">{{
          newlyAddedNFT
        }}</span>
      </div>
      <div class="revenue">
        {{ $t("dashboard.changeHandsNft") }}：<span class="green">{{
          changeHandsNFT
        }}</span>
      </div>
      <div class="revenue">
        {{ $t("dashboard.reviewedNft") }}：<span class="green">{{
          reviewedNFT
        }}</span>
      </div>
    </div>
    <el-table :data="NFT" border class="mb-20">
      <el-table-column prop="name" :label="$t('dashboard.nftName')">
      </el-table-column>
      <el-table-column prop="address" :label="$t('dashboard.nftType')">
      </el-table-column>
      <el-table-column prop="countNft" :label="$t('dashboard.count')">
      </el-table-column>
      <el-table-column prop="reviewedNft" :label="$t('dashboard.reviewedNft')">
      </el-table-column>
      <el-table-column prop="newlyAddedNft" :label="$t('dashboard.addNft')">
      </el-table-column>
      <el-table-column
        prop="changeHandsNft"
        :label="$t('dashboard.changeHandsNft')"
      >
      </el-table-column>
    </el-table>

    <div class="title-text">{{ $t("dashboard.addNftContrast") }}</div>
    <el-row :gutter="20">
      <el-col :span="12">
        <chart-bar
          v-if="NFT.length > 0"
          :title="$t('dashboard.addNftContrast')"
          :legendData="contrastNewlyAddedNftLegend"
          :xData="xNFT"
          :yBefore="yNewlyAddedNftBefore"
          :yAfter="yNewlyAddedNftAfter"
        ></chart-bar>
        <chart-bar
          :title="$t('dashboard.addNftContrast')"
          :legendData="contrastNewlyAddedNftLegend"
          v-else
        ></chart-bar>
      </el-col>
      <el-col :span="12">
        <chart-pie
          v-if="NFT.length > 0"
          :title="$t('dashboard.addNftRatio')"
          :data="chartNewlyAddedNft"
        ></chart-pie>
        <chart-pie v-else :title="$t('dashboard.addNftRatio')"></chart-pie>
      </el-col>
    </el-row>

    <div class="title-text">{{ $t("dashboard.changeHandsNftContrast") }}</div>
    <el-row :gutter="20">
      <el-col :span="12">
        <chart-bar
          v-if="NFT.length > 0"
          :title="$t('dashboard.changeHandsNftContrast')"
          :legendData="contrastChangeHandsNftLegend"
          :xData="xNFT"
          :yBefore="yChangeHandsNftBefore"
          :yAfter="yChangeHandsNftAfter"
        ></chart-bar>
        <chart-bar
          :title="$t('dashboard.changeHandsNftContrast')"
          :legendData="contrastChangeHandsNftLegend"
          v-else
        ></chart-bar>
      </el-col>
      <el-col :span="12">
        <chart-pie
          v-if="NFT.length > 0"
          :title="$t('dashboard.changeHandsNftRatio')"
          :data="chartChangeHandsNft"
        ></chart-pie>
        <chart-pie v-else :title="$t('dashboard.changeHandsNftRatio')"></chart-pie>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ChartPie from "@/components/tool/chartPie.vue";
import ChartBar from "@/components/tool/chartBar.vue";
import rankingChart from "@/components/tool/rankingChart.vue";


export default {
  components: { ChartPie, ChartBar, rankingChart },
  data() {
    return {
      datetime: new Date().getTime(),
      userActive: [],
      userCreate: [],
      // 营收数据
      revenue: [],
      // 营收数据--总计
      totalMoney: 0,
      totalPlatform: 0,
      totalProfit: 0,
      totalGas: 0,
      // 营收数据--利润占比
      chartProfit: [],
      // 营收数据--销售量占比
      chartSalesVolume: [],
      // 营收数据--销量对比
      xRevenue: [],
      contrastSalesVolumeLegend: [
        "dashboard.yesterdaySales",
        "dashboard.todaySales",
      ],
      ySalesVolumeBefore: [],
      ySalesVolumeAfter: [],
      // 营收数据--销售额对比
      contrastSumMoneyLegend: [
        "dashboard.yesterdayMoney",
        "dashboard.todayMoney",
      ],
      ySumMoneyBefore: [],
      ySumMoneyAfter: [],
      // NFT管理
      NFT: [],
      // NFT管理--总计
      countNFT: 0,
      newlyAddedNFT: 0,
      changeHandsNFT: 0,
      reviewedNFT: 0,
      // NFT管理--新增nft占比
      chartNewlyAddedNft: [],
      // NFT管理--转手nft占比
      chartChangeHandsNft: [],
      // NFT管理--新增nft对比
      xNFT: [],
      contrastNewlyAddedNftLegend: [
        "dashboard.yesterdayAdd",
        "dashboard.todayAdd",
      ],
      yNewlyAddedNftBefore: [],
      yNewlyAddedNftAfter: [],
      // NFT管理--转手nft对比
      contrastChangeHandsNftLegend: [
        "dashboard.yesterdayChangeHands",
        "dashboard.todayChangeHands",
      ],
      yChangeHandsNftBefore: [],
      yChangeHandsNftAfter: [],
      // 排行榜
      rankData: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.$api("statistics.list", {
        staTime: parseInt(this.datetime / 1000),
      }).then((res) => {
        if (this.$tool.checkResponse(res)) {
          this.formatUserActive(res.data.staUserList);
          this.formatUserCreate(res.data.newCreateList);
          this.formatRevenue(res.data.staNftDealList, res.data.contrastNftDeal);
          this.formatNFT(res.data.staNftList, res.data.contrastNft);
        }
      })
    },
    increaseRate(prev, cur) {
      return {
        increase: cur - prev,
        rate: prev == 0 ? 0 : ((cur - prev) / prev) * 100,
      };
    },
    formatUserActive(users) {
      let { yesterday, today } = users;
      let { increase, rate } = this.increaseRate(yesterday, today),
        growth = `${increase}(${rate}%)`;
      this.userActive = [{ yesterday, today, growth }];
    },
    formatUserCreate(users) {
      let { beforeAdd, todayAdd } = users;
      let { increase, rate } = this.increaseRate(beforeAdd, todayAdd),
        growth = `${increase}(${rate}%)`;
      this.userCreate = [{ beforeAdd, todayAdd, growth }];
    },
    formatRevenue(data, contrasData) {
      this.resetRevenue();
      if (data.length <= 0) return;
      for (let i = 0; i < data.length; i++) {
        if (i < 10) {
          this.revenue.push(data[i]);
          this.chartProfit.push({
            value: data[i].nowProfit,
            name: data[i].name + "-" + i,
          });
          this.chartSalesVolume.push({
            value: data[i].salesVolume,
            name: data[i].name + "-" + i,
          });
          this.xRevenue.push(data[i].name + "-" + i);
          this.ySalesVolumeAfter.push(data[i].salesVolume);
          this.ySumMoneyAfter.push(data[i].sumMoney);
        }
        this.ySalesVolumeBefore = contrasData.yesterdaySalesVolume;
        this.ySumMoneyBefore = contrasData.yesterdaySumMoney;
        this.totalMoney += data[i].sumMoney;
        this.totalPlatform += data[i].platformMoney;
        this.totalProfit += data[i].nowProfit;
        this.totalGas += data[i].gasMoney;
      }
    },
    formatNFT(data, contrasData) {
      this.resetNFT();
      if (data.length <= 0) return;
      for (let i = 0; i < data.length; i++) {
        if (i < 10) {
          this.NFT.push(data[i]);
          this.chartNewlyAddedNft.push({
            value: data[i].newlyAddedNft,
            name: data[i].name + "-" + i,
          });
          this.chartChangeHandsNft.push({
            value: data[i].changeHandsNft,
            name: data[i].address + "-" + i,
          });
          this.xNFT.push(data[i].name + "-" + i);
          this.yNewlyAddedNftAfter.push(data[i].newlyAddedNft);
          this.yChangeHandsNftAfter.push(data[i].changeHandsNft);
        }
        this.yNewlyAddedNftBefore = contrasData.yesterdayNewlyAddedNft;
        this.yChangeHandsNftBefore = contrasData.yesterdayChangeHandsNft;
        this.countNFT += data[i].countNft;
        this.newlyAddedNFT += data[i].newlyAddedNft;
        this.changeHandsNFT += data[i].changeHandsNft;
        this.reviewedNFT += data[i].reviewedNft;
      }
    },
    dateChange(val) {
      this.datetime = val;
      this.getList();
    },
    resetRevenue() {
      this.revenue = [];
      this.totalMoney = 0;
      this.totalPlatform = 0;
      this.totalProfit = 0;
      this.totalGas = 0;
      this.chartProfit = [];
      this.chartSalesVolume = [];
      this.xRevenue = [];
      this.ySalesVolumeBefore = [];
      this.ySalesVolumeAfter = [];
      this.ySumMoneyBefore = [];
      this.ySumMoneyAfter = [];
    },
    resetNFT() {
      this.NFT = [];
      this.countNFT = 0;
      this.newlyAddedNFT = 0;
      this.changeHandsNFT = 0;
      this.reviewedNFT = 0;
      this.chartNewlyAddedNft = [];
      this.chartChangeHandsNft = [];
      this.xNFT = [];
      this.yNewlyAddedNftBefore = [];
      this.yNewlyAddedNftAfter = [];
      this.yChangeHandsNftBefore = [];
      this.yChangeHandsNftAfter = [];
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
#myChart4 {
  width: 50%;
  height: 300px;
  border: 1px solid #c8c8c8;
}
.read-container {
  position: relative;
}

.red {
  color: #da4947;
}
.dashboard-editor-container {
  padding: 32px;
}
.head-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.download {
  font-size: 26px;
  color: #0f0f0f;
}
.ml-50 {
  margin-left: 50px;
}
.ml-40 {
  margin-left: 40px;
}
.mb-20 {
  margin-bottom: 20px;
}
.p-5 {
  padding: 5px 0px;
}
.line {
  height: 1px;
  width: 100%;
  background-color: #cacaca;
  margin: 20px 0px;
}
.title-text {
  margin: 20px 0px 10px 0px;
}
.green {
  color: #15e345;
}

.revenue {
  width: 50%;
  padding: 5px 0px;
}
.revenue-container {
  display: flex;
  flex-wrap: wrap;
}
</style>
