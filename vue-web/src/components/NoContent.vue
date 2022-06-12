<template>
  <div class="fc-no-content">
    <div class="no-content-image">
      <img src="@/assets/img/nocontent.png" alt="">
    </div>
    <div class="no-content-tip" v-if="tip"> {{tip}} </div>
  </div>
</template>

<script>
export default{
  name: 'NoContent',
  props: {
    tip: {
      type: String,
      default: "",
    }
  },
}
</script>

<style lang="scss" scoped>
.fc-no-content{
  padding: 50px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  flex-direction: column;
  .no-content-image{
    width: 100px;
    display: flex;
    img{
      width: 100%;
    }
  }
  .no-content-tip{
    padding-top: 10px;
    color: #999;
    font-size: 16px;
    text-transform: uppercase;
  }
}

</style>
