const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer:{
    // hotOnly:true,
    proxy:{
      "/api":{
        target:"http://localhost:8080",
        ws:true,
        changeOrigin: true,
        pathRewrite:{
          "^/api":""
        }
      },
      "/api2":{
        target:"http://localhost:8082",
        ws:true,
        changeOrigin: true,
        pathRewrite:{
          "^/api2":""
        }
      }
    }
  }
})
