import Vue from 'vue'
import App from '@/App.vue'
import store from '@/store/store'
import router from '@/router/index'
import { createSocket } from "@/xiaofei-im/xiaofei-im-js-sdk";
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';

Vue.config.productionTip = false;
Vue.prototype.$fim = createSocket(() => {
  return JSON.parse(localStorage.getItem("auth"));
});
Vue.use(ElementUI);
new Vue({
  render: h => h(App),
  store,
  router
}).$mount('#app');
