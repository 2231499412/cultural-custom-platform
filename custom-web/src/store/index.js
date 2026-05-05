import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value.role === 'admin')

  function setToken(t) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function setUserInfo(info) {
    userInfo.value = info
    localStorage.setItem('user', JSON.stringify(info))
  }

  function logout() {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return { token, userInfo, isLoggedIn, isAdmin, setToken, setUserInfo, logout }
})
