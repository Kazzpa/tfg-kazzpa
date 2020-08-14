<template>
  <v-main>
    <!-- TODO: REMOVE IN PROD-->
    <div> size:
      <div class="d-sm-none d-flex">xs</div>
      <div class="d-none d-sm-flex d-md-none">sm</div>
      <div class="d-none d-md-flex d-lg-none">md</div>
      <div class="d-none d-lg-flex d-xl-none">lg</div>
      <div class="d-none d-xl-flex">xl</div>
    </div>
    <v-stepper v-model="e1" vertical class="ma-12 ">

      <v-stepper-step editable :complete="e1 > 1" step="1">Seleccionar tipo de algoritmo</v-stepper-step>
      <v-stepper-content step="1">
        <v-radio-group v-model="algorithmType" @change="algorithmTypeChosen=true;loadDatasets()">
          <v-radio value="true" class="secondary--text" label="Selección de atributos"/>
          <v-radio value="false" label="Clasificadores"/>
        </v-radio-group>
        <v-btn
            color="primary"
            @click="e1 = 2"
            v-if="algorithmTypeChosen"
            :loading="this.datasetsLoaded<2"
        >
          Continue
        </v-btn>
      </v-stepper-content>
      <v-stepper-step editable :complete="e1 > 2" step="2">Selección de dataset</v-stepper-step>
      <v-stepper-content step="2">

        <h3>Subir un dataset:</h3>
        <v-form v-on:submit.prevent="uploadFile">
          <v-file-input v-model="inputFile" type="file" dense multiple label="Seleccionar Archivo">

          </v-file-input>
          <v-btn v-if="inputFile != null" class="secondary" type="submit" rounded>Subir</v-btn>
          <v-alert type="info" v-if="responseFileUpload" dense>{{ responseFileUpload }}
          </v-alert>
        </v-form>
        <div v-if="this.featureDatasets">
          <h3>Seleccionar un dataset cargado previamente:</h3>
          <div v-if="this.algorithmType ==='true'">
            <v-combobox
                :items="featureDatasets"
                prepend-icon="mdi-file" dense v-model="filenameProcess"
                label="Dataset a procesar"/>
          </div>
          <div v-else>
            <v-combobox
                :items="classifierDatasets"
                prepend-icon="mdi-file" dense v-model="filenameProcess"
                label="Dataset a procesar"/>
          </div>
        </div>
        <v-btn
            color="primary"
            @click="e1 = 3"
            :disabled="!(this.filenameProcess)"
        >
          Continuar
        </v-btn>
      </v-stepper-content>

      <v-stepper-step :complete="e1 > 3" step="3">Ejecución de algoritmo</v-stepper-step>
      <v-stepper-content step="3">
        <v-form v-if="this.filenameProcess" v-on:submit.prevent="processAlgorithm">

          <v-row class="d-none d-sm-flex">
            <v-col cols="2" class="d-lg-flex" lg="2">
              <v-subheader>Dataset seleccionado</v-subheader>
            </v-col>
            <v-col sm="6" md="5" lg="4">
              <v-text-field disabled readonly v-model="this.filenameProcess.text"></v-text-field>
            </v-col>
          </v-row>

          <div class="d-sm-none d-flex">
            Dataset chico
            <br/>
            <v-text-field disabled readonly v-model="this.filenameProcess.text"></v-text-field>
          </div>

          <div v-if="this.algorithmType ==='true'">
            Seleccione un algoritmo de selección de atributos
            <v-row>
              <v-col sm="8" md="5" lg="4">
                <v-combobox v-model="algorithm"
                            :items="algorithms"/>
              </v-col>

            </v-row>
            <v-btn v-if="algorithm" type="submit" rounded>Evaluar</v-btn>
          </div>
          <div v-else>
            Seleccione un clasificador
            <v-row>
              <v-col sm="8" md="5" lg="4">
                <v-combobox v-model="algorithm"
                            :items="classifierAlgorithms"/>
              </v-col>

            </v-row>
            <v-btn v-if="classifierAlgorithms" type="submit" rounded>Evaluar</v-btn>
          </div>
          <v-alert v-bind:type="responseProcessStatus" v-if="responseProcess" dense>
            {{ responseProcess }}
          </v-alert>
        </v-form>


        <v-btn text @click="e1 = 1">Cancelar</v-btn>
      </v-stepper-content>

    </v-stepper>
    <br/>


    <!--  TODO: FIX SOMEDAY DISABLED ATM-->
    Working on it: Descargar dataset filtrado
    <v-form disabled v-on:submit.prevent="retrieveFile">
      <v-text-field dense prepend-icon="mdi-file"
                    label="Dataset a procesar" v-model="inputFileName"></v-text-field>
      <v-btn v-if="inputFileName != null" type="submit" rounded>Descargar</v-btn>
      <v-alert v-if="responseFileDownload" type="info" dense>{{ responseFileDownload }}</v-alert>
    </v-form>
    <!-- FINISH OF DISABLED ELEMENT-->
  </v-main>
