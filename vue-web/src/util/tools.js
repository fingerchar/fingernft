import { ElMessageBox, ElMessage } from "element-plus";
import i18n from "@/i18n/i18n";
import store from "@/store";
import router from "@/router";
import web3 from "@/util/web3";
import BigNumber from "bignumber.js";

const NETWORKS = {
  "1": "Main",
  "2": "Morden",
  "3": "Ropsten",
  "4": "Rinkeby",
  "42": "Kovan",
  "5777": "Ganache",
  "56": "Bnb",
  "97": "Bnb Testnet",
  "137": "Polygon",
  "80001": "Polygon Mumbai",
  "43113": "avalanche FUJI",
  "43114": "avalanche",
};
const NOTIFY_TYPE = {
  "1": "FOLLOW",
  "2": "LIKE",
  "3": "TRADE",
};

const NOTIFY_SUB_TYPE = {
  "1": "SALE", // 无
  "2": "EDIT_SAlE", // 无
  "3": "CANCEL_SALE", // 自己
  "4": "BUY", // 通知卖方
  "5": "BID", // onwers
  "6": "EDIT_BID", // onwers
  "7": "CANCEL_BID", // onwers + 自己
  "8": "ACCEPT_BID", // 卖方
  "9": "MINT", // 自己
  "10": "LIKING", // 无
  "11": "LIKED", // creator
  "12": "FOLLOWING", // 无
  "13": "FOLLOWED", // 被关注者
  "14": "BURN", // 自己
  "15": "TRANSFER", // 发送者
  "16": "RECEIVE", // 接收者
  "17": "BOUGHT", // 通知买方
  "18": "BIDDEN", // 买方
};

const NOTIFY_SUB_TYPE_CODE = {};
for(var key in NOTIFY_SUB_TYPE){
  NOTIFY_SUB_TYPE_CODE[NOTIFY_SUB_TYPE[key]] = key;
}

const AUCTION_STATUS = {
  "0": "CREATED",
  "1": "FINISHED",
};

const AUCTION_STATUS_CODE = {
  "1": "cancel",
  "2": "finished",
  "3": "expired",
};

let messageBoxStatus = true;

