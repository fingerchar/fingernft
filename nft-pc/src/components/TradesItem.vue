<template>
  <div class="trades-item" v-if="tradeSize">
    <div class="inner big-inner">
        <div class="username">
          <div class="bigTag">1</div>
          BIOH
        </div>
        <div class="avatar">
          <avatar
            @click="goUser()"
            class="bflex cpointer"
            :imgWidth="imgSize"
            :imgHeight="imgSize"
            shape="circular"
          ></avatar>
        </div>
        <div class="price">99.33ETH</div>
    </div>
  </div>
  <div class="trades-item trades-item-small" v-else>
  <div class="inner">
        <div class="tag">{{ index + 2 }}</div>
        <div class="avatar">
          <avatar
            @click="goUser()"
            class="bflex cpointer"
            :imgWidth="imgSize / 2"
            :imgHeight="imgSize / 2"
            shape="circular"
          ></avatar>
        </div>
        <div class="avatar-r">
          <div class="username">BIOH</div>
          <div class="price">99.33ETH</div>
        </div>
   </div>
  </div>
</template>

<script>
export default {
  name: "TradesItem",
  data() {
    return {
      imgSize: 100,
      loading: true,
    };
  },
  props: {
    index: Number,
    tradeSize: {
      type: String,
      default: "",
    },
    trade: {
      type: Object,
      default: null,
    },
  },
  mounted() {
    window.addEventListener("resize", this.handleResize);
  },
  unmounted() {
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    goUser() {},
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
    .avatar {
      margin-left: 35px;
      margin-right: 15px;
    }
    & .tag {
      position: absolute;
      font-size: 12px;
      color: #3795f1;
      font-weight: 800;
      left: 15px;
      top: 50%;
      transform: translateY(-50%);
    }
  }
}
.big-inner.inner {
  flex-direction: column;
  justify-content: center;
  text-align: center;
  & .avatar {
    margin: 20px 0;
  }
  & .bigTag {
    font-weight: 800;
    color: #fff;
    background: $gradientColor;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    position: absolute;
    text-align: center;
    line-height: 20px;
    left: 15px;
  }
}
.username {
  font-size: 15px;
  color: #000;
  font-weight: 800;
  position: relative;
  width: 100%;
}
.price {
  color: #888;
}
@media screen and (max-width: 1040px) {
  .trades-item-small {
    height: 100px !important;
  }
}
@media screen and (max-width: 780px) {
  .trades-item-small {
    width: 50%;
    height: 100px !important;
  }
}
</style>

