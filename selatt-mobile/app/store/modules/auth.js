import User from "../../models/user";

export default {
    namespaced: true,
    state: {
        value: 'this is a text',
        user: new User('', '', '')
    },
    getters: {
        getText: state => state.value,
        getUser: state => state.user,
        getUsername: state => state.user.username,
        getToken: state => state.user.token,
    },
    actions: {
        test(store, text) {
            store.commit('SET_TEXT', text);
        },
        login(store, user, token) {
            store.commit('login_success', user, token);
        },
        login_fail(store, user) {
            store.commit('login_failed', user);
        }

    },
    mutations: {
        SET_TEXT(state, text) {
            state.value = text;
        },
        login_success(state, user, token) {
            delete (user.password);
            user.token = token;
            user.processStatus = 'Exito';
            state.user = user;
        },
        login_failed(state, user) {
            delete (user.password);
            user.processStatus = 'Fallido';

        }
    }
};
