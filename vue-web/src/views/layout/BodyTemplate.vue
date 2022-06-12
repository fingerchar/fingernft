<template>
  <component :is="currentView" class="body-template" />
</template>

<script>
export default {
  name: "BodyTemplate",
  props: {
    currentView: {
      type: Object,
      default: null
    }
  },
};
</script>

