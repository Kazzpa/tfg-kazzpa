<template>
  <v-main>
    <v-card>
      <v-card-title>
        Resultados de ejecuciones previos
      </v-card-title>
      <v-spacer/>
      <v-text-field
          v-model="search"
          append-icon="mdi-filter-variant"
          label="Filtrar"
          single-line
          hide-details
      />
      <v-data-table v-if="results != null" :headers="headers"
                    :items="results"
                    :search="search">
        <template v-slot:item.actions="{ item }">
          <v-icon
              small
              class="mr-2"
              @click="viewFeature(item)"
          >
            mdi-file-chart
          </v-icon>
        </template>
      </v-data-table>
      <v-dialog v-if="resultChosen" v-model="resultChosen">
        <v-card>
          <v-card-text>
          </v-card-text>
          <v-card-actions>
            <v-btn color="green darken-1" text @click="resultChosen = null">
              Cerrar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
    <v-card>
      <v-card-title>
        Clasificaciones previas
      </v-card-title>
      <v-spacer/>
      <v-text-field
          v-model="classifierSearch"
          append-icon="mdi-filter-variant"
          label="Filtrar"
          single-line
          hide-details
      />
      <v-data-table v-if="classifierResults != null" :headers="classifierHeaders"
                    :items="classifierResults"
                    :search="classifierSearch">
        <template v-slot:item.actions="{ item }">
          <v-icon
              small
              class="mr-2"
              @click="viewClassifier(item)"
          >
            mdi-file-chart
          </v-icon>
        </template>
      </v-data-table>
      <v-dialog v-if="resultChosen" v-model="resultChosen">
        <v-card>
          <v-card-title>
            Lista de attributos seleccionados
          </v-card-title>
          <v-card-text>'{{ resultChosen.attributesSelected }}'
          </v-card-text>
          <v-card-actions>
            <v-btn class="secondary" text @click="resultChosen = null">
              Cerrar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
  </v-main>
</template>

<script>
import axios from "axios";
import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

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
      {text: "Tamaño del dataset(bytes)", value: "performed.size"},
      {text: 'Fecha', value: 'finishedDate', sortable: true},
      {text: 'Attributos seleccionados', value: 'actions', sortable: false},
    ],
    classifierHeaders: [
      {text: "Dataset", align: "start", value: "performed.filename"},
      {text: "Algoritmo seleccionado", value: "algorithm.name"},
      {text: 'Fecha', value: 'finishedDate', sortable: true},
      {text: 'Instancias', value: 'numInstances', sortable: true},
      {text: 'Porcentaje de acierto', value: 'percentage', sortable: true},
      {text: 'Instancias correctamente clasificadas', value: 'correctlyClassified', sortable: true},
      {text: 'Mediana de error absoluto', value: 'meanAbsoluteError', sortable: true}
    ],
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
      this.classifierChosen = null;
    },
    viewClassifier(item) {
      this.classifierChosen = item;
      this.resultChosen = null;
    }
  },
  created() {
    if (!this.loggedIn) {
      this.$router.push(login_path);
    } else {
      let url = server_url + "/featureSelection/resultsByUser/";
      axios.get(url,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": "Bearer " + this.$store.state.auth.user.token,
            }
          })
          .then(response => {

            const options = {
              hour: 'numeric',
              minutes: 'numeric',
              weekday: 'long',
              year: 'numeric',
              month: 'numeric',
              day: 'numeric',
            };
            response.data.forEach(elem => {
              elem.finishedDate = new Date(elem.finishedDate).toLocaleString("es-ES", options);
              elem.percentage = (elem.correctlyClassified / elem.numInstances) * 100;
            })
            console.log(response.data);
            this.results = response.data;
          })
          .catch(error => {
            console.log(error);
            console.log(error.response);


          });

      url = server_url + "/evaluate/resultsByUser/";
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
              hour: 'numeric',
              minutes: 'numeric',
              weekday: 'long',
              year: 'numeric',
              month: 'numeric',
              day: 'numeric',
            };
            response.data.forEach(elem => {
              elem.finishedDate = new Date(elem.finishedDate).toLocaleString("es-ES", options);
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

</style>