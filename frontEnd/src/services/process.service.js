import axios from 'axios';


class ProcessService {

    sendRequest(payload) {
        let user = payload[0];
        let filename = payload[1];
        let url = payload[2];
        return axios
            .get(url + "/" + filename, {
                headers: {
                    "Authorization": "Bearer " + user.token,
                }
            })
            .then(response => {
                return response.data;
            });
    }

    sendFilteredRequest(payload) {
        let user = payload[0];
        let file = payload[1];
        let url = payload[2];
        return axios
            .post(url, {
                performed: file.performed,
                attributesSelected: file.attributesSelected,
                algorithm: file.algorithm,
            }, {

                headers: {
                    "Authorization": "Bearer " + user.token,
                }
            })
            .then(response => {
                return response.data;
            });
    }

}

export default new ProcessService();
