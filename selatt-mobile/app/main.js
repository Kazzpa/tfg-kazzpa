import Vue from 'nativescript-vue'
import App from './components/App'
import VueDevtools from 'nativescript-vue-devtools'

import store from './store';

if (TNS_ENV !== 'production') {
    Vue.use(VueDevtools)
}

// Prints Vue logs when --env.production is *NOT* set while building
Vue.config.silent = (TNS_ENV === 'production')

Vue.registerElement(
    'RadSideDrawer',
    () => require('nativescript-ui-sidedrawer').RadSideDrawer
)
Vue.registerElement(
    'DropDown',
    () => require('nativescript-drop-down').DropDown
)

import Navigator from 'nativescript-vue-navigator'
import { routes } from './routes';
Vue.use(Navigator, { routes })
new Vue({
    store,
    render: h => h(App),
}).$start()
