import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/test/';

class UserService {
    processRequest(process_url,filename,token) {
        var url = API_URL + process_url;
        axios.get(url, {
            params: {
                filename: filename,
            },
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
            .then(response => {
                console.log(response);
                return response.data;
            })
            .catch(error => {
                return error;
            });
    }

    getUserBoard() {
        return axios.get(API_URL + 'user', { headers: authHeader() });
    }

    getModeratorBoard() {
        return axios.get(API_URL + 'mod', { headers: authHeader() });
    }

    getAdminBoard() {
        return axios.get(API_URL + 'admin', { headers: authHeader() });
    }
}

export default new UserService();
