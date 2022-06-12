<template>
  <el-popover placement="bottom" trigger="click" :show-arrow="false" popper-class="filterPopover">
    <template #reference>
      <div class="filter-btn" :class="{'active': category}">
        <img class="filter-icon" src="@/assets/img/filter.png" alt="">
        <span v-if="!category">{{$t('hindex.filter')}}</span>
        <span class="category-text" v-else>
          <span>
            {{ category }}
          </span>
          <span @click.stop="selectFilter('')" class="close-icon iconfont icon-close"></span>
        </span>
      </div>
    </template>
    <div class="popover-filters">
      <div class="popover-filters-item" v-for="(filter, i) in filters" :key="i"
        @click="selectFilter(filter.id)" :class="filterId == filter.id ? 'active' : ''">
        {{ filter.name }}
      </div>
    </div>
  </el-popover>
</template>

<script>
export default {
  name: "FilterAndSort",
  props: {
    filters: {
      type: Object,
      default: {},
    },
    filterId: {
      type: [String, Number],
      default: "",
    },
  },
  emits: ['selectFilter'],
  computed: {
    category() {
      for (var i = 0; i < this.filters.length; i++) {
        let filter = this.filters[i];
        if (filter.id == this.filterId) return filter.name;
      }
      return "";
    },
  },
  methods: {
    selectFilter(id) {
      this.$emit("selectFilter", id);
    },
  },
};
</script>
<style lang="scss" scoped>
.filter-btn {
  cursor: pointer;
  display: flex;
  align-items: center;
  border: 1px solid #eeeeee;
  border-radius: 6px;
  font-size: 13px;
  padding: 8px 12px;
  font-weight: 400;
  color: #333;
  &.active {
    background: #f8f8f8;
  }
  .filter-icon {
    height: 10px;
    padding-right: 5px;
  }
  .close-icon {
    position: absolute;
    font-size: 15px;
    font-weight: bold;
    color: $primaryColor;
    padding-left: 4px;
    right: 0;
    padding: 1px;
    z-index: 100;
  }
  .category-text {
    display: flex;
    position: relative;
    padding-right: 20px;
  }
}
.popover-filters {
  padding: 11px;
  box-shadow: 0px 0px 6px 0px rgba(153, 153, 153, 0.3);
  border-radius: 8px;
  font-size: 14px;
  font-weight: 400;
  color: #444444;
  line-height: 12px;
  width: 140px;
  background: #ffffff;
  border: 1px solid #eeeeee;
  border-radius: 5px;
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
}
.popover-filters-item {
  cursor: pointer;
  margin-left: 7px;
  margin-bottom: 8px;
  background: #f4f4f4;
  border-radius: 5px;
  padding: 7px 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  &.active {
    color: white !important;
    background: $primaryColor !important;
  }
}
</style>
<style lang="scss">
.filterPopover {
  padding: 0 !important;
  width: fit-content !important;
  min-width: 0 !important;
}
</style>
