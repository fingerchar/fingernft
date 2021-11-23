<template>
  <div ref="jazzicon" />
</template>
<script>
import MersenneTwister from "mersenne-twister";
import Color from "color";
import addressToNumber from "./addressToNumber";
export default {
  name: "Jazzicon",
  props: {
    shape: {
      type: String,
      default: "",
    },
    seed: {
      type: Number,
      default: Math.round(Math.random() * 10000000),
    },
    diameter: {
      type: Number,
      default: 100,
    },
    address: {
      type: String,
      default: null,
    },
    shapeCount: {
      type: Number,
      default: 4,
    },
    colors: {
      type: Array,
      default: () => [
        "#01888C", // teal
        "#FC7500", // bright orange
        "#034F5D", // dark teal
        "#F73F01", // orangered
        "#FC1960", // magenta
        "#C7144C", // raspberry
        "#F3C100", // goldenrod
        "#1598F2", // lightning blue
        "#2465E1", // sail blue
        "#F19E02", // gold
      ],
    },
  },
  data() {
    return {
      generator: null,
      svgns: "http://www.w3.org/2000/svg",
    };
  },
  watch: {
    seed: {
      handler() {
        this.icon();
      },
    },
    address: {
      handler() {
        this.icon();
      },
    },
    diameter: {
      handler() {
        this.icon();
      },
    },
    shape: {
      handler() {
        this.icon();
      },
    },
  },
  mounted() {
    this.icon();
  },
  methods: {
    async icon() {
      const seed = this.address ? addressToNumber(this.address) : this.seed;
      this.$refs.jazzicon.innerHTML = "";
      const el = await this.generateIdenticon(this.diameter, seed);
      await this.$refs.jazzicon.append(el);
    },
    newPaper(diameter, color) {
      const container = document.createElement("div");
      if (this.shape == "circular") {
        container.style.borderRadius = `${diameter / 2}px`;
      }else{
        container.style.borderRadius = '10px';
      }

      container.style.overflow = "hidden";
      container.style.padding = "0px";
      container.style.margin = "0px";
      container.style.width = "" + diameter + "px";
      container.style.height = "" + diameter + "px";
      container.style.display = "inline-block";
      container.style.background = color;
      return {
        container: container,
      };
    },
    generateIdenticon(diameter, seed) {
      this.generator = new MersenneTwister(seed);
      const remainingColors = this.hueShift(
        this.colors.slice(),
        this.generator
      );
      const elements = this.newPaper(diameter, this.genColor(remainingColors));
      const container = elements.container;
      const svg = document.createElementNS(this.svgns, "svg");
      svg.setAttributeNS(null, "x", "0");
      svg.setAttributeNS(null, "y", "0");
      svg.setAttributeNS(null, "width", diameter);
      svg.setAttributeNS(null, "height", diameter);
      container.appendChild(svg);
      for (let i = 0; i < this.shapeCount - 1; i++) {
        this.genShape(remainingColors, diameter, i, this.shapeCount - 1, svg);
      }
      return container;
    },
    genShape(remainingColors, diameter, i, total, svg) {
      const center = diameter / 2;
      const shape = document.createElementNS(this.svgns, "rect");
      shape.setAttributeNS(null, "x", "0");
      shape.setAttributeNS(null, "y", "0");
      shape.setAttributeNS(null, "width", diameter);
      shape.setAttributeNS(null, "height", diameter);
      const firstRot = this.generator.random();
      const angle = Math.PI * 2 * firstRot;
      const velocity =
        (diameter / total) * this.generator.random() + (i * diameter) / total;
      const tx = Math.cos(angle) * velocity;
      const ty = Math.sin(angle) * velocity;
      const translate = "translate(" + tx + " " + ty + ")";
      // Third random is a shape rotation on top of all of that.
      const secondRot = this.generator.random();
      const rot = firstRot * 360 + secondRot * 180;
      const rotate =
        "rotate(" + rot.toFixed(1) + " " + center + " " + center + ")";
      const transform = translate + " " + rotate;
      shape.setAttributeNS(null, "transform", transform);
      const fill = this.genColor(remainingColors);
      shape.setAttributeNS(null, "fill", fill);
      svg.appendChild(shape);
    },
    genColor(colors) {
      /* eslint-disable */
      const rand = this.generator.random();
      const idx = Math.floor(colors.length * this.generator.random());
      const color = colors.splice(idx, 1)[0];
      return color;
    },
    hueShift(colors, generator) {
      const wobble = 30;
      const amount = generator.random() * 30 - wobble / 2;
      return colors.map(function (hex) {
        const color = Color(hex);
        color.rotate(amount);
        return color.hex();
      });
    },
  },
};
</script>
