import axios from 'axios';


class AuthService {

    login(user) {
        var login_path = process.env.VUE_APP_API_AUTH_LOGIN;
        var API_URL = process.env.VUE_APP_API_SERVER_URL;
        return axios
            .post(API_URL + login_path, {
                username: user.username,
                password: user.password
            })
            .then(response => {
                if (response.data.accessToken) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }

    register(user) {

        var register_path = process.env.VUE_APP_API_AUTH_REGISTER;
        var API_URL = process.env.VUE_APP_API_SERVER_URL;
        return axios
            .post(API_URL + register_path, {
                username: user.username,
                password: user.password,
                email: user.email,
            })
            .then(response => {
                if (response.data.accessToken) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }
}

export default new AuthService();
