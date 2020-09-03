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
            :loading="!this.datasetsLoaded"
        >
          Continue
        </v-btn>
      </v-stepper-content>
      <v-stepper-step :editable="algorithmTypeChosen" :complete="e1 > 2" step="2">Selección de dataset
      </v-stepper-step>
      <v-stepper-content step="2">

        <h3>Subir un dataset:</h3>
        <v-form v-on:submit.prevent="uploadFile">
          <v-file-input v-model="inputFile" type="file" dense multiple label="Seleccionar Archivo">

          </v-file-input>
          <v-btn v-if="inputFile != null" class="secondary" type="submit" rounded>Subir</v-btn>
          <v-alert class="mx-2 my-2" type="info" v-if="responseFileUpload" dense>{{ responseFileUpload }}
          </v-alert>
        </v-form>
        <div v-if="this.featureDatasets">
          <h3>Seleccionar un dataset cargado previamente:</h3>
          <div v-if="this.algorithmType ==='true'">
            <v-combobox class="d-inline-flex"
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
              <v-icon color="primary">mdi-file</v-icon>
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

          <div class="mx-2 my-2" v-if="this.algorithmType ==='true'">
            Seleccione un algoritmo de selección de atributos
            <v-row>
              <v-col sm="8" md="5" lg="4">
                <v-combobox v-model="algorithm"
                            :items="algorithms"/>
              </v-col>

            </v-row>
            <v-btn v-if="algorithm" :disabled="processing" :loading="processing" type="submit" rounded>Evaluar</v-btn>
            <v-btn text @click="e1 = 1">Cancelar</v-btn>
          </div>
          <div class="mx-2 my-2" v-else>
            Seleccione un clasificador
            <v-row>
              <v-col sm="8" md="5" lg="4">
                <v-combobox v-model="algorithm"
                            :items="classifierAlgorithms"/>
              </v-col>

            </v-row>
            <v-btn v-if="classifierAlgorithms" :disabled="processing" :loading="processing" type="submit"
                   rounded>Evaluar
            </v-btn>
            <v-btn text @click="e1 = 1">Cancelar</v-btn>
          </div>
          <v-alert class=" mx-2 my-2" v-bind:type="responseProcessStatus" v-if="responseProcess" dense>
            {{ responseProcess }}
          </v-alert>
        </v-form>


      </v-stepper-content>

    </v-stepper>
    <br/>


  </v-main>
</template>

<script>
import axios from 'axios';
import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);


const login_path = process.env.VUE_APP_LOGIN_PATH;
const server_url = process.env.VUE_APP_API_SERVER_URL;

