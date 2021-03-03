// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import {http} from './plugins/'
/**
 * 全局通知插件
 * 可以在组件中使用 this.$notify({}) 来调用
 * 文档：https://github.com/euvl/vue-notification#readme
 */
import Notifications from 'vue-notification'
import velocity from 'velocity-animate'

Vue.use(http)
Vue.use(Notifications, {velocity})

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
