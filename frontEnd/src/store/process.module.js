import UserService from '../services/user.service';

const user = JSON.parse(localStorage.getItem('user'));
const initialState = response
    ? { status: { processed: true }, response }
    : { status: { processed: false }, response: null };

export const auth = {
    namespaced: true,
    state: initialState,
    actions: {
        sendRequest({ commit }, usuario) {
            return UserService.processRequest(usuario).then(
                response => {
                    commit('requestSuccess');
                    return Promise.resolve(response);
                },
                error => {
                    commit('requestFailure');
                    return Promise.reject(error);
                }
            );
        },
        }
    },
    mutations: {
        requestSuccess(state) {
            state.status.processed = true;
            state.responseProcess = response;
        },
        requestFailure(state) {
            state.status.loggedIn = false;
            state.user = null;
        },
    }
};
