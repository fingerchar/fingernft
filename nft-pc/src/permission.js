import router from './router'
import { getLocalStorage } from "@/util/local-storage.js"
import store from "./store";


router.beforeEach(async(to, from, next) => {
  const items = getLocalStorage("connected")
  if(items.connected){
    next();
  }else{
    if(to.meta.auth){
      next(`/connect?redirect=${to.path}`);
    }else{
      next();
    }
  }
});

