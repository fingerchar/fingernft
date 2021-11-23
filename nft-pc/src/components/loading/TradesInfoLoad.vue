<template>
  <el-skeleton
    v-if="loadStatus != 'over'"
    :loading="loadStatus == 'loading'"
    v-for="i in limit"
    :key="i"
    animated
  >
    <template #template>
      <div class="inner">
        <el-skeleton-item variant="image" style="width: 130px; height: 130px" />
        <div class="descript">
          <el-skeleton-item variant="h3" style="width: 30%" />
          <el-skeleton-item variant="h3" style="width: 50%; margin: 15px 0" />
          <el-skeleton-item variant="h3" style="width: 15%" />
        </div>
      </div>
    </template>
  </el-skeleton>
  <div class="cel-load-status" v-else>
    <div class="loading-over">{{ $t("loadStatus.end") }}</div>
  </div>
</template>
<script>
export default {
  name: "TradesInfoLoad",
  props: {
    loadStatus: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      limit: this.$store.state.pageLimit,
    };
  },
};
</script>

<style lang="scss" scoped>
.inner {
  display: flex;
  align-items: center;
  padding: 18px 30px;
  background: #fff;
  .descript {
    margin-left: 20px;
    flex: 1;
  }
}
.el-skeleton + .el-skeleton {
  margin-top: 40px;
}
.el-skeleton__item {
  display: block;
}
.cel-load-status {
  padding: 10px 0;
  width: 100%;
  .loading-over {
    text-align: center;
    padding: 30px 0;
    color: $grayColor;
  }
}
</style>