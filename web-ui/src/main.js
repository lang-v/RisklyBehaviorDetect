import {createApp, reactive} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "axios";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App);
app.config.globalProperties.$axios=axios;

let userinfo = reactive({
    username: "None",
    userId: "",
    email: "",
    token: "",
    login:false
})
app.config.globalProperties.$userinfo = userinfo

app.use(store).use(ElementPlus).use(router).mount('#app');
