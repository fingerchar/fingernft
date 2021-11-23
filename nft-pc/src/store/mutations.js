"use strict";
import { setLocalStorage, removeLocalStorage, getLocalStorage } from "@/util/local-storage.js"
import sdk from '@/util/sdk/index.js';
import tools from "@/util/tools.js";


export default {
  WEB_LOADING(state){
    state.webLoading = true;
  },
  CONFIG(state, payload){
    state.config = Object.assign({}, payload);
  },
  CONNECT(state, payload){
    if(payload.coinbase) payload.coinbase = payload.coinbase.toLocaleLowerCase();

    state.user = Object.assign({}, state.user, {
      coinbase: payload.coinbase,
    });
    state.web3 = Object.assign({}, state.web3, {
      networkId: payload.networkId,
      walletType: payload.walletType,
    });
    state.connected = true;
    setLocalStorage({
      connected: true,
    });
  },
  WEB3(state, payload){
    state.web3 = Object.assign({}, state.web3, {
      networkId: payload.networkId,
      walletType: payload.walletType,
    });
  },
  LOGIN(state, payload){
    state.token = payload.token;
    if(payload.user && payload.user.address){
      payload.user.address = payload.user.address.toLocaleLowerCase();
    }
    state.user = Object.assign({}, state.user, {
      coinbase: payload.user.address || "",
      avatar: payload.user.avatar || "",
      brief: payload.user.brief || "",
      nickname: payload.user.nickname || "",
      shortUrl: payload.user.shortUrl || "",
      bannerUrl: payload.user.bannerUrl || "",
      id: payload.user.id || "",
    })
    state.web3 = Object.assign({}, state.web3, { 
        networkId: payload.networkId,
        walletType: payload.walletType,
    });
    state.isLogin = true;
    state.connected = true;
    setLocalStorage({
      Authorization: payload.token,
    });
    setLocalStorage({
      connected: true,
    });
  },
  LOGOUT(state){
    state.user = {
      coinbase: "",
      avatar: "",
      brief: "",
      nickname: "",
      shortUrl: "",
      bannerUrl: "",
      id: "",
    };
    state.web3 = {
      address: null,
      coinbase: null,
      error: null,
      instance: null,
      isInjected: false,
      walletType: "",
      networkId: null
    }
    removeLocalStorage("Authorization");
    removeLocalStorage("connected");
    state.ethBalance = "0";
    state.erc20Balance = {};
    state.token = null;
    state.isLogin = false;
    state.connected = false;
    state.notice_unread = 0;
  },
  RELOAD(state){
    state.isLogin = true;
    var items = getLocalStorage("Authorization");
    state.token = items.Authorization;
  },
  USERINFO(state, payload){
    if(payload.address) payload.address = payload.address.toLocaleLowerCase();

    state.user = {
      coinbase: payload.address || "",
      avatar: payload.avatar || "",
      brief: payload.brief || "",
      nickname: payload.nickname || "",
      shortUrl: payload.shortUrl || "",
      bannerUrl: payload.bannerUrl || "",
      id: payload.id || "",
    };
  },
  NOTICE_UNREAD(state, payload){
    state.notice_unread = payload;
  },
  HEARTBEAT(state, timer){
    state.heartbeatTimer = timer;
  },
  ETH_BALANCE(state, balance){
    state.ethBalance = balance;
  },
  ERC20_BALANCE(state, payload){
    state.erc20Balance = Object.assign({}, state.erc20Balance, payload)
  },
  PAYTOKENS(state, payTokens){
    let _paytokens = [];
    for(var i = 0; i < payTokens.length; i++){
      let paytoken = payTokens[i];
      if(paytoken.address == sdk.NULL_ADDRESS()){
        paytoken.erc20 = false;
      }else{
        paytoken.erc20 = true;
      }
      if(paytoken.isDefault){
        state.defalutPayToken = paytoken;
      }
      _paytokens.push(paytoken);
    }
    state.payTokens = _paytokens;
  },
  CATEGORYS(state, categorys){
    state.categorys = categorys;
  },
  setCurrentView(state, newRoute) {
    state.currentView = newRoute.meta.view;
  },
  changeCurrentRouteTo(state, newRoute) {
    state.currentRoute = newRoute;
  },
  LOG_TIMER(state, payload){
    state.logTimer = payload;
  },
  CLEAR_LOGS(state){
    state.logs = [];
  },
  PUSH_LOG(state, log){
    state.logs.push(log);
  },
};


