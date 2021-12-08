import request from "@/api/request";
import apiList from "@/api/apiList";
import qs from "qs";
import store from '@/store'

function getApiObj(url) {
  let apiArray = url.split(".");
  let api = apiList;
  apiArray.forEach(v => {
    api = api[v];
  });
  return api;
}

const URL_PREFIX = "/admin"

export default function api(url, data = {}) {
  let url_prefix = URL_PREFIX;
  if(store.state.app.currentNetwork && !store.state.app.currentNetwork.default){
    url_prefix = "/" + store.state.app.currentNetwork.symbol + url_prefix;
  }
  
  var api = getApiObj(url);
  var post = {
    url: url_prefix + api.url,
    method: api.method,
  };
  if (url == "storage.upload" || url == "storage.create" || url=="storage.multiupload") {
    post.headers = { 'Content-Type': 'multipart/form-data' }
    post.data = data;
  }else {
    var method = api.method.toLowerCase();
    if (method == "post") {
      post.data = qs.stringify(data);
    } else {
      post.post = data;
    }
  }
  return request(post);
}


