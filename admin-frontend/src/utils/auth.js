import Cookies from 'js-cookie'
import {Message} from "element-ui";

const TokenKey = 'authorize'

export function getToken() {
  const str = localStorage.getItem(TokenKey) || sessionStorage.getItem(TokenKey);
  if(!str) return null
  const authObj = JSON.parse(str)
  if(authObj.expire <= new Date()) {
    removeToken()
    Message.warning("登录状态已过期，请重新登录！")
    return null
  }
  return authObj.token
}

export function setToken(token, expire) {
  const authObj = {
    token: token,
    expire: expire
  }
  const str = JSON.stringify(authObj)
    localStorage.setItem(TokenKey, str)
    sessionStorage.setItem(TokenKey, str)
}

export function removeToken() {
  localStorage.removeItem(TokenKey)
  sessionStorage.removeItem(TokenKey)
}
