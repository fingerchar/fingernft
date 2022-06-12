import { createApp } from "vue";
import App from "./App.vue";
import router from "@/router";
import store from "@/store";
import api from "@/api/index.js";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import svgComps from "@/icons";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
const req = require.context("./icons/svg", false, /\.svg$/);
const requireAll = (requireContext) =>
  requireContext.keys().map(requireContext);
requireAll(req);
import echarts from "echarts";

import "normalize.css/normalize.css"; // A modern alternative to CSS resets

import "./styles/element-variables.scss";

import "@/styles/index.scss"; // global css

import "./permission";
import myDirective from "@/directive/index.js";

import tools from "@/utils/tools";
import i18n from "@/i18n/i18n.js";
import * as util_filters from "@/utils/filters";

const app = createApp(App);
app
  .use(router)
  .use(store)
  .use(i18n)
  .use(svgComps)
  .use(myDirective)
  .use(ElementPlus)
  .mount("#app");
app.config.globalProperties.$api = api;
app.config.globalProperties.$echarts = echarts;
app.config.globalProperties.$tool = tools;
app.config.globalProperties.$filters = util_filters;
app.config.globalProperties.$i18n = i18n;
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
