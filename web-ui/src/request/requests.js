import axios from 'axios'//引入axios

//axios.create能创造一个新的axios实例
const server = axios.create({
    baseURL: "http://localhost:8080/", //配置请求的url
    timeout: 6000, //配置超时时间
    headers:{}//配置请求头
})

// // interceptors为axios的拦截器 如果我们想要在请求以前做些什么 这个时候就需要用到拦截器  视业务需求而定
// server.interceptors.request.use(function (config) {
//     //非登录的请求都加上一个请求头
//     console.log("请求拦截器已经执行");
//     return config;
// }, function (error) {
//     return Promise.reject(error);
// });

export default server
