<template>
  <el-dropdown trigger="click" @command="handleSetSize">
    <div>
      <el-icon
        @click="click"
        style="
          display: inline-block;
          cursor: pointer;
          fill: #5a5e66;
          width: 20px;
          height: 20px;
          font-size: 20px;
        "
        ><Rank
      /></el-icon>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item :disabled="size === 'medium'" command="medium"
          >Medium</el-dropdown-item
        >
        <el-dropdown-item :disabled="size === 'small'" command="small"
          >Small</el-dropdown-item
        >
        <el-dropdown-item :disabled="size === 'mini'" command="mini"
          >Mini</el-dropdown-item
        >
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script>
  export default {
    computed: {
      size() {
        return this.$store.getters.size;
      },
    },
    methods: {
      handleSetSize(size) {
        this.$ELEMENT.size = size;
        this.$store.dispatch("setSize", size);
        this.refreshView();
        this.$message({
          message: "布局尺寸切换成功",
          type: "success",
        });
      },
      refreshView() {
        // In order to make the cached page re-rendered
        this.$store.dispatch("delAllCachedViews", this.$route);

        const { fullPath } = this.$route;

        this.$nextTick(() => {
          this.$router.replace({
            path: "/redirect" + fullPath,
          });
        });
      },
    },
  };
</script>

<style scoped></style>
