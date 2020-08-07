export default {
    namespaced: true,
    state: {
        value: 'this is a text',
    },
    getters: {
        getProcess: state => state.value,
    },
    actions: {
        processed(store, text) {
            store.commit('processed', text)
        }

    },
    mutations: {
        processed(state, text) {
            state.value = text;
        }
    }
};