<template>
  <Page>
    <ScrollView>
      <StackLayout>
        <TabView :selectedIndex="selectedIndex">

          <TabViewItem title="Selección de dataset">
            <Label class="h4 font-weight-bold" text="Subir un dataset"/>
            <StackLayout>
              <!-- TODO: IMPLEMENT FILE INPUT IN ANDROID-->
              <Label text="Seleccionar un dataset cargado previamente:"/>
              <Label text="Dataset a procesar"/>
              <ListPicker
                  :items="datasets"
                  v-model="inputFileNameProcess"
              />
            </StackLayout>
          </TabViewItem>

          <TabViewItem title="Ejecución de algoritmo" :disabled="!(this.inputFileNameProcess)">
            <StackLayout v-if="inputFileNameProcess">
              <Label class="h4 font-weight-bold" text="Seleccion de atributos"/>
              <ListPicker v-model="algorithm" :items="algorithms"/>
              <Label text="Clasificadores"/>
              <ListPicker v-model="algorithm" :items="classifierAlgorithms"/>
              <Button v-if="algorithm" @tap="processAlgorithm">Evaluar</Button>
              <Label v-if="responseProcess">
                {{ responseProcess }}</Label>
            </StackLayout>
          </TabViewItem>
        </TabView>
      </StackLayout>
    </ScrollView>
  </Page>
</template>

<script>
import axios from 'axios';



const server_url = process.env.VUE_APP_API_SERVER_URL;


//require('axios-debug-log');
//localStorage.debug = "axios";
export default {
  name: 'AlgorithmsView',
  data: () => ({
    selectedIndex: 1,
    inputFile: null,
    inputFileName: null,
    inputFileNameProcess: null,
    responseFileUpload: null,
    responseFileDownload: null,
    responseProcess: null,
    responseProcessStatus: "success",
    algorithm: null,
    loading: true,
    datasets: null,
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
  }), computed: {
    loggedIn() {
      /**if (this.$store.state) {
        return this.$store.state.authº.loggedIn;
      }
       */
    }
  },
  created() {
    if (!this.loggedIn) {
      //Nada
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
      //this.$navigateTo(ResultsView);
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
            this.responseProcess = "Ejecucion finalizada " + response;
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
