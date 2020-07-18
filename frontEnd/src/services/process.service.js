import axios from 'axios';


class ProcessService {

    sendRequest(payload) {
        let usuario = payload[0];
        let filename = payload[1];
        let url = payload[2];
        return axios
            .get(url+"/"+filename, {
                headers: {
                    "Authorization": "Bearer " + usuario.token,
                }
            })
            .then(response => {
                return response.data;
            });
    }

}

export default new ProcessService();
