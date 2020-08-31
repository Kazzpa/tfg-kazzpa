<template>
  <v-main>
    <div id="result">
      <v-card class="mx-2 my-2" v-if="resultChosen" v-model="resultChosen">
        <v-card-title>
          Selector de atributos {{ resultChosen.algorithm.name }}
        </v-card-title>
        <v-card-text class="text--primary">
          <p>Dataset: {{ resultChosen.performed.filename }} de {{ resultChosen.performed.size }}
            {{ resultChosen.performed.units }}</p>
          <p>Atributos: '{{ resultChosen.attributesSelected }}'</p>
        </v-card-text>
        <v-card-actions>
          <v-btn class="secondary" text @click="resultChosen = null">
            Cerrar
          </v-btn>
          <v-btn class="primary" @click="downloadCsv">CSV
            <v-icon>mdi-download</v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
      <v-card class="mx-2 my-2" v-if="classifierChosen" v-model="classifierChosen">
        <v-card-title>
          Clasificador {{ classifierChosen.algorithm.name }}
        </v-card-title>
        <v-card-text class="text--primary">
          <p> Dataset: {{ classifierChosen.performed.filename }} de {{ classifierChosen.performed.size }}
            {{ classifierChosen.performed.units }}</p>
          <div v-if="classifierChosen.featureAlgorithm !=null">
            <p>Filtrado con: {{ classifierChosen.featureAlgorithm.name }}</p>
          </div>
          <p> Numero de instancias:{{ classifierChosen.numInstances }}</p>
          <p>Instancias correctamente clasificadas: {{ classifierChosen.correctlyClassified }}</p>
          <p> Porcentaje de acierto: {{ classifierChosen.percentage }} %</p>
          <p>Mediana de Error absoluto: {{ classifierChosen.meanAbsoluteError }}</p>
        </v-card-text>
        <v-card-actions>
          <v-btn class="secondary" text @click="classifierChosen = null">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
    </div>
    <v-card class="mx-2 my-2">

      <v-toolbar flat color="primary" dark>
        <v-toolbar-title>Resultados</v-toolbar-title>
      </v-toolbar>
      <v-tabs vertical
              background-color="primary">
        <v-divider/>
        <v-tab class="justify-end">
          <div class="shadow">Selección de Atributos</div>
          <v-icon left>mdi-arrow-right</v-icon>
        </v-tab>
        <v-divider/>
        <v-tab class="justify-end">
          <div class="shadow">Clasificadores</div>
          <v-icon left>mdi-arrow-right</v-icon>
        </v-tab>
        <v-divider/>
        <v-tab-item>
          <div class="mx-2 my-2">
            <v-text-field
                v-model="search"
                append-icon="mdi-filter-variant"
                label="Filtrar"
                single-line
                hide-details
            />
            <v-data-table v-if="results != null" :headers="headers"
                          :items-per-page="5"
                          :items="results"
                          :search="search">
              <template v-slot:item.actions="{ item }">
                <v-btn outlined v-scroll-to="'#result'" @click="viewFeature(item)">
                  Ver
                </v-btn>
              </template>
            </v-data-table>
          </div>

        </v-tab-item>
        <v-tab-item>

          <div class="mx-2 my-2">
            <v-text-field
                v-model="classifierSearch"
                append-icon="mdi-filter-variant"
                label="Filtrar"
                single-line
                hide-details
            />
            <v-data-table v-if="classifierResults != null" :headers="classifierHeaders"
                          :items-per-page="5"
                          :items="classifierResults"
                          :search="classifierSearch">

              <template v-slot:item.percentage="{ item }">
                {{ item.percentage }}%
              </template>
              <template v-slot:item.actions="{ item }">

                <v-btn outlined v-scroll-to="'#result'" @click="viewClassifier(item)">
                  Ver
                </v-btn>
              </template>
            </v-data-table>

          </div>
        </v-tab-item>
      </v-tabs>
    </v-card>
  </v-main>
</template>

<script>
import axios from "axios";
import Vue from 'vue';
import VueRouter from 'vue-router';

var VueScrollTo = require('vue-scrollto');

Vue.use(VueScrollTo);
Vue.use(VueRouter);

Vue.use(VueScrollTo, {
  container: "body",
  duration: 700,
  easing: "ease-in-out",
  offset: 0,
  force: true,
  cancelable: true,
  onStart: false,
  onDone: false,
  onCancel: false,
  x: false,
  y: true
});
const login_path = process.env.VUE_APP_LOGIN_PATH;
const server_url = process.env.VUE_APP_API_SERVER_URL;


require('axios-debug-log');
localStorage.debug = "axios";

