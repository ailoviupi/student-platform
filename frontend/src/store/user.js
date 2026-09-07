import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, logout } from '../api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  const isLoggedIn = computed(() => !!token.value)
  const role = computed(() => userInfo.value.role || '')
  const username = computed(() => userInfo.value.username || '')
  const realName = computed(() => userInfo.value.realName || '')

  async function loginAction(loginForm) {
    const res = await login(loginForm)
    token.value = res.data.token
    userInfo.value = res.data
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('userInfo', JSON.stringify(res.data))
    return res
  }

  function logoutAction() {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return {
    token, userInfo, isLoggedIn, role, username, realName,
    loginAction, logoutAction
  }
})