export default {
  imageType(){
    return "image/jpg,image/jpeg,image/png,image/gif,image/svg,image/webp,image/avif";
  },
  mediaType(){
    return this.imageType() + ",audio/midi,audio/mpeg,audio/webm, audio/mp4,audio/mp3";
  },
  getNotifyType(type) {
    return NOTIFY_TYPE[type];
  },
  getNotifySubType(subType) {
    return NOTIFY_SUB_TYPE[subType];
  },
  getNotitySubTypeCode(name){
    return NOTIFY_SUB_TYPE_CODE[name];
  },
  minPriceLimit(price, paytoken) {
    let value = new BigNumber(price);
    value = value.multipliedBy(
      new BigNumber(10).exponentiatedBy(paytoken.decimals)
    );
    value = value.toFixed();
    if (value >= 1) return true;
    return false;
  },
  needLogin(path) {
    if (!store.state.connected) {
      let url = "/connect";
      if (path) url += "?redirect=" + path;
      router.push(url);
      return false;
    }
    if (!store.state.isLogin) {
      store.dispatch("signLogin");
      return false;
    }
    return true;
  },
  needConnected(path) {
    if (!store.state.connected) {
      let url = "/connect";
      if (path) url += "?redirect=" + path;
      router.push(url);
      return false;
    }
    return true;
  },
  checkResponse(response) {
    if (!response.errno) {
      return true;
    }
    return false;
  },
  serializeNfts(nfts) {
    let _nfts = [];
    for (var i = 0; i < nfts.length; i++) {
      let nft = nfts[i];
      _nfts.push(nft.address + ":" + nft.tokenId);
    }
    return _nfts.join(",");
  },
  serializeNfts2(nfts) {
    let _nfts = [];
    for (var i = 0; i < nfts.length; i++) {
      let nft = nfts[i];
      if (nft.address) {
        _nfts.push(nft.address + ":" + nft.tokenId);
      } else if (nft.sellToken && nft.sellTokenId != 0) {
        _nfts.push(nft.sellToken + ":" + nft.sellTokenId);
      } else {
        _nfts.push(nft.buyToken + ":" + nft.buyTokenId);
      }
    }
    return _nfts.join(",");
  },
  parseMetaData(content) {
    if (typeof content != "string") return content;
    try {
      return JSON.parse(content);
    } catch (e) {
      return {};
    }
  },
  str2num(n) {
    var _n = parseFloat(n);
    if (isNaN(_n) || _n != n) return;

    if (
      !(Number(_n) === _n && _n % 1 !== 0) &&
      !(Number(_n) === _n && _n % 1 === 0)
    ) {
      return;
    }
    return _n;
  },
  isEmpty(obj) {
    if (obj == null) return true;
    if (obj.length > 0) return false;
    if (obj.length === 0) return true;
    if (typeof obj !== "object") return true;
    for (var key in obj) {
      if (hasOwnProperty.call(obj, key)) return false;
    }
    return true;
  },
  decimal(num, v) {
    if (!v) v = store.state.decimal;
    var vv = Math.pow(10, v);
    return Math.round(num * vv) / vv;
  },
  isAudioUrl(url) {
    return [".mp3", ".wav", ".oga"].some(function(ext) {
      return url.endsWith(ext);
    });
  },
  isVideoUrl(url) {
    return [".webm", ".mp4", ".m4v", ".ogg", ".ogv", ".mov"].some(function(
      ext
    ) {
      return url.endsWith(ext);
    });
  },
  networkName(networkId) {
    var name = NETWORKS[networkId];
    if (!name) {
      return "unknown network";
    }
    return name;
  },
  delHashFormat(hash) {
    if (hash.startsWith("0x")) return hash.slice(2, hash.length);
    return hash;
  },
  message(message, type = "") {
    ElMessage.closeAll();

    ElMessage({
      showClose: false,
      message: message,
      type: type || "error",
      offset: 40,
      duration: 2000,
    });
  },
  messageBox(title, content, confirmText = "") {
    if (!messageBoxStatus) return;
    messageBoxStatus = false;
    ElMessageBox.confirm(content, title, {
      customClass: "finger-message",
      confirmButtonText: confirmText || i18n.global.t("global.confirm"),
      confirmButtonClass: "finger-message-btn",
      buttonSize: "medium",
      showClose: false,
      showCancelButton: false,
      center: true,
    })
      .then(() => {
        messageBoxStatus = true;
      })
      .catch(() => {
        messageBoxStatus = true;
      });
  },
  time(item) {
    let nowTimeStamp = new Date().getTime();
    let updateTimeStamp = new Date(item.addtime).getTime();
    let newTime = nowTimeStamp - updateTimeStamp;
    let finallyTime;
    if (newTime / 1000 / 3600 / 24 < 1) {
      if (newTime / 1000 / 3600 < 1) {
        if (newTime / 1000 / 60 < 1) {
          finallyTime = parseInt(newTime / 1000) + " seconds ago";
        } else {
          finallyTime = parseInt(newTime / 1000 / 60) + " minutes ago";
        }
      } else {
        finallyTime = parseInt(newTime / 1000 / 3600) + " hours ago";
      }
    } else {
      finallyTime = parseInt(newTime / 1000 / 3600 / 24) + " days ago";
    }
    return finallyTime;
  },
  singlePrice(price, quanlity, payToken) {
    let value = new BigNumber(price);
    value = value.dividedBy(Math.pow(10, payToken?.decimals));
    value = value.dividedBy(new BigNumber(quanlity)).toFixed(6);
    value = parseFloat(value);
    return value;
  },
  decimalsValue(value, quanlity, decimals){
    value = new BigNumber(value);
    value = value.multipliedBy(new BigNumber(quanlity)).multipliedBy(
      new BigNumber(10).exponentiatedBy(new BigNumber(decimals))
    ).toFixed();
    return value;
  },
  noDecimalsValue(value, quanlity, decimals){
    value = new BigNumber(value);
    value = value.dividedBy(new BigNumber(quanlity)).dividedBy(
      new BigNumber(10).exponentiatedBy(new BigNumber(decimals))
    ).toFixed();
    return value;
  },
  analysis(param) {
    try {
      return JSON.parse(param);
    } catch (e) {
      return {};
    }
  },
  formatSecond(second) {
    var days = Math.floor(second / 86400);
    var hours = Math.floor((second % 86400) / 3600);

    var minutes = Math.floor(((second % 86400) % 3600) / 60);
    var seconds = Math.floor(((second % 86400) % 3600) % 60);
    if (hours < 10) hours = "0" + hours;
    if (minutes < 10) minutes = "0" + minutes;
    if (seconds < 10) seconds = "0" + seconds;
    var str = "";
    if (days) {
      str = days + " " + i18n.global.t("time.days") + " ";
    }
    str = str + hours + ":" + minutes + ":" + seconds;
    return str;
  },
  countData(leadTime) {
    let hours;
    let minutes;
    let seconds;
    let countDown;
    hours = parseInt((leadTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    minutes = parseInt((leadTime % (1000 * 60 * 60)) / (1000 * 60));
    seconds = (leadTime % (1000 * 60)) / 1000;
    hours = hours < 10 ? "0" + hours : hours;
    minutes = minutes < 10 ? "0" + minutes : minutes;
    seconds = seconds < 10 ? "0" + seconds : seconds;
    countDown = hours + ":" + minutes + ":" + seconds;
    return countDown;
  },
};
