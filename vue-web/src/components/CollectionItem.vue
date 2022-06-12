<template>
  <div class="collection-item">
    <div class="inner" @click="toDetail">
      <div class="cover-img">
        <img v-if="collection.cover" :src="collection.cover" alt=""/>
        <img v-else class="default" src="@/assets/img/FINGER.jpg" alt=""/>
      </div>
      <div class="cover-info">
        <avatar
          @click="goUser(collection.owner)"
          class="bflex cpointer"
          :imageUrl="$filters.fullImageUrl(collection.owner)"
          :address="collection.owner"
          :imgWidth="40"
          :imgHeight="40"
          shape="circular"
        ></avatar>
        <div class="name">{{collection.nickname || $filters.ellipsisAddress(collection.owner)}}<br/> <span class="nameby blueColor" v-if="collection.owner"> <span class="cColor">by</span> {{$filters.ellipsisAddress(collection.owner)}}</span><span class="nameby" v-else></span></div>
      </div>
      <div class="cover-text" v-if="collection.description">"{{collection.description}}"</div>
      <div class="cover-text" v-else>"The collection without description."</div>
    </div>
  </div>
</template>
<script>
export default {
  props:{
    collection:{
      type: Object,
      default: {
        contract: {},
        deal: {}
      }
    }
  },
  data() {
    return {
      
    }
  },
  methods:{
    goUser(address) {
      this.$router.push({ path: "/account/" + address });
    },
    toDetail() {
      console.log(this.collection)
      this.$router.push({ path: "/collection/" + this.collection.address });
    }
  }
}
</script>
<style lang="scss" scoped>
.collection-item {
  position: relative;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  margin-bottom: 20px;
  padding: 0 20px;
  color: #fff;
  .inner {
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    background: #323548;
    border-radius: 30px;
    overflow: hidden;
    cursor: pointer;
    .cover-img {
      width: 90%;
      height: 300px;
      margin: 0 auto;
      display: flex;
      justify-content: center;
      align-items: center;
      img {
        max-width: 100%;
        max-height: 100%;
        height: auto;
        border-radius: 1%;
      }
      img.default {
        width: 50%;
        height: auto;
      }
    }
    .cover-info {
      margin-top: -23px;
      display: flex;
      flex-direction: column;
      align-items: center;
      position: relative;
      z-index: 1;
      .name {
        text-align: center;
        font-size: 24px;
        .nameby {
          font-size: 16px;
          display: inline-block;
          min-height: 29px;
        }
      }
    }
    .cover-text {
      width: 90%;
      margin: 10px auto;
      color: #ccc;
      font-size: 16px;
      word-break: break-word;
      height: 48px;
      line-height: 24px;
      text-overflow:ellipsis;
      overflow: hidden;
      display:-webkit-box;
      -webkit-line-clamp:2;
      -webkit-box-orient:vertical;
    }
  }
}
.blueColor {
  color: skyblue
}
.cColor {
  color: #ccc;
}

</style>
