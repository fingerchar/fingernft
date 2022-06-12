<!-- <template>

</template> -->

<script>
  import { h } from "vue";
  import { isExternal } from "@/utils/validate";
  import { RouterLink } from "vue-router";

  export default {
    props: {
      to: {
        type: String,
        required: true,
      },
    },
    render() {
      if (isExternal(this.to)) {
        return h(
          "a",
          { href: this.to, target: "_blank", rel: "noopener" },
          this.$slots
        );
      } else {
        return h(RouterLink, { to: this.to }, this.$slots);
      }
    },
  };
</script>
