import Vue from 'nativescript-vue';
import Vuex from 'vuex';

import { auth } from './auth.module';
import { process} from "./process.module";

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        auth,
        process
    }
});