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
      dataArr: []
    }
  },
  props: {
    title: String,
    data: {
      type: Array,
      default: () => [],
    },
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
      this.dataArr = this.data.length <= 0 ? [{ value: "", name: this.$t('dashboard.noData') }] : this.data;
      let myChart = this.$echarts.init(document.getElementById(this.ids));
      let option = {
        title: {
          text: this.title,
          left: "left",
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)",
        },
        legend: {
          orient: "vertical",
          right: 20,
          top: 50,
          bottom: 20,
          backgroundColor: "#FAFAFA",
        },
        label: {
          formatter: "{d}%",
          backgroundColor: "#000",
          position: "top",
          padding: 3,
          color: "#fff",
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
            data: this.dataArr,
            itemStyle: {
              borderWidth: 1,
              borderColor: "#fff",
            },
          },
        ],
      };
      myChart.setOption(option, true);
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
  },

  watch: {
    data() {
      this.drawLine()
    },
    language() {
      this.drawLine()
    },
  }
}
</script>


