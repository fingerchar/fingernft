<template>
  <div>
    <div :id="ids" class="myChart"></div>
  </div>
</template>

<script>
export default {
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
      this.myChart = this.$echarts.init(document.getElementById(this.ids));
      this.myChart.setOption({
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)",
        },
        legend: {
          y: 20,
          left: "center",
        },
        label: {
          formatter: " {b|{b}：} {per|{d}%}",
          backgroundColor: "#F6F8FC",
          borderColor: "#8C8D8E",
          borderWidth: 1,
          borderRadius: 4,

          rich: {
            a: {
              color: "#6E7079",
              lineHeight: 22,
              align: "center",
            },
            hr: {
              borderColor: "#8C8D8E",
              width: "100%",
              borderWidth: 1,
              height: 0,
            },
            b: {
              color: "#4C5058",
              fontSize: 14,
              fontWeight: "bold",
              lineHeight: 33,
            },
            per: {
              color: "#fff",
              backgroundColor: "#4C5058",
              padding: [3, 4],
              borderRadius: 4,
            },
          },
        },
        series: [
          {
            name: " ",
            type: "pie",
            radius: "50%",
            data: [
              { value: 1048, name: "0.001~0.01" },
              { value: 735, name: "0.01~0.1" },
              { value: 580, name: "0.1~1" },
              { value: 484, name: "1~10" },
              { value: 300, name: "10~20" },
              { value: 735, name: "20~30" },
              { value: 580, name: "30~40" },
              { value: 484, name: "40~50" },
              { value: 300, name: "50~60" },
            ],
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
  height: 450px;
  border: 1px solid #e3e3e3;
  margin-bottom: 20px;
}
</style>
