import {createApp, reactive} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "axios";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import ck from "@/cookies/utils";

import VideoPlayer from 'vue-video-player'
require('vue-video-player/src/custom-theme.css')

const app = createApp(App);
app.config.globalProperties.$axios=axios;


// 每次初始化就从cookies中载入
// const key = 'userinfo'
// let userinfo = this.$cookies.get(key)
app.config.globalProperties.$userinfo = reactive({
    userid:'',
    username:'None',
    email:'',
    token:'',
    hasLogin:false
})
// app.config.globalProperties.$token = ''
app.config.globalProperties.$ck = ck
ck.refreshCookies(app.config.globalProperties)

// 路由拦截
router.beforeEach((to,from,next)=>{
    const allowPath = ['/']
    if (allowPath.includes(to.path)) {
        // 无论是否登录，首页都不拦截
        next()
        return
    }
    if (['/login','/register'].includes(to.path)) {
        if (ck.getCookie('token') === '') {
            next()
        }else {
            next({path: '/'})
        }
    }else {
        if (ck.getCookie('token') === '') {
            next('/login')
        }else {
            next()
        }
    }
})

app.use(store).use(VideoPlayer).use(ElementPlus).use(router).mount('#app');



