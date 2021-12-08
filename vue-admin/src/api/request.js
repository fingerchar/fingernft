import axios from 'axios'
import store from '@/store'
import router from '@/router'
import { isExternal } from '@/utils/validate'
import errnoHandler from '@/utils/errnoHandler'
import Vue from 'vue'
import { getToken,removeToken } from "@/utils/auth";
import i18n from "@/i18n/i18n.js";

const service = axios.create({
  timeout: 30 * 1000000 // request timeout
})

service.interceptors.request.use(
  config => {
    if (isExternal(config.url)) config.baseURL = '' // crmChat api 用的不一样的代理
    if (!config.headers['FingerNft-Admin-Token'] && getToken() ) {
      config.headers['FingerNft-Admin-Token'] = getToken();
    }
 
    return config
  },
  error => {
    console.log('error', error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    if (response.headers['content-type'] && response.headers['content-type'].includes('nd.ms-excel')) {
      return response
    }
    if (response.headers['content-disposition'] && response.headers['content-disposition'].includes('attachment')) {
      return response
    }
    const { errno, errmsg, type } = errnoHandler(response)
    const url = response.config.url.match(/(\/admin|\/jzshop)(.*)/)[0]
    //console.log(type,'type')


    type === 'error' && Vue.prototype.$notify({
      type,
      title: errno + `  ${url}`,
      message:i18n.t('response.'+errno),
      duration: 3.5 * 1000
    })
    if(errmsg==='unauthz'){
        removeToken();
        router.push("/login");
    }
    return Promise.resolve(response.data)
  }, error => {
    const { errno, errmsg, type } = errnoHandler(error)
    const url = error.config.url.match(/(\/admin|\/jzshop)(.*)/)[0]
    Vue.prototype.$notify({
      type,
      title: errno + `  ${url}`,
      message: i18n.t('response.'+errno),
      duration: 3.5 * 1000
    })
    return Promise.reject(error)
  })

export default service
