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
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/question": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/setting": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/image": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/user": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/rate": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/cookie": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/yahooOrder": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/MerOrder": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/good": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/txRecord": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/account": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/contact": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/admin": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
      "/api": {
        target: "https://107.151.206.75/",
        changeOrigin: true,
      },
    }
  }
})