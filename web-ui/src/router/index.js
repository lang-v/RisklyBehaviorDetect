import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/home/HomeView.vue'

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    // {
    //   path: '/project',
    //   name: 'project',
    //   component: () => import('../views/project/ProjectView'),
    //   children: [
    {
        path: '/project/list',
        name: 'list',
        component: () => import('../views/project/ListView')
    },
    {
        path: '/project/create',
        name: 'create',
        component: () => import('../views/project/CreateView')
    },
    {
        path: '/project/detect',
        name: 'detect',
        component: () => import('../views/project/DynamicDetect')
    },
    // ]
    // },
    // {
    //   path: '/log',
    //   name: 'log',
    //   // component: () => import('../views/log/EventLogView'),
    //   children:[
    {
        path: '/log/retrieval',
        name: 'retrieval',
        component: () => import('../views/log/RetrievalView')
    },
    {
        path: '/log/analyse',
        // name:'analyse',
        component: () => import('../views/log/AnalyseView')
    },
    //   ]
    // },
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
        component: () => import('../views/account/LoginView')
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
