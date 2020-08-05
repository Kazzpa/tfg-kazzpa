export default {
    namespaced: true,
    state: {
        value: 'this is a text',
    },
    getters: {
        getText: state => state.value,
    },
    actions: {
        test(store, text) {
            store.commit('SET_TEXT', text);
        },
        processed(store, text) {
            store.commit('processed', text)
        }

    },
    mutations: {
        SET_TEXT(state, text) {
            state.value = text;
        },
        processed(state, text) {
            state.value = text;
        }
    }
};