Vue.use(VueMaterial.default);
data: {
    sending: false;
};
var header = new Vue({
    el: '#header',
    data: {
        login: 'Acceso',
        about: 'Info',
        logo: 'https://cdn.iconscout.com/icon/free/png-256/vue-282497.png'
    }
});
var dat_form = new Vue({
    el: '#dataset_form',
    data: {
        sending: false,

    },
    methods: {
        saveDataset () {
            this.sending = true;

            // Instead of this timeout, here you can call your API
            window.setTimeout(() => {
                this.sending = false;
                this.clearForm();
            }, 1500)
        },
    }
});