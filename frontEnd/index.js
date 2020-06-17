Vue.use(VueMaterial.default);

var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello World!',
        login: 'LogIn',
        titulo: 'Bienvenido a Vue'
    }
});

var header = new Vue({
    el: '#header',
    data: {
        login: 'Acceso',
        about: 'Info',
        logo: 'https://cdn.iconscout.com/icon/free/png-256/vue-282497.png'
    }
});
