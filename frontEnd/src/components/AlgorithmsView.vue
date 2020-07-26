<template>
    <v-main>
        <v-row>
            <v-col></v-col>
            <v-col>
                <v-btn v-on:click="goToResults">Check Results</v-btn>
                <br/>
                Subir un dataset
                <v-form v-on:submit.prevent="uploadFile">
                    <v-file-input v-model="inputFile" type="file" dense multiple label="uploadDataset">

                    </v-file-input>
                    <v-btn v-if="inputFile != null" type="submit" rounded>Subir</v-btn>
                    <v-alert type="info" v-if="responseFileUpload" dense>{{responseFileUpload}}</v-alert>
                </v-form>
                <!-- DISABLED ATM-->
                Working on it: Descargar dataset filtrado
                <v-form disabled v-on:submit.prevent="retrieveFile">
                    <v-text-field dense prepend-icon="mdi-file"
                                  label="Dataset a procesar" v-model="inputFileName"></v-text-field>
                    <v-btn v-if="inputFileName != null" type="submit" rounded>Descargar</v-btn>
                    <v-alert v-if="responseFileDownload" type="info" dense>{{responseFileDownload}}</v-alert>
                </v-form>
                <!-- FINISH OF DISABLED ELEMENT-->
                Algoritmos de seleccion de atributos:
                <v-form v-on:submit.prevent="processAlgorithm">
                    <v-combobox
                            :items="datasets"
                            prepend-icon="mdi-file" dense v-model="inputFileNameProcess"
                            label="Dataset a procesar"/>

                    <div v-if="inputFileNameProcess">

                        Seleccion de atributos:
                        <v-combobox v-model="algorithm"
                                    :items="algorithms"/>
                        <v-btn v-if="algorithm" type="submit" rounded>Evaluar</v-btn>
                    </div>
                    <v-alert v-bind:type="responseProcessStatus" v-if="responseProcess" dense>{{responseProcess}}
                    </v-alert>
                </v-form>

            </v-col>
            <v-col></v-col>
        </v-row>
    </v-main>
</template>

<script>
    import axios from 'axios';
    import streamSaver from 'streamsaver';
    import Vue from 'vue';
    import VueRouter from 'vue-router';

    Vue.use(VueRouter);


    const login_path = process.env.VUE_APP_LOGIN_PATH;
    const result_path = process.env.VUE_APP_RESULTS_PATH;
    const server_url = process.env.VUE_APP_API_SERVER_URL;


    require('axios-debug-log');
    localStorage.debug = "axios";
    export default {
        name: 'AlgorithmsView',
        data: () => ({
            inputFile: null,
            inputFileName: null,
            inputFileNameProcess: null,
            responseFileUpload: null,
            responseFileDownload: null,
            responseProcess: null,
            responseProcessStatus: "success",
            algorithm: null,
            datasets: null,
            algorithms: [
                {text: "Fast Correlation Based Filter", value: "FCBF"},
                {text: "Variable neighbourhood search", value: "VNS"},
                {text: "Scatter Search", value: "Scs"},
                {text: "Naive Bayes", value: "NvB"}
                /**"FCBF","VNS","ScS","Nvb"

                 */
            ]
        }), computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        created() {
            if (!this.loggedIn) {
                this.$router.push(login_path);
            } else {
                let url = server_url + "/featureSelection/filesByUser/";
                return axios.get(url,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "Authorization": "Bearer " + this.$store.state.auth.user.token,
                        }
                    })
                    .then(response => {
                        response.data.forEach(elem => {
                            elem.value = elem.filename;
                            elem.text = elem.filename;
                        });
                        this.datasets = response.data;


                    })
                    .catch(error => {
                        console.log(error);

                    });
            }

        },
        methods: {
            goToResults() {
                this.$router.push(result_path);
            },
            uploadFile() {


                let i = 0;

                let obj = new FormData();
                console.log(this.inputFile[i]);
                const blob = new Blob([this.inputFile[i]], {
                    //type: 'application/json'
                    type: 'multipart/form-data'
                });
                obj.append("file", blob, this.inputFile[i].name);
                let url = server_url + "/fileManager/uploadDataset";
                axios.post(url,
                    obj,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "Authorization": "Bearer " + this.$store.state.auth.user.token,
                        }
                    })
                    .then(response => {
                        this.responseFileUpload = "Success download url: " + response.data;
                        this.datasets[this.datasets.size] = {
                            "value": this.inputFile[i].name,
                            "text": this.inputFile[i].name
                        };
                        console.log(this.datasets);
                        console.log({
                            "value": this.inputFile[i].name,
                            "text": this.inputFile[i].name
                        });
                        console.log(this.datasets.size);

                    })
                    .catch(error => {
                        this.responseFileUpload = error;
                    });

            },
            retrieveFile() {
                let url = server_url + "/fileManager/downloadFile/";
                console.log(url);
                axios.get(url, {
                    params: {
                        filename: this.inputFileName,
                    }
                })
                    .then(response => {
                        console.log(response);
                        const fileStream = streamSaver.createWriteStream(this.inputFileName, {
                            size: response.data.size,
                        })

                        this.responseFileDownload = response.data;
                        new Response('StreamSaver is awesome').body
                            .pipeTo(fileStream)
                    })
                    .catch(error => {
                        this.responseFileDownload = error;
                    });
            },
            processAlgorithm() {
                let process_path = null;
                switch (this.algorithm.value) {
                    case "FCBF":
                        process_path = "/featureSelection/fcbf";
                        break;
                    case "VNS":
                        process_path = "/featureSelection/vns";
                        break;
                    case "Scs":
                        process_path = "/featureSelection/Scs";
                        break;
                    case "NvB":
                        process_path = "/evaluate/naivebayes";
                        break;
                    default:
                        return null;
                }
                let url = server_url + process_path;
                console.log(url);
                let usuario = this.$store.state.auth.user;
                let filename = this.inputFileNameProcess.value;
                let data = [usuario, filename, url];
                this.$store.dispatch("process/sendRequest", data).then(
                    (response) => {
                        this.responseProcess = "Ejecucion finalizada " + response;
                        this.responseProcessStatus = "success";
                    },
                    error => {
                        this.loading = false;
                        console.log(error);
                        this.responseProcessStatus = "warning";
                        this.responseProcess =
                            error.message;
                    }
                );

            }
        }
    }
</script>
