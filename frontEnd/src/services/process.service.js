import axios from 'axios';


class ProcessService {

    sendRequest(payload) {
        console.log("process service:");
        console.log(payload.user,payload.file,payload.url);
        return axios
            .get(payload.url, {
                filename: payload.file,
                headers: {
                    "authorization": "Bearer " + payload.user.token,
                }
            })
            .then(response => {
                return response.data;
            });
    }

}

export default new ProcessService();
