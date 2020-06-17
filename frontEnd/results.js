Vue.use(VueMaterial.default);

var header = new Vue({
    el: '#header',
    data: {
        login: 'Acceso',
        about: 'Info',
        logo: 'https://cdn.iconscout.com/icon/free/png-256/vue-282497.png'
    }
});
var resultsTable = new Vue({
    el: '#results_table'
})