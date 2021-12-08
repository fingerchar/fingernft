<template>
  <div class="mb-10">
    开始时间：
    <el-date-picker class="mr-50" @change="changeEnd" v-model="startBudgetTime" type="date"
      :picker-options="pickerOptionsStart" placeholder="选择日期"></el-date-picker>
    结束时间：
    <el-date-picker @change="changeStart" v-model="endBudgetTime" type="date"
      :picker-options="pickerOptionsEnd" placeholder="选择日期" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      pickerOptionsStart: {},
      pickerOptionsEnd: {},
      startBudgetTime: "", // 预算开始时间
      endBudgetTime: "", // 预算结束时间
    };
  },
  methods: {
    // 结束时间限制开始时间
    changeStart() {
      if (!this.endBudgetTime) {
        this.pickerOptionsStart = {
          disabledDate: {},
        };
        return;
      }
      this.pickerOptionsStart = Object.assign({}, this.pickerOptionsStart, {
        // 可通过箭头函数的方式访问到this
        disabledDate: (time) => {
          var times = "";
          times = time.getTime() > this.endBudgetTime;
          return times;
        },
      });
    },
    // 开始时间 控制结束时间
    changeEnd() {
      if (!this.startBudgetTime) {
        this.pickerOptionsEnd = {
          disabledDate: {},
        };
        return;
      }
      this.pickerOptionsEnd = Object.assign({}, this.pickerOptionsEnd, {
        disabledDate: (time) => {
          return time.getTime() < this.startBudgetTime;
        },
      });
    },
  },
};
</script>

<style scoped>
.mb-10 {
  margin-bottom: 10px;
}
.mr-50 {
  margin-right: 50px;
}
</style>
