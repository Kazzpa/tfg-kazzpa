import ProcessService from '../services/process.service';

const responseProcess = null;
const processState = responseProcess
    ? {status: {processed: true}, responseProcess}
    : {status: {processed: false}, responseProcess: null};

export const process = {
    namespaced: true,
    state: processState,
    actions: {
        sendRequest({commit}, payload) {
            return ProcessService.sendRequest(payload).then(
                response => {
                    commit('requestSuccess');
                    console.log("ha llegado",response);
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
        requestSuccess(state,response) {
            console.log("Request success:"+response);
            state.status.processed = true;
            state.responseProcess = response;
        },
        requestFailure(state) {
            state.status.loggedIn = false;
            state.user = null;
        },
    },
};
