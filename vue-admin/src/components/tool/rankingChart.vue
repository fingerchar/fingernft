<template>
  <div>
    <div :id="ids" class="myChart"></div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
export default {
  data() {
    return {
      myChart: ""
    }
  },
  props: {
    randData: {
      type: Array,
      default: () => []
    },
    xData: {
      type: Array,
      default: () => [],
    },
    yData: {
      type: Array,
      default: () => [],
    }
  },
  computed: {
    ...mapGetters(['language']),
    ids() {
      return Math.random() * 100000;
    },
  },
   mounted: function () {
    this.$nextTick(() => {
      this.drawLine()
    });
  },
  methods: {
    drawLine() {
      this.myChart = this.$echarts.init(
        document.getElementById(this.ids)
      );
      let option = {
        title: {
          text: this.$t("dashboard.rankTitle"),
          left: "center",
        },
        legend: {
          y: 30,
          data: [this.$t("dashboard.rankLegend")]
        },
        grid: {
          top: 80
        },
        xAxis: {
          type: 'value',
        },
        yAxis: { 
          type: 'category',
          data: this.yData
        },
        series: [
           {
            name: this.$t("dashboard.rankLegend"),
            type: 'bar',
            data: this.xData
        },
        ],
      };
      this.myChart.setOption(option, true);
      window.addEventListener("resize", () => {
        this.myChart.resize();
      });
    },
  },
  watch: {
    randData() {
      this.drawLine()
    },
    language() {
      this.drawLine()
    }
  },
};
</script>


