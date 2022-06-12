<template>
  <div
    :class="{ hidden: hidden, mobile: mobileObj }"
    class="pagination-container"
  >
    <el-pagination
      :hide-on-single-page="show"
      :background="background"
      v-model:current-page="currentPage"
      :key="lang"
      v-model:page-size="pageSize"
      :layout="layout"
      :total="totalValue"
      v-bind="$attrs"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
  import { scrollTo } from "@/utils/scrollTo";
  import { isMobile } from "@/utils";

  export default {
    name: "Pagination",
    props: {
      show: {
        type: Boolean,
        default: true,
      },
      total: {
        required: true,
        type: [Number, String],
      },
      page: {
        type: Number,
        default: 1,
      },
      limit: {
        type: Number,
        default: 10,
      },
      layout: {
        type: String,
        default: isMobile()
          ? "total, prev, pager, next"
          : "total, sizes, prev, pager, next, jumper",
      },
      background: {
        type: Boolean,
        default: true,
      },
      autoScroll: {
        type: Boolean,
        default: true,
      },
      hidden: {
        type: Boolean,
        default: false,
      },
    },
    emits: ["update:page", "update:limit"],
    computed: {
      lang() {
        return this.$store.state.app.language;
      },
      mobileObj() {
        return !!isMobile();
      },
      currentPage: {
        get() {
          return this.page;
        },
        set(val) {
          this.$emit("update:page", val);
        },
      },
      totalValue() {
        if (!this.total) {
          return 0;
        }
        if (typeof this.total == "string") {
          return parseInt(this.total);
        }
        return this.total;
      },
      pageSize: {
        get() {
          return this.limit;
        },
        set(val) {
          this.$emit("update:limit", val);
        },
      },
    },
    methods: {
      handleSizeChange(val) {
        this.$emit("pagination", { page: this.currentPage, limit: val });
        if (this.autoScroll) {
          scrollTo(0, 800);
        }
      },
      handleCurrentChange(val) {
        this.$emit("pagination", { page: val, limit: this.pageSize });
        if (this.autoScroll) {
          scrollTo(0, 800);
        }
      },
    },
  };
</script>

<style scoped>
  .pagination-container {
    background: #fff;
    padding: 32px 16px;
  }
  .pagination-container.hidden {
    display: none;
  }

  .pagination-container.mobile {
    padding: 10px 0;
    margin-top: 0;
  }
</style>
