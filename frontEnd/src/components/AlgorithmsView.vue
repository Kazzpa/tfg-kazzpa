<template>
    <v-row>
        <v-col></v-col>
        <v-col>
            <v-main>
                Subir un dataset
                <v-form v-on:submit.prevent="uploadFile">
                    <v-file-input v-model="inputFile" type="file" dense multiple label="uploadDataset">

                    </v-file-input>
                    <v-btn v-if="inputFile != null" type="submit" rounded>Subir</v-btn>
                    <v-alert type="info" v-if="responseFileUpload" dense>{{responseFileUpload}}</v-alert>
                </v-form>
                Working on it: Descargar dataset filtrado
                <v-form v-on:submit.prevent="retrieveFile">
                    <v-text-field dense v-model="inputFileName" label="filename"></v-text-field>
                    <v-btn v-if="inputFileName != null" type="submit" rounded>Descargar</v-btn>
                    <v-alert v-if="responseFileDownload" type="info" dense>{{responseFileDownload}}</v-alert>
                </v-form>
                Algoritmos de seleccion de atributos:
                <v-form v-on:submit.prevent="processAlgorithm">
                    <v-text-field
                            prepend-icon="mdi-file" dense v-model="inputFileNameProcess"
                            label="Dataset a procesar"/>

                    <div v-if="inputFileNameProcess">

                        Seleccion de atributos:
                        <v-select v-model="algorithm"
                                  :items="algorithms"/>
                        <v-btn v-if="algorithm" type="submit" rounded>Evaluar</v-btn>
                    </div>
                    <v-alert type="info" v-if="responseProcess" dense>{{responseProcess}}</v-alert>
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

    Vue.use(VueRouter);


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
            algorithm: null,
            algorithms: [
                {text: "Fast Correlation Based Filter", value: "FCBF"},
                {text: "Variable neighbourhood search", value: "VNS"},
                {text: "Scatter Search", value: "ScS"},
                {text: "Naive Bayes", value: "NvB"}
                /**"FCBF","VNS","ScS","Nvb"

                 */
            ]
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
            processAlgorithm() {
                let process_path = null;
                switch (this.algorithm) {
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
                        process_path = "/evaluate/naiveBayes";
                        break;
                    default:
                        return null;
                }
                let url = server_url + process_path;
                console.log(url);
                console.log(this.$store.state.user.token);

            }
        }
    }
</script>
