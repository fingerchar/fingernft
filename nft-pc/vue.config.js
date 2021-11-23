const path = require('path');
const CompressionWebpackPlugin = require('compression-webpack-plugin');
const productionGzipExtensions = ['js', 'css'];
require('babel-polyfill')

const isProduction = process.env.NODE_ENV === 'production';


module.exports = {
    lintOnSave: false,
    outputDir: "dist",
    runtimeCompiler: false,
    productionSourceMap: false,
    css: {
      loaderOptions: {
        scss: {
          additionalData: `@import "~@/styles/variables.scss";`
        }
      }
    },
    pages: {
        index: {
            // entry point to NFT-MARKET application
            entry: "src/main.js",
            // html file to use as template when serving or building
            template: "public/index.html",
            // file to distribute from build for production (in the dist directory)
            filename: "index.html"
        }
    },
    transpileDependencies: [
      'some-imported-lib',
      'iview',
      'axios',
    ],
    pwa: {
      name: "Finger NFT",
      iconPaths: {
        favicon32: 'favicon.ico',
        favicon16: 'favicon.ico',
        appleTouchIcon: 'favicon.ico',
        maskIcon: 'favicon.ico',
        msTileImage: 'favicon.ico'
      }
    },
    chainWebpack: (config) => {
       config.plugins.delete('index').delete('preload-index');
    },
    devServer: {
        host: '0.0.0.0',
        port: 20008,
        https: false,
        hotOnly: false,
        disableHostCheck: false,
        proxy: {
            '/fingernft': {
                target: 'https://fingernft.fingerchar.com',
            },
            "/static":{
                target: 'https://fingernft.fingerchar.com',
                changeOrigin: true,
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
        // 生产模式
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
}
