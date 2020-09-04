import Vue from 'vue';
import Vuex from 'vuex';

import { auth } from './auth.module';
import { process} from "./process.module";
import { cookielaw } from './cookie.module';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        auth,
        process,
        cookielaw
    }
});