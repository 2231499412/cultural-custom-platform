import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/Home.vue') },
      { path: 'templates', name: 'TemplateList', component: () => import('../views/TemplateList.vue') },
      { path: 'template/:id', name: 'TemplateDetail', component: () => import('../views/TemplateDetail.vue') },
      { path: 'editor/:id', name: 'Editor', component: () => import('../views/Editor.vue') },
      { path: 'orders', name: 'OrderList', component: () => import('../views/OrderList.vue') },
      { path: 'profile', name: 'Profile', component: () => import('../views/Profile.vue') }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { requiresAdmin: true },
    children: [
      { path: '', name: 'AdminDashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'templates', name: 'AdminTemplates', component: () => import('../views/admin/TemplateManage.vue') },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/OrderManage.vue') },
      { path: 'users', name: 'AdminUsers', component: () => import('../views/admin/UserManage.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.path === '/login' || to.path === '/register') {
    next()
    return
  }

  if (!token && to.path !== '/') {
    next('/login')
    return
  }

  if (to.meta.requiresAdmin) {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    if (user.role !== 'admin') {
      next('/')
      return
    }
  }

  next()
})

export default router
