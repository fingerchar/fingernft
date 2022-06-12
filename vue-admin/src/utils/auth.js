import Cookies from "js-cookie";

const TokenKey = "FingerNft-Admin-Token";

export function getToken() {
  // return window.localStorage.getItem(TokenKey);
  return Cookies.get(TokenKey);
}

export function setToken(token) {
  // window.localStorage.setItem(TokenKey, token);
  return Cookies.set(TokenKey, token);
}

export function removeToken() {
  // localStorage.removeItem(TokenKey);
  return Cookies.remove(TokenKey);
}
