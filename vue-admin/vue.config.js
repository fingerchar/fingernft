const { defineConfig } = require("@vue/cli-service");
const isDev = process.env.NODE_ENV === "development";
const webpack = require("webpack");
const NodePolyfillWebpackPlugin = require("node-polyfill-webpack-plugin");
const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = defineConfig({
  productionSourceMap: isDev,
  devServer: {
    port: 9528,
    proxy: {
      "/admin": {
        target: process.env.VUE_APP_API_URL,
        changeOrigin: true,
      },
      "/static": {
        target: process.env.VUE_APP_STATIC_URL,
        changeOrigin: true,
      },
    },
  },
  // 引入全局变量scss
  css: {
    loaderOptions: {
      scss: {
        additionalData: `@import "~@/styles/variables.scss";`,
      },
    },
  },
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        Buffer: ["buffer", "Buffer"],
      }),
      new NodePolyfillWebpackPlugin(),
    ],
  },
  chainWebpack(config) {
    config.module.rule("svg").exclude.add(resolve("src/icons")).end();
    config.module
      .rule("icons")
      .test(/\.svg$/)
      .include.add(resolve("src/icons"))
      .end()
      .use("svg-sprite-loader")
      .loader("svg-sprite-loader")
      .options({
        symbolId: "icon-[name]",
      })
      .end();
  },
});
