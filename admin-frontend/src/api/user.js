import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/account/auth/login',
    method: 'post',
    params:{
      username,
      password
    }
  })
}

export function getInfo(username) {
  return request({
    url: '/account/info',
    method: 'get',
    params: username
  })
}

export function logout() {
  return request({
    url: '/account/auth/logout',
    method: 'post'
  })
}
