import request from '../utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

export function refreshToken(token) {
  return request({
    url: '/auth/refresh',
    method: 'post',
    headers: { Authorization: `Bearer ${token}` }
  })
}
