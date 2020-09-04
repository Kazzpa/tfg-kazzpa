import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import vueHeadful from 'vue-headful';

Vue.component('vue-headful', vueHeadful);
import store from './store';
import VeeValidate from 'vee-validate';
Vue.config.productionTip = false

Vue.use(VeeValidate);
new Vue({
  vuetify,
  store,
  render: h => h(App)
}).$mount('#app')
