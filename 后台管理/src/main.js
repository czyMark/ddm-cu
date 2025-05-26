import Vue from 'vue'
import router from './router'
import store from './store'
Vue.prototype.$store = store
import App from './App.vue'
import './style/main.css'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI)

import CKEditor from 'ckeditor4-vue';
Vue.use( CKEditor );

// import Vant from 'vant';
// import 'vant/lib/index.css';
// Vue.use(Vant);

Vue.config.productionTip = false


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')






























