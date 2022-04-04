import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/home',
    name: 'home',
    component: HomeView
  },
  {
    path: '/project',
    name: 'project',
    component: () => import('../views/project/ProjectView'),
    children: [
      {
        path:'list',
        name:'list',
        component:() => import('../views/project/ListView')
      },
      {
        path: 'create',
        name: 'create',
        component:() => import('../views/project/CreateView')
      }
    ]
  },
  {
    path:'/project_detail',
    name:'project_detail',
    component: () => import('../views/project/detail/DetailView'),
    children: [
      {
        path:'detect',
        name: 'detect',
        component: () => import('../views/project/detail/DynamicDetect')
      }
    ]

  },
  {
    path: '/log',
    name: 'log',
    component: () => import('../views/log/EventLogView'),
    children:[
      {
        path:'retrieval',
        name:'retrieval',
        component: () => import('../views/log/RetrievalView')
      },
      {
        path:'analyse',
        // name:'analyse',
        component: () => import('../views/log/AnalyseView')
      }
    ]
  },
  {
    path: '/account',
    name: 'account',
    component: () => import('../views/account/AccountView')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/account/RegisterView')
  },
  {
    path: '/login',
    name: 'login',
    component: ()=> import('../views/account/LoginView')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