if (process.env.NODE_ENV === "development") {
  require('axios-debug-log');
  localStorage.debug = "axios";
}

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
    datasetsLoaded: false,
    processing: false,
    featureDatasets: null,
    classifierDatasets: null,
    tab: null,
    algorithmType: true,
    algorithms: [
      {text: "Fast Correlation Based Filter", value: "FCBF"},
      {text: "Variable neighbourhood search", value: "VNS"},
      {text: "Scatter Search", value: "Scs"},
      {text: "Ranker", value: "Ranker"},
      {text: "Best First", value: "BestFirst"},
      {text: "Exhaustive", value: "Exhaustive"},
    ],
    algDict: {
      "FastCorrelationBasedFilter": "FCBF",
      "Naive Bayes": "NvB",
      "ScatterSearchV1": "Scs",
      "VariableNeighbourhoodSearch": "VNS",
      "Best First": "BestFirst",
      "Ranker": "Ranker",
      "Exhaustive": "exhaustive",
      "Ibk": "Ibk",
      "BayesNet": "bayesNet",
      "Hidden Naive Bayes": "hnb",
      "MultiLayer Perceptron": "hnb",
    },
    classifierAlgorithms: [
      {text: "Naive Bayes", value: "NvB"},
      {text: "Ibk", value: "Ibk"},
      {text: "BayesNet", value: "bayesNet"},
      {text: "Hidden Naive Bayes", value: "hnb"},
      {text: "MultiLayer Perceptron", value: "mlp"},
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
      if (!this.datasetsLoaded) {
        let url = server_url + "/featureSelection/datasets/";
        axios.get(url,
            {
              headers: {
                "Content-Type": "multipart/form-data",
                "Authorization": "Bearer " + this.$store.state.auth.user.token,
              }
            })
            .then(response => {
              url = server_url + "/evaluate/datasets/";
              axios.get(url,
                  {
                    headers: {
                      "Content-Type": "multipart/form-data",
                      "Authorization": "Bearer " + this.$store.state.auth.user.token,
                    }
                  })
                  .then(response => {
                    response.data.forEach(elem => {
                      elem.value = elem.performed.filename + "-" + this.algDict[elem.algorithm.name];
                      elem.text = elem.performed.filename + "-" + this.algDict[elem.algorithm.name];
                      if (!this.algDict[elem.algorithm.name]) {
                        console.log(elem.algorithm.name);
                      }
                    });
                    console.log(response.data);
                    this.classifierDatasets = response.data;
                    this.classifierDatasets.push.apply(this.classifierDatasets, this.featureDatasets);
                    this.datasetsLoaded = true;


                  })
                  .catch(error => {
                    console.log(error.response);

                  });
              response.data.forEach(elem => {
                elem.value = elem.filename;
                elem.text = elem.filename;
              });
              this.featureDatasets = response.data;


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
            this.responseFileUpload = "Subido archivo " + response.data.filename + " tamaño: " + response.data.size;
            this.featureDatasets.push.apply(this.featureDatasets, [
              {
                "value": this.inputFile[i].name,
                "text": this.inputFile[i].name,
              }]);

          })
          .catch(error => {
            console.log(error.response);
            this.responseFileUpload = error;
          });

    },
    processAlgorithm() {
      this.responseProcess = null;
      this.processing = true;
      let process_path = null;
      switch (this.algorithm.value) {
        case "FCBF":
          process_path = "/featureSelection/fcbf";
          break;
        case "VNS":
          process_path = "/featureSelection/vns";
          break;
        case "Scs":
          process_path = "/featureSelection/scs";
          break;
        case "Ranker":
          process_path = "/featureSelection/ranker";
          break;
        case "BestFirst":
          process_path = "/featureSelection/bestfirst";
          break;
        case "Exhaustive":
          process_path = "/featureSelection/exhaustive";
          break;
        case "NvB":
          process_path = "/evaluate/naivebayes";
          break;
        case "Ibk":
          process_path = "/evaluate/ibk";
          break;
        case "bayesNet":
          process_path = "/evaluate/bayesnet";
          break;
        case "hnb":
          process_path = "/evaluate/hnb";
          break;
        case "mlp":
          process_path = "/evaluate/mlp";
          break;
        default:
          this.responseProcess = "Error en la solicitud";
          this.processing = false;
          this.responseProcessStatus = "warning";
          return null;
      }
      let url = server_url + process_path;
      let user = this.$store.state.auth.user;
      let isFeatureResult = this.algorithms.some(elem => {
        return this.filenameProcess.value.includes(elem.value);
      });
      if (isFeatureResult) {
        url += "/filtered";
        let data = [user, this.filenameProcess, url];
        this.$store.dispatch("process/sendFilteredRequest", data).then(
            (response) => {
              this.processing = false;
              this.responseProcess = "Tasa de acierto : "
                  + Math.round(((response.correctlyClassified / response.numInstances) +
                      Number.EPSILON) *
                      10000) / 100 + "%";

              this.responseProcessStatus = "success";
            },
            error => {
              this.processing = false;
              console.log(error.response);
              this.responseProcessStatus = "warning";
              this.responseProcess =
                  error.message;
            }
        );
      } else {
        let filename = this.filenameProcess.value;
        console.log(filename)
        let data = [user, filename, url];
        this.$store.dispatch("process/sendRequest", data).then(
            (response) => {
              this.processing = false;
              if (process_path.includes("evaluate")) {
                this.responseProcess = "Tasa de acierto : "
                    + Math.round(((response.correctlyClassified / response.numInstances) +
                        Number.EPSILON) *
                        10000) / 100 + "%";
              } else {
                console.log(response);
                this.responseProcess = "Ejecucion Atributos seleccionados: " + response.attributesSelected;
                this.classifierDatasets.push.apply(this.classifierDatasets, [{
                  "value": filename + "-" + this.algorithm.value,
                  "text": filename + "-" + this.algorithm.value,
                  "performed": response.performed,
                  "attributesSelected": response.attributesSelected,
                  "algorithm": response.algorithm,
                }]);
              }
              this.responseProcessStatus = "success";
            },
            error => {
              this.processing = false;
              console.log(error.response);
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
