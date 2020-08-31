import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import es from 'vuetify/es5/locale/es'
import en from 'vuetify/es5/locale/en'

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
            light:
                {
                    primary: '#019AA8',
                    primaryAccent: '#016D77',
                    secondary: '#582072',
                    secondaryAccent: '#8730AF',
                    error: '#FF5252',
                    warning: '#ff5722',
                    info: '#03a9f4',
                    success: '#8bc34a',
                    background: '#404040'
                }
        }
    },
    lang: {
        locales: { es, en },
        current: 'es',
    },
})