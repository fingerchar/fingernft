<script>
  import { h } from "vue";

  export default {
    name: "MenuItem",
    functional: true,
    props: {
      icon: {
        type: String,
        default: "",
      },
      title: {
        type: String,
        default: "",
      },
    },
    render() {
      const vnodes = [];

      // if (icon) {
      //   vnodes.push(<svg-icon icon-class={icon} />);
      // }

      if (this.title) {
        // vnodes.push(<span slot="title">{title}</span>);
        vnodes.push(h("span", {}, this.title));
      }
      return vnodes;
    },
  };
</script>
