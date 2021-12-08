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
      myChart: "",
      legendArr: [],
      legend: [],
    }
  },
  props: {
    title: String,
    type: {
      type: String,
      default: 'bar'
    },
    legendData: {
      type: Array,
      default: () => []
    },
    xData: {
      type: Array,
      default: () => [],
    },
    yBefore: {
      type: Array,
      default: () => [],
    },
    yAfter: {
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
      this.legendArr = [this.legendData[0], this.legendData[1]]
      this.legend = [this.$t(this.legendData[0]), this.$t(this.legendData[1])]
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
          text: this.title,
          left: "center",
        },
        legend: {
          y: 30,
          data: this.legend,
        },
        grid: {
          top: 80
        },
        xAxis: {
          type: "category",
        data: this.xData,
          axisLabel: {
            interval: 0,
          },
        },
        yAxis: { type: "value" },
        series: [
          {
            name: this.legend[0],
            data: this.yBefore,
            type: this.type,
            label: {
              show: true,
              position: "top",
            },
            color: "#467CC2",
            barWidth: this.type ==="bar" ? 30 : ""
          },
          {
            name: this.legend[1],
            data: this.yAfter,
            type: "bar",
            label: {
              show: true,
              position: "top",
            },
            color: "#CE4442",
            barWidth: 30
            
          },
        ],
      }
      this.myChart.setOption(option, true);
      window.addEventListener("resize", () => {
        this.myChart.resize();
      });
    },
  },
  watch: {
    xData() {
      this.drawLine()
    },
    language() {
      this.legend = [this.$t(this.legendArr[0]), this.$t(this.legendArr[1])];
      this.drawLine()
    }
  },
};
</script>


