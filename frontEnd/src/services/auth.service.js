import axios from 'axios';


class AuthService {

    login(user) {
        var auth_path = process.env.VUE_APP_API_AUTH;
        var API_URL = process.env.VUE_APP_API_SERVER_URL;
        return axios
            .post(API_URL + auth_path, {
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
        //TODO: Create method in server
        console.log(user + " tried to register but is unavailable");
        return null;
        /**
         return axios.post(API_URL + 'signup', {
            username: user.username,
            email: user.email,
            password: user.password
        });
         */
    }
}

export default new AuthService();
