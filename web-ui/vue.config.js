const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer:{
    // hotOnly:true,
    proxy:{
      "/bpi":{
        target:"http://localhost:8082",
        ws:true,
        changeOrigin: true,
        pathRewrite:{
          "^/bpi":""
        }
      },
      "/api":{
        target:"http://localhost:8080",
        ws:true,
        changeOrigin: true,
        pathRewrite:{
          "^/api":""
        }
      },

    }
  }
})
