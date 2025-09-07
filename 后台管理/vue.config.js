const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  publicPath: './',
  transpileDependencies: true,
  lintOnSave:false,
  devServer:{
    port: 8080,
    // 是否自动在浏览器打开
    open: false,
    // 是否开启 https
    https: false,
    proxy: {
      "/notice": {
        target: "https://fenlei.ddm-cu.com/",
        changeOrigin: true,
      },
      "/question": {
        target: "https://fenlei.ddm-cu.com/",
        changeOrigin: true,
      },
      "/setting": {
        target: "https://fenlei.ddm-cu.com/",
        changeOrigin: true,
      },
      "/image": {
        target: "https://fenlei.ddm-cu.com/",
        changeOrigin: true,
      },
      "/user": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/rate": {
        target: "https://fenlei.ddm-cu.com/",
        changeOrigin: true,
      },
      "/cookie": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/yahooOrder": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/MerOrder": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/good": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/txRecord": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/account": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/contact": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/admin": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/api": {
        target: "https://ddm-cu.com/",
        changeOrigin: true,
      },
      "/fenlei": {
        target: "https://fenlei.ddm-cu.com/",
        changeOrigin: true,
      },
      "/upload": {
        target: "https://fenlei.ddm-cu.com/",
        changeOrigin: true,
      },
      
    }
  }
})