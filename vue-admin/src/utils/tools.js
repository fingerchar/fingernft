import Web3 from "web3";
import i18n from "@/i18n/i18n";
import { ElMessageBox } from "element-plus";

export default {
  checkResponse(response) {
    if (!response.errno) {
      return true;
    }
    return false;
  },
  isAudioUrl(url) {
    return [".mp3", ".wav", ".oga"].some(function (ext) {
      return url.endsWith(ext);
    });
  },
  isVideoUrl(url) {
    return [".webm", ".mp4", ".m4v", ".ogg", ".ogv", ".mov"].some(function (
      ext
    ) {
      return url.endsWith(ext);
    });
  },
  decimal(num, v) {
    if (!v) v = 2;
    var vv = Math.pow(10, v);
    return Math.round(num * vv) / vv;
  },
  isAddress(address) {
    return Web3.utils.isAddress(address);
  },
  ellipsisAddress(address) {
    if (!address) return;
    return address.slice(0, 11) + "..." + address.slice(-4);
  },
  messageBox(title, content, confirmText = "") {
    ElMessageBox.confirm(content, title, {
      customClass: "finger-message",
      confirmButtonText: confirmText || i18n.t("global.confirm"),
      confirmButtonClass: "finger-message-btn",
      buttonSize: "medium",
      showClose: false,
      showCancelButton: false,
      center: true,
    })
      .then(() => {
        return true;
      })
      .catch(() => {
        return false;
      });
  },
};
