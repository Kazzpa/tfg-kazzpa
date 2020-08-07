import User from "../../models/user";

export default {
    namespaced: true,
    state: {
        user: new User('', '', '')
    },
    getters: {
        getUser: state => state.user,
        getUsername: state => state.user.username,
        getToken: state => state.user.token,
    },
    actions: {
        login(store, user) {
            store.commit('login_success', user);
        },
        login_fail(store, user) {
            store.commit('login_failed', user);
        },
        login_saved(store, userData) {
            store.commit('save_login', userData);
        },
        logout(store) {
            store.commit('logout');
        }

    },
    mutations: {
        login_success(state, user) {
            delete (user.password);
            user.processStatus = 'Logado';
            state.user = user;
        },
        login_failed(state, user) {
            delete (user.password);
            user.processStatus = 'Fallido';

        },
        save_login(state, userData) {
            state.user = new User(userData.username, userData.email, '');
            state.user.token = userData.token;
            state.user.processStatus = userData.processStatus;
        },
        logout(state) {
            state.user = new User('', '', '');
        }
    }
};
