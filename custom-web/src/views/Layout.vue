<template>
  <div class="layout">
    <header class="header" :class="{ scrolled: isScrolled }">
      <div class="header-content">
        <router-link to="/" class="logo">
          <div class="logo-mark">
            <svg width="28" height="28" viewBox="0 0 28 28" fill="none">
              <rect x="2" y="2" width="24" height="24" rx="6" fill="var(--c-orange)" />
              <path d="M8 10h12M8 14h8M8 18h10" stroke="#fff" stroke-width="2" stroke-linecap="round" />
              <circle cx="21" cy="18" r="3" fill="var(--c-gold)" />
            </svg>
          </div>
          <span class="logo-text">造物记</span>
        </router-link>

        <nav class="nav-menu">
          <router-link to="/" class="nav-link">
            <span class="nav-link-text">首页</span>
          </router-link>
          <router-link to="/templates" class="nav-link">
            <span class="nav-link-text">模板中心</span>
          </router-link>
          <router-link to="/orders" v-if="userStore.isLoggedIn" class="nav-link">
            <span class="nav-link-text">我的订单</span>
          </router-link>
          <router-link to="/admin" v-if="userStore.isAdmin" class="nav-link">
            <span class="nav-link-text">后台管理</span>
          </router-link>
        </nav>

        <div class="header-right">
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand" trigger="click">
              <div class="user-badge">
                <div class="user-avatar">
                  {{ userStore.userInfo.nickname?.[0] || 'U' }}
                </div>
                <span class="user-name">{{ userStore.userInfo.nickname || userStore.userInfo.username }}</span>
                <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
                  <path d="M3 5l3 3 3-3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" />
                </svg>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <svg width="14" height="14" viewBox="0 0 14 14" fill="none" style="margin-right:8px"><circle cx="7" cy="5" r="3" stroke="currentColor" stroke-width="1.2"/><path d="M2 13c0-2.8 2.2-5 5-5s5 2.2 5 5" stroke="currentColor" stroke-width="1.2" stroke-linecap="round"/></svg>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="orders">
                    <svg width="14" height="14" viewBox="0 0 14 14" fill="none" style="margin-right:8px"><rect x="2" y="1" width="10" height="12" rx="2" stroke="currentColor" stroke-width="1.2"/><path d="M5 5h4M5 8h2" stroke="currentColor" stroke-width="1.2" stroke-linecap="round"/></svg>
                    我的订单
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <svg width="14" height="14" viewBox="0 0 14 14" fill="none" style="margin-right:8px"><path d="M5 2H3a1 1 0 00-1 1v8a1 1 0 001 1h2M9 10l3-3-3-3M12 7H6" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <button class="btn-ghost" @click="$router.push('/login')">登录</button>
            <button class="btn-primary" @click="$router.push('/register')">注册</button>
          </template>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>

    <footer class="footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <div class="logo">
            <div class="logo-mark">
              <svg width="24" height="24" viewBox="0 0 28 28" fill="none">
                <rect x="2" y="2" width="24" height="24" rx="6" fill="var(--c-orange)" />
                <path d="M8 10h12M8 14h8M8 18h10" stroke="#fff" stroke-width="2" stroke-linecap="round" />
                <circle cx="21" cy="18" r="3" fill="var(--c-gold)" />
              </svg>
            </div>
            <span class="logo-text">造物记</span>
          </div>
          <p class="footer-desc">每一件定制，都是独一无二的记忆</p>
        </div>
        <div class="footer-copy">
          © 2026 造物记 · 文创周边个性化定制平台
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store'

const router = useRouter()
const userStore = useUserStore()
const isScrolled = ref(false)

function onScroll() {
  isScrolled.value = window.scrollY > 10
}

onMounted(() => window.addEventListener('scroll', onScroll))
onUnmounted(() => window.removeEventListener('scroll', onScroll))

function handleCommand(command) {
  switch (command) {
    case 'profile': router.push('/profile'); break
    case 'orders': router.push('/orders'); break
    case 'logout':
      userStore.logout()
      router.push('/login')
      break
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* ---- Header ---- */
.header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 248, 240, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid transparent;
  transition: all 0.3s var(--ease-smooth);
}

.header.scrolled {
  background: rgba(255, 248, 240, 0.95);
  border-bottom-color: var(--c-border);
  box-shadow: 0 2px 16px rgba(45, 24, 16, 0.04);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* Logo */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-mark {
  display: flex;
  align-items: center;
}

.logo-text {
  font-family: var(--f-display);
  font-size: 22px;
  color: var(--c-brown);
  letter-spacing: 2px;
}

/* Nav */
.nav-menu {
  display: flex;
  gap: 8px;
}

.nav-link {
  position: relative;
  padding: 8px 18px;
  font-size: 15px;
  font-weight: 500;
  color: var(--c-text-secondary);
  border-radius: var(--r-sm);
  transition: all 0.25s var(--ease-smooth);
}

.nav-link:hover {
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.06);
}

.nav-link.router-link-exact-active {
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.1);
}

.nav-link.router-link-exact-active::after {
  content: '';
  position: absolute;
  bottom: 2px;
  left: 50%;
  transform: translateX(-50%);
  width: 16px;
  height: 3px;
  background: var(--c-orange);
  border-radius: 2px;
}

/* Right side */
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px 4px 4px;
  border-radius: 40px;
  cursor: pointer;
  transition: all 0.2s;
  color: var(--c-text);
}

.user-badge:hover {
  background: rgba(45, 24, 16, 0.04);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--c-orange), var(--c-orange-light));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
}

/* Buttons */
.btn-ghost {
  padding: 8px 20px;
  font-size: 14px;
  font-weight: 500;
  color: var(--c-text);
  background: none;
  border: none;
  cursor: pointer;
  border-radius: var(--r-sm);
  transition: all 0.2s;
  font-family: var(--f-body);
}

.btn-ghost:hover {
  background: rgba(45, 24, 16, 0.05);
}

.btn-primary {
  padding: 8px 24px;
  font-size: 14px;
  font-weight: 600;
  color: #fff;
  background: var(--c-orange);
  border: none;
  cursor: pointer;
  border-radius: var(--r-sm);
  transition: all 0.25s var(--ease-bounce);
  font-family: var(--f-body);
}

.btn-primary:hover {
  background: var(--c-orange-dark);
  transform: translateY(-1px);
  box-shadow: var(--shadow-warm);
}

/* ---- Footer ---- */
.footer {
  margin-top: auto;
  background: var(--c-brown);
  color: rgba(255, 248, 240, 0.7);
}

.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 24px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.footer .logo-text {
  color: rgba(255, 248, 240, 0.9);
  font-size: 18px;
}

.footer-desc {
  margin-top: 8px;
  font-size: 13px;
  color: rgba(255, 248, 240, 0.4);
}

.footer-copy {
  font-size: 13px;
  color: rgba(255, 248, 240, 0.3);
}

.main-content {
  flex: 1;
}
</style>
