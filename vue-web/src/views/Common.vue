<template>
  <div class="common-wraper">
    <HeaderTemplate />
    <div class="widClass"><router-view /></div>
    <FooterTemplate />
  </div>
</template>

<script>
import HeaderTemplate from "./layout/HeaderTemplate";
import BodyTemplate from "./layout/BodyTemplate";
import FooterTemplate from "./layout/FooterTemplate";

export default {
  name: "Home",
  components: {
    HeaderTemplate,
    BodyTemplate,
    FooterTemplate,
  },
  props: {
    currentView: {
      type: Object,
      default: null,
    },
  },
  methods: {},
};
</script>

<style lang="scss">
#home {
  width: 100%;
}
.common-wraper {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.widClass {
  width: 100%;
  padding-top: 112px;
  min-height: 100vh;
}
</style>
