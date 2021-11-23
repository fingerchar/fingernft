<template>
  <div class="trades-item" v-if="tradeSize && loadStatus">
    <div class="inner big-inner">
      <el-skeleton :loading="loadStatus" animated>
        <template #template>
          <el-skeleton-item
            class="goodsItem-image-skeleton"
            style="width: 50%"
          />
          <el-skeleton-item
            variant="image"
            :style="{ width: imgSize + 'px', height: imgSize + 'px' }"
          />
          <el-skeleton-item
            class="goodsItem-image-skeleton"
            style="width: 70%"
          />
        </template>
      </el-skeleton>
    </div>
  </div>
  <div class="trades-item trades-item-small" v-else-if="!tradeSize && loadStatus" v-for="i in 6" :key="i">
    <div class="inner">
      <el-skeleton :loading="loadStatus" animated>
        <template #template>
          <el-skeleton-item
            variant="image"
            :style="{ width: imgSize / 2 + 'px', height: imgSize / 2 + 'px' }"
          />
          <div class="avatar-r">
            <el-skeleton-item
              class="goodsItem-image-skeleton"
              style="width: 50%"
            />
            <el-skeleton-item
              class="goodsItem-image-skeleton"
              style="width: 70%"
            />
          </div>
        </template>
      </el-skeleton>
    </div>
  </div>
</template>

<script>
export default {
  name: "TradesItemLoad",
  data() {
    return {
      imgSize: 100,
    };
  },
  props: {
    tradeSize: {
      type: String,
      default: "",
    },
    loadStatus: {
      type: Boolean,
      default: false,
    },
  },
  mounted() {
    window.addEventListener("resize", this.handleResize);
  },
  unmounted() {
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    handleResize() {
      let big_inner = document.querySelectorAll(".inner.big-inner")[0];
      this.imgSize = big_inner.offsetWidth * (100 / 216);
    },
  },
};
</script>
<style lang="scss" scoped>
.trades-item {
  display: flex;
  box-sizing: border-box;
  padding: 10px;
  .inner {
    display: flex;
    position: relative;
    background: #fff;
    border-radius: $borderRadius;
    overflow: hidden;
    width: 100%;
    height: 100%;
    flex-wrap: wrap;
    align-items: center;
    .el-skeleton {
      display: flex;
      align-items: center;
      .avatar-r {
        flex: 1;
      }
      .el-skeleton__image {
        border-radius: 50%;
        margin-left: 35px;
        margin-right: 15px;
      }
    }
  }
}

.big-inner.inner {
  flex-direction: column;
  justify-content: center;
  text-align: center;
  .el-skeleton {
    height: 100%;
    display: flex;
    flex-flow: column wrap;
    justify-content: center;
    align-items: center;
    .el-skeleton__image {
      margin: 20px 0;
      border-radius: 50%;
    }
  }
}
.trades-item-small {
  width: 33.33333333%;
  height: 50%;
  margin-bottom: 0;
}

@media screen and (max-width: 1040px) {
  .trades-item-small {
    height: 100px;
  }
}
@media screen and (max-width: 780px) {
  .trades-item-small {
    width: 50%;
    height: 100px;
  }
}
</style>