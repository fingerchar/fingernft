const CompressionWebpackPlugin = require('compression-webpack-plugin');
const productionGzipExtensions = ['js', 'css'];
const isProduction = process.env.NODE_ENV === 'production';


module.exports = {
  productionSourceMap: false,
  css: {
    loaderOptions: {
      scss: {
        additionalData: `@import "~@/styles/variables.scss";`,
      },
    },
  },
  devServer: {
    host: "0.0.0.0",
    port: 20008,
    https: false,
    hotOnly: false,
    disableHostCheck: false,
    proxy: {
      "/fingernft": {
        target: process.env.VUE_APP_API_URL,
      },
      "/static": {
        target: process.env.VUE_APP_STATIC_URL,
      },
      "/oauth": {
        target: process.env.VUE_APP_OAUTH_URL,
      },
    },
  },
  configureWebpack: config => {
        if (!isProduction) {
            config.devtool = 'cheap-source-map';
            config.optimization = {
                splitChunks: {
                    cacheGroups: {
                        default: false
                    }
                }
            }
        }
 
        if (isProduction) {
            // 打包生产.gz包
            config.plugins.push(new CompressionWebpackPlugin({
                algorithm: 'gzip',
                test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'),
                threshold: 10240,
                minRatio: 0.8
            }))
            config.performance = {
              hints: "warning",
              hints: "error",
              hints: false,
              maxAssetSize: 200000,
              maxEntrypointSize: 400000,
              assetFilter: function(assetFilename) {
                return assetFilename.endsWith('.css') || assetFilename.endsWith('.js');
              }
            }
        }
 
    }

};
