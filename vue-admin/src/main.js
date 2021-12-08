import Vue from 'vue'

import api from "@/api/index.js";
import echarts from 'echarts'
import Cookies from 'js-cookie'
import Element from 'element-ui'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import './styles/element-variables.scss'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
import './permission' // permission control

import * as filters from './filters' // global filters

import '@/directive/index.js'

import CountTo from 'vue-count-to'

import * as tools from '@/utils/tools'
import i18n from "@/i18n/i18n.js";

Vue.prototype.$api = api

Vue.prototype.$tool = tools.default

Vue.prototype.$echarts = echarts
Vue.prototype._i18n = i18n;

Vue.component('count-to', CountTo)
Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})


Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
