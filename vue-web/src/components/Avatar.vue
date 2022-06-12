<template>
  <div>
    <img v-if="imageUrl" :width="imgWidth" :height="imgHeight"
        class="avatar-image"
      :class=" shape=='circular' ? 'circular':'' " :src="thumbnail(imageUrl)" />
    <jazzicon class="avatar-image" :shape="shape" v-else :address="address"
      :diameter="imgWidth">
    </jazzicon>
  </div>
</template>
<script>
import Jazzicon from '@/components/components';

export default {
  components:{
    Jazzicon,
  },
  name: "HeadPortrait",
  props: {
    imageUrl: {
      type: String,
      default: "",
    },
    address: {
      type: String,
      default: "",
    },
    imgWidth: {
      type: Number,
      default: 50,
    },
    imgHeight: {
      type: Number,
      default: 50,
    },
    shape: {
      type: String,
      default: "",
    },
  },
  data() {
    return {};
  },
  methods: {
    thumbnail(url){
      if (url) {
        if(url.endsWith('.gif')) return url;
        if(url.indexOf("ipfs") != -1) return url;
        if(url.startsWith("data:image")) return url;
        return url + '!100x100';
      } else {
        return "";
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.avatar-image{
  display: flex;
  border-radius: $borderRadius;
  &.circular{
    border-radius: 50%;
  }
}
</style>