</template>

<script>
import axios from 'axios';
import streamSaver from 'streamsaver';
import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);


const login_path = process.env.VUE_APP_LOGIN_PATH;
const server_url = process.env.VUE_APP_API_SERVER_URL;


require('axios-debug-log');
localStorage.debug = "axios";

export default {
  name: 'AlgorithmsView',
  data: () => ({
    e1: 1,
    inputFile: null,
    inputFileName: null,
    filenameProcess: null,
    responseFileUpload: null,
    responseFileDownload: null,
    responseProcess: null,
    responseProcessStatus: "success",
    algorithm: null,
    algorithmTypeChosen: false,
    datasetsLoaded: 0,
    featureDatasets: null,
    classifierDatasets: null,
    tab: null,
    algorithmType: true,
    algorithms: [
      {text: "Fast Correlation Based Filter", value: "FCBF"},
      {text: "Variable neighbourhood search", value: "VNS"},
      {text: "Scatter Search", value: "Scs"},
    ],
    algorithmsDict: {
      "FastCorrelationBasedFilter": "FCBF",
      "Naive Bayes": "NvB",
      "ScatterSearchV1": "Scs",
      "VariableNeighbourhoodSearch": "VNS",
    }
    ,
    classifierAlgorithms: [

      {text: "Naive Bayes", value: "NvB"},
    ]
  }),

  watch: {
    tab() {
      this.algorithm = null;
    },
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  created() {
    if (!this.loggedIn) {
      this.$router.push(login_path);
    }
  },
  methods: {
    loadDatasets() {
      //TODO: NEST AXIOS.GET CALLS AND CONVERT DATASETSLOADED TO BOOLEAN
      if (this.datasetsLoaded < 2) {
        let url = server_url + "/featureSelection/filesByUser/";
        axios.get(url,
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
              this.featureDatasets = response.data;
              this.datasetsLoaded++;


            })
            .catch(error => {
              console.log(error.response);

            });

        url = server_url + "/evaluate/filesByUser/";
        axios.get(url,
            {
              headers: {
                "Content-Type": "multipart/form-data",
                "Authorization": "Bearer " + this.$store.state.auth.user.token,
              }
            })
            .then(response => {
              response.data.forEach(elem => {
                elem.value = elem.performed.filename + "-" + this.algorithmsDict[elem.algorithm.name];
                elem.text = elem.performed.filename + "-" + this.algorithmsDict[elem.algorithm.name];
              });
              console.log(response.data);
              this.classifierDatasets = response.data;
              this.datasetsLoaded++;


            })
            .catch(error => {
              console.log(error.response);

            });
      }
    },
    uploadFile() {
      let i = 0;

      let obj = new FormData();
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
            this.featureDatasets[this.featureDatasets.size] = {
              "value": this.inputFile[i].name,
              "text": this.inputFile[i].name
            };
            console.log(this.featureDatasets);
            console.log({
              "value": this.inputFile[i].name,
              "text": this.inputFile[i].name
            });
            console.log(this.featureDatasets.size);

          })
          .catch(error => {
            console.log(error.response);
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
            });

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
      let usuario = this.$store.state.auth.user;
      let isFeatureResult = this.algorithms.some(elem => {
        return this.filenameProcess.value.includes(elem.value);
      });
      if (isFeatureResult) {
        console.log("is feature result");
        url += "_filtered";
        let data = [usuario, this.filenameProcess, url];
        console.log(this.filenameProcess);
        this.$store.dispatch("process/sendFilteredRequest", data).then(
            (response) => {
              this.responseProcess = "Tasa de acierto : " + (response.correctlyClassified / response.numInstances) + "%";

              this.responseProcessStatus = "success";
            },
            error => {
              console.log(error.response);
              this.loading = false;
              this.responseProcessStatus = "warning";
              this.responseProcess =
                  error.message;
            }
        );
      } else {
        let filename = this.filenameProcess.value;
        let data = [usuario, filename, url];
        this.$store.dispatch("process/sendRequest", data).then(
            (response) => {
              if (process_path.includes("evaluate")) {
                this.responseProcess = "Tasa de acierto : " + (response.correctlyClassified / response.numInstances) + "%";
              } else {
                this.responseProcess = "Ejecucion Atributos seleccionados: " + response.attributesSelected;
              }
              this.responseProcessStatus = "success";
            },
            error => {
              console.log(error.response);
              this.loading = false;
              this.responseProcessStatus = "warning";
              this.responseProcess =
                  error.message;
            }
        );
      }

    }
  }

}
</script>
