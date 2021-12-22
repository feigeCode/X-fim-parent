import Vue from 'vue'
import App from '@/App.vue'
import store from '@/store/store'
import router from '@/router/index'
import XIAOFEI_IM from "@/xiaofei-im/xiaofei-im-vue-sdk";
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';

Vue.config.productionTip = false
Vue.prototype.$xfIm = XIAOFEI_IM;
Vue.use(ElementUI)
new Vue({
  render: h => h(App),
  store,
  router
}).$mount('#app')
