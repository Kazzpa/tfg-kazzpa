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


      <v-stepper-step editable :complete="e1 > 1" step="1">Selección de dataset</v-stepper-step>
      <v-stepper-content step="1">
        <h3>Subir un dataset:</h3>
        <v-form v-on:submit.prevent="uploadFile">
          <v-file-input v-model="inputFile" type="file" dense multiple label="uploadDataset">

          </v-file-input>
          <v-btn v-if="inputFile != null" class="secondary" type="submit" rounded>Subir</v-btn>
          <v-alert type="info" v-if="responseFileUpload" dense>{{ responseFileUpload }}
          </v-alert>
        </v-form>
        <div v-if="this.datasets">
          <h3>Seleccionar un dataset cargado previamente:</h3>
          <v-combobox
              :items="datasets"
              prepend-icon="mdi-file" dense v-model="inputFileNameProcess"
              label="Dataset a procesar"/>
        </div>
        <v-btn
            color="primary"
            @click="e1 = 2"
            :disabled="!(this.inputFileNameProcess)"
        >
          Continue
        </v-btn>
      </v-stepper-content>

      <v-stepper-step :complete="e1 > 2" step="2">Ejecución de algoritmo</v-stepper-step>
      <v-stepper-content step="2">
        <v-form v-if="inputFileNameProcess" v-on:submit.prevent="processAlgorithm">

          <v-row class="d-none d-sm-flex">
            <v-col cols="2" class="d-lg-flex" lg="2">
              <v-subheader>Dataset seleccionado</v-subheader>
            </v-col>
            <v-col sm="6" md="5" lg="4">
              <v-text-field disabled readonly v-model="this.inputFileNameProcess.filename"></v-text-field>
            </v-col>
          </v-row>

          <div class="d-sm-none d-flex">
            Dataset chico
            <br/>
            <v-text-field disabled readonly v-model="this.inputFileNameProcess.filename"></v-text-field>
          </div>

          <v-tabs v-model="tab">
            <v-tab v-on:click="this.algorithm = null">
              Seleccion de atributos
            </v-tab>
            <v-tab-item>
              Seleccione un algoritmo
              <v-row>
                <v-col sm="8" md="5" lg="4">
                  <v-combobox v-model="algorithm"
                              :items="algorithms"/>
                </v-col>

              </v-row>
              <v-btn v-if="algorithm" type="submit" rounded>Evaluar</v-btn>
            </v-tab-item>
            <v-tab v-on:click="this.algorithm = null">
              Clasificadores
            </v-tab>
            <v-tab-item>
              Seleccione un algoritmo
              <v-row>
                <v-col sm="8" md="5" lg="4">
                  <v-combobox v-model="algorithm"
                              :items="classifierAlgorithms"/>
                </v-col>

              </v-row>
              <v-btn v-if="classifierAlgorithms" type="submit" rounded>Evaluar</v-btn>
            </v-tab-item>
          </v-tabs>
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
    inputFileNameProcess: null,
    responseFileUpload: null,
    responseFileDownload: null,
    responseProcess: null,
    responseProcessStatus: "success",
    algorithm: null,
    datasets: null,
    tab: null,
    algorithms: [
      {text: "Fast Correlation Based Filter", value: "FCBF"},
      {text: "Variable neighbourhood search", value: "VNS"},
      {text: "Scatter Search", value: "Scs"},
      /**"FCBF","VNS","ScS","Nvb"

       */
    ],
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
      let filename = this.inputFileNameProcess.value;
      let data = [usuario, filename, url];
      this.$store.dispatch("process/sendRequest", data).then(
          (response) => {
            this.responseProcess = "Ejecucion finalizada " + response.jsonAttributes;
            this.responseProcessStatus = "success";
          },
          error => {
            this.loading = false;
            this.responseProcessStatus = "warning";
            this.responseProcess =
                error.message;
          }
      );

    }
  }
}
</script>
