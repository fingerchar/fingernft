<template>
  <div>
    <div :id="ids" class="myChart"></div>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: "",
    },
    legendDtate: {
      type: Array,
      default: () => [],
    },
    model: {
      type: String,
      default: "",
    },
  },
  computed: {
    ids() {
      return Math.random() * 100000;
    },
  },
  data() {
    return {
      myChart: "",
    };
  },
  created() {
    this.$nextTick(() => {
      this.drawLine1();
    });
  },
  mounted: function () {
    let _this = this;
    window.addEventListener("resize", function () {
      _this.myChart.resize();
    });
  },
  methods: {
    drawLine1() {
      this.myChart = this.$echarts.init(
        document.getElementById(this.ids),
        this.model
      );
      this.myChart.setOption({
        title: {
          text: this.title,
          left: "center",
        },
        legend: {
          y: 265,
          data: this.legendDtate,
        },
        xAxis: {
          type: "category",
          data: [
            "art",
            "photo",
            "game",
            "meta",
            "music",
            "doma",
            "defi",
            "memes",
          ],
          axisLabel: {
            interval: 0,
          },
        },
        yAxis: { type: "value" },
        series: [
          {
            name: this.legendDtate[0],
            data: [10000, 5000, 3000, 4000, 5000, 3000, 2500, 5000],
            type: "bar",
            label: {
              show: true,
              position: "top",
            },
            color: "#467CC2",
          },
          {
            name: this.legendDtate[1],
            data: [9000, 4000, 2000, 3000, 4000, 2000, 1500, 4000],
            type: "line",
            label: {
              show: true,
              position: "top",
            },
            color: "#CE4442",
          },
        ],
      });
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.myChart {
  width: 100%;
  height: 300px;
  border: 1px solid #c8c8c8;
}
</style>
