<template>
  <div>
    <div :id="ids" class="myChart"></div>
  </div>
</template>

<script>
export default {
  props: {
    xData: {
      type: Array,
      default: [],
    },
    title: {
      type: String,
      default: "",
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
          left: "left",
        },
        tooltip: {
          trigger: "axis",
        },
        xAxis: {
          type: "category",
          data: this.xData,
        },
        yAxis: {
          type: "value",
        },
        legend: {
          y: 370,
          data: ["1", "2", "3", "4", "5"],
        },
        series: [
          {
            name: "1",
            type: "line",
            data: [
              120,
              132,
              101,
              134,
              90,
              230,
              210,
              120,
              132,
              101,
              134,
              90,
              230,
              210,
              120,
              132,
              101,
              134,
              90,
              230,
              210,
            ],
          },
          {
            name: "2",
            type: "line",
            data: [220, 182, 191, 234, 290, 330, 310],
          },
          {
            name: "3",
            type: "line",
            data: [150, 232, 201, 154, 190, 330, 410],
          },
          {
            name: "4",
            type: "line",
            data: [320, 332, 301, 334, 390, 330, 320],
          },
          {
            name: "5",
            type: "line",
            data: [820, 932, 901, 934, 1290, 1330, 1320],
          },
        ],
      });
    },
  },
};
</script>

<style scoped>
.myChart {
  width: 100%;
  height: 300px;
  border: 1px solid #c8c8c8;
  /* margin: 0px auto;
  margin-bottom: 20px; */
}
</style>
