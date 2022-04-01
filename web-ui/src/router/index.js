import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/project',
    name: 'project',
    component: () => import('../views/ProjectView')
  },
  {
    path: '/log',
    name: 'log',
    component: () => import('../views/EventLogView')
  },
  {
    path: '/account',
    name: 'account',
    component: () => import('../views/AccountView')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterView')
  },
  {
    path: '/login',
    name: 'login',
    component: ()=> import('../views/LoginView')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