export default {
  name: "ResultsView",
  data: () => ({
    search: '',
    headers: [
      {text: "Dataset", align: "start", value: "performed.filename"},
      {text: "Algoritmo seleccionado", value: "algorithm.name"},
      {text: 'Fecha', value: 'finishedDate', sortable: true},
      {text: 'Attributos seleccionados', value: 'actions', sortable: false},
    ],
    classifierHeaders: [
      {text: "Dataset", align: "start", value: "filename"},
      {text: "Algoritmo seleccionado", value: "algorithm.name"},
      {text: 'Fecha', value: 'finishedDate', sortable: true},
      {text: 'Porcentaje de acierto', value: 'percentage', sortable: true},
      {text: 'Info', value: 'actions', sortable: true},
    ],
    algDict: {
      "FastCorrelationBasedFilter": "FCBF",
      "Naive Bayes": "NvB",
      "ScatterSearchV1": "Scs",
      "VariableNeighbourhoodSearch": "VNS",
      "Best First": "BestFirst",
      "Ranker": "Ranker",
      "Exhaustive Search": "exhaustive",
      "Ibk": "Ibk",
      "BayesNet": "bayesNet",
      "Hidden Naive Bayes": "hnb",
      "MultiLayer Perceptron": "hnb",
    },
    results: null,
    resultChosen: null,
    classifierSearch: null,
    classifierResults: null,
    classifierChosen: null,

  }), computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  methods: {
    viewFeature(item) {
      this.resultChosen = item;
      this.setSeenResult(item, "/featureSelection");
      this.classifierChosen = null;

    },
    viewClassifier(item) {
      this.classifierChosen = item;
      this.setSeenResult(item, "/evaluate");
      this.resultChosen = null;
    }, downloadCsv() {
      let csvContent = "data:text/csv;charset=utf-8,";
      csvContent += [this.resultChosen.attributesSelected
      ]
          .join("\n")
          .replace(/(^\[)|(\]$)/gm, "");

      const data = encodeURI(csvContent);
      const link = document.createElement("a");
      link.setAttribute("href", data);
      link.setAttribute("download", this.resultChosen.performed.filename + ".csv");
      link.click();
    },
    setSeenResult(item, type) {
      let url = server_url + type + "/result/seen";
      axios.post(url, {
        "id": item.id,
        "algorithm": item.algorithm,
        "performed": item.performed,
        "seen": item.seen,
      }, {
        headers: {
          "Authorization": "Bearer " + this.$store.state.auth.user.token,
        }
      })
    },
  },
  created() {
    if (!this.loggedIn) {
      this.$router.push(login_path);
    } else {
      let url = server_url + "/featureSelection/results/";
      axios.get(url,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": "Bearer " + this.$store.state.auth.user.token,
            }
          })
          .then(response => {

            const options = {
              weekday: 'long',
              month: 'long',
              year: 'numeric',
              day: 'numeric',
              hour: 'numeric',
              minute: 'numeric',
              second: 'numeric',
              hour12: false
            };
            response.data.forEach(elem => {
              elem.finishedDate = Intl.DateTimeFormat("es-ES", options).format(new Date(elem.finishedDate));
              if (elem.performed.size > 1024) {
                elem.performed.size = Math.round(((elem.performed.size + Number.EPSILON) / 1024) * 100) / 100;
                elem.performed.units = "Mb";
                if (elem.performed.size > 1024) {
                  elem.performed.size = Math.round(((elem.performed.size + Number.EPSILON) / 1024) * 100) / 100;
                  elem.performed.units = "Gb";
                }
              } else {
                elem.performed.units = "Kb";
              }
            })

            this.results = response.data;
          })
          .catch(error => {
            console.log(error);
            console.log(error.response);


          });

      url = server_url + "/evaluate/results/";
      axios.get(url,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": "Bearer " + this.$store.state.auth.user.token,
            }
          })
          .then(response => {
            console.log(response);
            const options = {
              weekday: 'long',
              month: 'short',
              year: 'numeric',
              day: 'numeric',
              hour: 'numeric',
              minute: 'numeric',
              second: 'numeric',
              hour12: false
            };
            response.data.forEach(elem => {
              let fecha = new Date(elem.finishedDate);
              if (elem.featureAlgorithm != null) {
                elem.filename = elem.performed.filename + "/" + this.algDict[elem.featureAlgorithm.name];
              } else {
                elem.filename = elem.performed.filename;
              }
              if (elem.performed.size > 1024) {
                elem.performed.size = Math.round(((elem.performed.size + Number.EPSILON) / 1024) * 100) / 100;
                elem.performed.units = "Mb";
                if (elem.performed.size > 1024) {
                  elem.performed.size = Math.round(((elem.performed.size + Number.EPSILON) / 1024) * 100) / 100;
                  elem.performed.units = "Gb";
                }
              } else {
                elem.performed.units = "Kb";
              }
              elem.finishedDate = Intl.DateTimeFormat("es-ES", options).format(fecha);
              elem.percentage = Math.round(((elem.correctlyClassified / elem.numInstances) +
                  Number.EPSILON) *
                  10000) / 100;
              elem.meanAbsoluteError = Math.round(elem.meanAbsoluteError * 10000 + Number.EPSILON) / 100;
            })
            this.classifierResults = response.data;
          })
          .catch(error => {
            console.log(error);
            console.log(error.response);

          });
    }
  }
}
</script>

<style scoped>
.shadow {
  filter: drop-shadow(2px 2px 3px black);
}
</style>