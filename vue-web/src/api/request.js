import axios from 'axios'
import i18n from "@/i18n/i18n";
import router from '@/router';
import tools from '@/util/tools.js'
import store from '@/store'

const service = axios.create({
  // baseURL: process.env.VUE_APP_BASE_API, // api 的 base_url
  timeout: 50000, // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    if (!config.headers['Finger-Nft-Token']) {
      config.headers['Finger-Nft-Token'] = `${window.localStorage.getItem(
        'Authorization'
      ) || ''}`;
    }
 
    return config;
  },
  err => Promise.reject(err)
)


// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.errno === 501) {
      if(!store.state.connected){
        tools.message(i18n.global.t('global.needLogin'), "error");
        setTimeout(() => {
          router.push("/connect");
        }, 1500)    
      }else{
        store.dispatch("signLogin");
      }
      return res;
    } else if (res.errno === 502) {
      tools.message(i18n.global.t('global.err502'), "error");
      return res;
    } if (res.errno === 401) {
      tools.message(i18n.global.t('global.errParams'), "error");
      return res;
    } if (res.errno === 402) {
      tools.message(i18n.global.t('global.errParams'), "error");
      return res;
    } else if (res.errno !== 0) {
      return res;
    } else {
      return res;
    }
  }, error => {
    return {
      errno: 400,
      errmsg: error.message,
    }
  }
)

export default service
