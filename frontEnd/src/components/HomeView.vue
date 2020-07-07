<template>
    <v-row>
        <v-col></v-col>
        <v-col>
            <v-main>
                <v-form v-on:submit.prevent="uploadFile">
                    <v-file-input v-model="inputFile" type="file" dense multiple label="uploadDataset">

                    </v-file-input>
                    <v-btn v-if="inputFile != null" type="submit" rounded>Subir</v-btn>
                    <v-alert type="info" dense id="uploadAlert">{{responseFileUpload}}</v-alert>
                </v-form>
                <v-form v-on:submit.prevent="retrieveFile">
                    <v-text-field dense v-model="inputFileName" label="filename"></v-text-field>
                    <v-btn v-if="inputFileName != null" type="submit" rounded>Descargar</v-btn>
                    <v-alert type="info" dense id="downloadAlert">{{responseFileDownload}}</v-alert>
                </v-form>
                <v-form v-on:submit.prevent="retrieveNaiveBayes">
                    <v-text-field dense v-model="inputFileNameNaive" label="filename"></v-text-field>
                    <v-btn v-if="inputFileNameNaive != null" type="submit" rounded>Evaluar</v-btn>
                    <v-alert type="info" dense id="responseAlert">{{responseNaive}}</v-alert>
                </v-form>
            </v-main>
        </v-col>
        <v-col></v-col>
    </v-row>
</template>

<script>
    import axios from 'axios';
    import Papa from 'papaparse';
    import streamSaver from 'streamsaver';
    import Vue from 'vue';
    import VueRouter from 'vue-router';
    import RegisterView from "@/components/RegisterView";
    import ProfileView from "@/components/ProfileView";

    Vue.use(VueRouter);


    const server_url = process.env.VUE_APP_API_SERVER_URL;
    const profile_path =process.env.VUE_APP_PROFILE_PATH;
    const register_path =process.env.VUE_APP_REGISTER_PATH;

    const router = new VueRouter({
        routes: [
            // dynamic segments start with a colon
            {path: profile_path, component: ProfileView},
            {path: register_path, component: RegisterView}
        ]
    })
    require('axios-debug-log');
    localStorage.debug = "axios";
    export default {
        name: 'HelloWorld',
        router,
        data: () => ({
            inputFile: null,
            inputFileName: null,
            inputFileNameNaive: null,
            responseFileUpload: null,
            responseFileDownload: null,
            responseNaive: null,
        }),
        methods: {
            uploadFile() {


                let i = 0;
                Papa.parse(this.inputFile[i],
                    {
                        complete: (results) => {
                            this.loadedData(results, i);
                        },
                    },
                    {
                        delimiter: "\n"
                    });


            },
            loadedData(json, i) {
                let obj = new FormData();
                const blob = new Blob([json.data], {
                    type: 'application/json'
                });
                //sets the blob content to the file content as parsed with papaparse and the name of the file
                obj.append("file", blob, this.inputFile[i].name);
                let url = server_url + "/fileManager/uploadDataset";
                axios.post(url,
                    obj,
                    {
                        headers: {
                            "Access-Control-Allow-Origin": "*",
                            "Content-Type": "multipart/form-data"
                        }
                    })
                    .then(response => {
                        this.responseFileUpload = response.data;

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
            retrieveNaiveBayes() {
                let url = server_url + "/evaluate/naiveBayes";
                console.log(url);
                axios.get(url, {
                    params: {
                        filename: this.inputFileNameNaive,
                    }
                })
                    .then(response => {
                        console.log(response);
                        this.responseNaive = response.data;
                    })
                    .catch(error => {
                        this.responseNaive = error;
                    });
            }
        }
    }
</script>
