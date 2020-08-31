<template>
  <v-main>

    <v-row class="mx-10">
      <v-col></v-col>
      <v-col class="justify-center">
        <div class="text-h3">
          Primeros pasos
        </div>
        Crear una cuenta:<br/>
        Para utilizar SelAtt es necesario disponer de una cuenta.<br/>
        <v-btn class="mx-2 my-2 primary" @click="goToLogin">LogIn</v-btn>
        <br/>
        Suba un dataset de los archivos soportados:<br/>
        <ul>
          <li>JSON para weka</li>
          <li>arff</li>
          <li>csv</li>
        </ul>
        <v-btn class="mx-2 my-2 primary" @click="goToDatasets">Subir Archivo</v-btn>
        <div class="text-h5">Seleccione uno de los algoritmos disponibles</div>
        <p>Recuerde que puede clasificar sus datasets una vez sean filtrado por uno de los siguientes algoritmos.</p>
        <v-btn class="mx-2 my-2 primary" @click="goToAlgorithms">Ir a Algoritmos</v-btn>
        <div class="text-h6">
          Algoritmos de selección de atributos
        </div>
        <ul>
          <li>Naive Bayes</li>
          <li>Naive Bayes Net</li>
          <li>Hidden Naive bayes</li>
          <li>Ibk</li>
          <li>Multi Layer Perceptron</li>
        </ul>
        <div class="text-h6">Algoritmos de clasificación
        </div>
        <ul>
          <li>Variable Neighbourhood Search</li>
          <li>Fast Correlation Based Filter</li>
          <li>Ranker</li>
          <li>Scatter Search</li>
          <li>Exhaustive Search</li>
        </ul>
        <p>La mayoria de los algoritmos son de la libreria de data mining
          <a @click="dialog = true">Weka
            <v-img contain src="https://waikato.github.io/weka-site/images/weka.png" max-height="40" max-width="40"/>
          </a>
        </p>
        <v-dialog v-model="dialog" max-width="30vh">
          <v-card >
            <v-card-title class="headline">Atención</v-card-title>
            <v-card-text>Va a acceder a una pagina externa a SelAtt, verifique la url previamente.</v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="primary" text @click="dialog = false">Cancelar</v-btn>
              <v-btn color="secondary"
                     target="_blank" rel="noopener noreferrer" href="https://www.cs.waikato.ac.nz/ml/weka/"
                     @click="dialog = false">Acceder
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <div class="text-h6">Podra comprobar el resultado en la pantalla de Resultados</div>
        <v-btn class="mx-2 my-2 primary" @click="goToResults">Ir a Resultados</v-btn>
      </v-col>
      <v-col></v-col>
    </v-row>
  </v-main>
</template>

<script>
import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const login_path = process.env.VUE_APP_LOGIN_PATH;
const dataset_path = process.env.VUE_APP_DATASETS_PATH;
const algorithm_path = process.env.VUE_APP_ALGORITHM_PATH;
const result_path = process.env.VUE_APP_RESULTS_PATH;
export default {
  name: "WebDocView",
  data: () => {
    return {
      dialog: false,
    }
  },
  methods: {
    goToLogin() {
      this.$router.push(login_path);
    },
    goToDatasets() {
      this.$router.push(dataset_path);
    },
    goToAlgorithm() {
      this.$router.push(algorithm_path);
    },
    goToResults() {
      this.$router.push(result_path);
    },
  }
}
</script>

<style scoped>

</style>