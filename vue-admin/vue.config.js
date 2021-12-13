'use strict'
const path = require('path');
const { HashedModuleIdsPlugin } = require('webpack')
const CompressionWebpackPlugin = require('compression-webpack-plugin') // 开启压缩
const UglifyJsPlugin = require('uglifyjs-webpack-plugin') // 去掉注释


const isProd = process.env.NODE_ENV === 'production'

function resolve(dir) {
  return path.join(__dirname, dir)
}

const name = 'fingernft' // page title

// If your port is set to 80,
// use administrator privileges to execute the command line.
// For example, Mac: sudo npm run
// You can change the port by the following method:
// port = 9527 npm run dev OR npm run dev --port = 9527
const port = process.env.port || process.env.npm_config_port || 9528 // dev port

// All configuration item explanations can be find in https://cli.vuejs.org/config/
module.exports = {
  /**
   * You will need to set publicPath if you plan to deploy your site under a sub path,
   * for example GitHub Pages. If you plan to deploy your site to https://foo.github.io/bar/,
   * then publicPath should be set to "/bar/".
   * In most cases please use '/' !!!
   * Detail: https://cli.vuejs.org/config/#publicpath
   */
  publicPath: './',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development',
  productionSourceMap: false,
  devServer: {
    host: "0.0.0.0",
    port: port,
    open: false,
    overlay: {
      warnings: false,
      errors: true
    },
    proxy: {
      '/admin': {
        // target: "https://admin.fingerchar.com",
        target: "http://192.168.1.8:8081",
        changeOrigin: true,
      },
      "/static":{
        // target: "https://admin.fingerchar.com",
        target: "http://192.168.1.8:8081",
        changeOrigin: true,
      },
    }
    // after: require('./mock/mock-server.js')
  },
  configureWebpack: config => {
    // provide the app's title in webpack's name field, so that
    // it can be accessed in index.html to inject the correct title.
    const plugins = []
    if (isProd) {
      plugins.push(
        new UglifyJsPlugin({
          uglifyOptions: {
            output: {
              comments: false // 去掉注释
            },
            warnings: false,
            compress: {
              drop_console: true,
              drop_debugger: false,
              pure_funcs: ['console.log']// 移除console
            }
          }
        })
      )
      // 服务器也要相应开启gzip
      plugins.push(
        new CompressionWebpackPlugin({
          algorithm: 'gzip',
          test: /\.(js|css)$/, // 匹配文件名
          threshold: 10000, // 对超过10k的数据压缩
          deleteOriginalAssets: false, // 不删除源文件
          minRatio: 0.8 // 压缩比
        })
      )

      // 用于根据模块的相对路径生成 hash 作为模块 id, 一般用于生产环境
      plugins.push(
        new HashedModuleIdsPlugin()
      )

      // 取消webpack警告的性能提示
      config.performance = {
        hints: 'warning',
        // 入口起点的最大体积
        maxEntrypointSize: 1000 * 500,
        // 生成文件的最大体积
        maxAssetSize: 1000 * 1000,
        // 只给出 js 文件的性能提示
        assetFilter: function (assetFilename) {
          return assetFilename.endsWith('.js')
        }
      }

      // 打包时npm包转CDN
      config.externals = externals
    }

    return { plugins }

  },
  // 引入全局变量scss
  css: {
    loaderOptions: {
      sass: {
        data: `@import "@/styles/variables.scss";`
      }
    }
  },
  chainWebpack(config) {
    config.name = name
    config.plugins.delete('preload') // TODO: need test
    config.plugins.delete('prefetch') // TODO: need test

    if (process.env.VUE_APP_ANALYZE) {
      config
        .plugin('webpack-bundle-analyzer')
        .use(require('webpack-bundle-analyzer').BundleAnalyzerPlugin)
    }

    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    // set preserveWhitespace
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options.compilerOptions.preserveWhitespace = true
        return options
      })
      .end()

    config
      // https://webpack.js.org/configuration/devtool/#development
      .when(process.env.NODE_ENV === 'development',
        // config => config.devtool('cheap-source-map')
        // config => config.devtool('eval-source-map')
      )

    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          config
            .plugin('ScriptExtHtmlWebpackPlugin')
            .after('html')
            .use('script-ext-html-webpack-plugin', [{
            // `runtime` must same as runtimeChunk name. default is `runtime`
              inline: /runtime\..*\.js$/
            }])
            .end()
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // only package third parties that are initially dependent
                },
                elementUI: {
                  name: 'chunk-elementUI', // split elementUI into a single package
                  priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                  test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // can customize your rules
                  minChunks: 8, //  minimum common number
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          config.optimization.runtimeChunk('single')
        }
      )
  }
}
