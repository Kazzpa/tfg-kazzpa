<template>
  <v-main>
    <v-card>
      <v-card-title>
        Datasets subidos
      </v-card-title>
      <v-spacer/>
      <v-text-field
          v-model="search"
          append-icon="mdi-filter-variant"
          label="Filtrar"
          single-line
          hide-details
      />
      <v-data-table v-if="datasets != null" :headers="headers"
                    :items="datasets"
                    :search="search">
        <template v-slot:item.actions="{ item }">
          <v-icon
              small
              class="mr-2"
              @click="viewItem(item)"
          >
            mdi-file-chart
          </v-icon>
        </template>
      </v-data-table>
      <v-dialog v-if="datasetChosen" v-model="datasetChosen">
        <v-card>
          <v-card-text>
            {{ this.datasetChosen.filename }}
          </v-card-text>
          <v-card-actions>
            <v-btn color="green darken-1" text @click="datasetChosen = null">
              Cerrar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
<v-spacer></v-spacer>
    <v-row>
      <v-col>

        <h3>Subir un dataset:</h3>
        <v-form v-on:submit.prevent="uploadFile">
          <v-file-input v-model="inputFile" type="file" dense multiple label="uploadDataset">

          </v-file-input>
          <v-btn v-if="inputFile != null" class="secondary" type="submit" rounded>Subir</v-btn>
          <v-alert type="info" v-if="responseFileUpload" dense>{{ responseFileUpload }}
          </v-alert>
        </v-form>
      </v-col>
    </v-row>
  </v-main>
</template>

<script>
import Vue from 'vue';
import VueRouter from 'vue-router';
import axios from "axios";


const login_path = process.env.VUE_APP_LOGIN_PATH;
const server_url = process.env.VUE_APP_API_SERVER_URL;

Vue.use(VueRouter);
export default {
  name: "DatasetsView",
  data: () => ({
    search: '',
    headers: [
      {text: "Dataset", align: "start", value: "name"},
      {text: "Nombre archivo", value: "filename"},
      {text: "Tamaño del dataset(bytes)", value: "size"},
      {text: 'Tipo archivo', value: 'fileType', sortable: true},
    ],
    inputFile: null,
    responseFileUpload: null,
    datasets: null,
    datasetChosen: null,
  }), computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  methods: {
    viewItem(item) {
      this.resultChosen = item;
    }, uploadFile() {
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
            console.log(response.data);
            this.datasets = response.data;
          })
          .catch(error => {
            console.log(error);

          });
    }
  }
}
</script>

<style scoped>

</style>