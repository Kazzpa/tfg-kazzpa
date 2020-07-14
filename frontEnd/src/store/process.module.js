import ProcessService from '../services/process.service';

const response = false;
const processState = response
    ? {status: {processed: true}, response}
    : {status: {processed: false}, response: null};

export const process = {
    namespaced: true,
    state: processState,
    actions: {
        sendRequest({commit}, payload) {
            console.log("module:");
            console.log(payload);
            return ProcessService.sendRequest(payload).then(
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
    },
};
