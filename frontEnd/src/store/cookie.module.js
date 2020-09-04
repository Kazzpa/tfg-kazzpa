const cookies = JSON.parse(localStorage.getItem("cookies"));
if (cookies != null) {
    console.log(!!cookies);
    console.log(cookies.status.accepted, cookies.set);
}
const cookieState = cookies
    ? {status: {accepted: cookies.accepted}, set: true}
    : {status: {accepted: false}, set: false};

export const cookielaw = {
    namespaced: true,
    state: cookieState,
    actions: {
        acceptcookies({commit}) {
            commit('cookieAccepted');

        },
        cancelcookies({commit}) {
            commit('cookieCanceled');

        },
    },
    mutations: {
        cookieAccepted(state) {
            state.status.accepted = true;
            state.set = true;
            console.log(state.status.accepted, state.set);
            localStorage.setItem("cookies", JSON.stringify(state));
        },
        cookieCanceled(state) {
            state.status.accepted = false;
            state.set = true;
            console.log(state.status.accepted, state.set);
            localStorage.removeItem("cookies");
            localStorage.setItem("cookies", JSON.stringify(state));
        },
    },
};
