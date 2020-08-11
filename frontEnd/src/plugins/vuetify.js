import Vue from 'vue';
import Vuetify from 'vuetify/lib';
/*
import es from 'vuetify/src/locale/es'
import en from 'vuetify/src/locale/en'
 */
Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
            light:
                {
                    primary: '#607d8b',
                    secondary: '#009688',
                    accent: '#4caf50',
                    error: '#673ab7',
                    warning: '#ff5722',
                    info: '#03a9f4',
                    success: '#8bc34a'
                }
        }
    }
    /*
    lang: {
        locales: { es, en },
        current: 'es',
    },
     */
})