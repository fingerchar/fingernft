import SvgIcon from "@/components/SvgIcon"; // svg组件

const req = require.context("./svg", false, /\.svg$/);
const requireAll = (requireContext) =>
  requireContext.keys().map(requireContext);
requireAll(req);
// const files = require.context("./svg", false, /\.svg$/);
export default {
  install(app) {
    app.component("SvgIcon", SvgIcon);
  },
};
