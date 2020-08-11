<template>
  <Page @loaded="on_load">
    <ActionBar>
      <GridLayout width="100%" columns="auto, *">
        <Label text="MENU" @tap="$refs.drawer.nativeView.showDrawer()" col="0"/>
        <Label class="title" text="Algorithms" col="1"/>
      </GridLayout>
    </ActionBar>

    <RadSideDrawer ref="drawer">
      <StackLayout ~drawerContent backgroundColor="#ffffff">
        <Label class="drawer-header" text="Drawer"/>

        <Label class="drawer-item" text="Home" @tap="goToApp"/>
        <Label class="drawer-item" text="Profile" @tap="goToProfile"/>
        <Label class="drawer-item" text="Results" @tap="goToResults"/>
      </StackLayout>

      <GridLayout ~mainContent columns="*" rows="*">
        <StackLayout v-if="this.datasets">
          <Label class="h2" text="Seleccionar un dataset:"/>
          <Label class="h3" text="Dataset a procesar"/>
          <ListPicker :items="datasetArray" :selectedIndex="datasetIndex"
                      @selectedIndexChange="selectedDatasetChanged"/>
          <Button v-if="!selectedDataset" @tap="selectedDataset=true" text="Seleccionar"></Button>

          <StackLayout v-if="selectedDataset">
            <Switch v-model="chooseAlgorithm"></Switch>

            <StackLayout v-if="chooseAlgorithm">
              <Label class="h3 font-weight-bold" text="Seleccion de atributos"/>
              <DropDown @selectedIndexChange="selectedAlgorithmChanged" v-model="algorithmIndex"
                        :selectedIndex="this.algorithmIndex"
                        :items="algorithms"/>
              <Button v-if="!selectedAlgorithm" @tap="selectedAlgorithm=true" text="Seleccionar"></Button>
              <Label v-if="selectedAlgorithm" :text="this.algorithmsValue[this.algorithmIndex]"></Label>
            </StackLayout>

            <StackLayout v-else>
              <Label class="h3 font-weight-bold" text="Clasificadores"/>
              <DropDown @selectedIndexChange="selectedClassifierChanged" :selectedIndex="classifierIndex"
                        :items="classifiers"/>
              <Button v-if="!selectedClassifier" @tap="selectedClassifier=true" text="Seleccionar"></Button>
              <Label v-if="selectedClassifier" :text="this.classifiersValue[this.classifierIndex]"></Label>
            </StackLayout>
            {{ this.datasetArray[this.datasetIndex] }}
            <Button v-if="selectedAlgorithm||selectedClassifier" @tap="processAlgorithm">Evaluar</Button>
            <Label v-if="responseProcess">
              {{ responseProcess }}</Label>

          </StackLayout>
        </StackLayout>
      </GridLayout>
    </RadSideDrawer>

  </Page>
</template>
<script>
import {mapGetters, mapActions} from 'vuex'
import axios from 'axios';
import * as config from '../config.js';
import {ApplicationSettings} from "@nativescript/core"


export default {
  data() {
    return {
      msg: '',
      selectedIndex: 1,
      inputFile: null,
      inputFileName: null,
      responseFileUpload: null,
      responseFileDownload: null,
      responseProcess: null,
      responseProcessStatus: "success",
      loading: true,
      datasets: null,
      chooseAlgorithm: true,
      datasetArray: null,
      selectedDataset: false, selectedAlgorithm: false, selectedClassifier: false,
      datasetIndex: 0, algorithmIndex: 0, classifierIndex: 0,
      algorithms: [
        "Fast Correlation Based Filter",
        "Variable neighbourhood search",
        "Scatter Search",
      ],
      algorithmsValue: [
        "fcbf", "vns", "Scs", "naivebayes"],
      classifiers: [
        "Naive Bayes",
      ],
      classifiersValue: [
        "NvB",
      ]
    }
  },
  computed: {
    ...mapGetters({
      getUser: 'auth/getUser',
      getToken: 'auth/getToken',
      getProcess: 'process/getProcess'
    })
  },
  methods: {
    ...mapActions({
      process_algorithm: 'process/processed',
      logout: 'auth/logout',
    }),
    on_load() {
      this.selectedDataset = false;
      this.selectedAlgorithm = false;
      this.selectedClassifier = false;
      if (this.loggedIn()) {
        let url = config.BACKEND + "/featureSelection/filesByUser/";
        return axios.get(url,
            {
              headers: {
                "Content-Type": "multipart/form-data",
                "Authorization": "Bearer " + this.getToken,
              }
            })
            .then(response => {
              this.datasetArray = [];
              response.data.forEach(elem => {
                elem.value = elem.filename;
                elem.text = elem.filename;
                this.datasetArray.push(elem.filename);
              });
              this.datasets = response.data;
              console.log(this.datasetArray);


            })
            .catch(error => {
              console.log(this.getToken);
              console.log(error);
              console.log(typeof error);

            });
      }
    },
    selectedDatasetChanged(args) {
      let picker = args.object;
      if (this.datasetIndex !== picker.selectedIndex) {
        this.selectedDataset = true;
        this.datasetIndex = picker.selectedIndex;
        console.log("Indice cambiado:" + this.datasetIndex);
      }
    },
    selectedAlgorithmChanged(args) {

      let picker = args.object;
      if (this.algorithmIndex !== picker.selectedIndex) {
        this.selectedAlgorithm = true;
        this.selectedClassifier = false;
        this.algorithmIndex = picker.selectedIndex;
      }
    },
    selectedClassifierChanged(args) {

      let picker = args.object;
      if (this.classifierIndex !== picker.selectedIndex) {
        this.selectedClassifier = true;
        this.selectedAlgorithm = false;
        this.classifierIndex = picker.selectedIndex;
      }
    },
    goToProfile() {
      this.$navigator.navigate('profile', {clearHistory: true});
    },
    goToResults() {
      this.$navigator.navigate('results', {clearHistory: true});
    },
    goToApp() {
      this.$navigator.navigate('/', {clearHistory: true});
    },
    loggedIn() {
      return this.getUser != null;


    },
    processAlgorithm() {
      let process_path;
      if (this.chooseAlgorithm && this.selectedAlgorithm) {
        process_path = "/featureSelection/" + this.algorithmsValue[this.algorithmIndex];
      } else if (!this.chooseAlgorithm && this.selectedClassifier) {
        process_path = "/evaluate/" + this.classifiersValue[this.classifierIndex];
      }
      let url = config.BACKEND + process_path;
      let filename = this.datasetArray[this.datasetIndex];
      console.log(url, filename);
      axios
          .get(url + "/" + filename, {
            headers: {
              "Authorization": "Bearer " + this.getToken,
            }
          })
          .then(response => {
            this.process_algorithm(response.data);
            this.responseProcess = "Ejecucion finalizada " + response;
            console.log(JSON.stringify(response));

          });

    }
  },
  logout() {
    this.logout();
    ApplicationSettings.remove("userData");
    this.$navigateTo(routes.LoginView);
  }

}

</script>
<style scoped>

.message {
  vertical-align: center;
  text-align: center;
  font-size: 20;
}

.drawer-header {
  padding: 50 16 16 16;
  margin-bottom: 16;
  font-size: 24;
  background: #2196f3
}

.drawer-item {
  padding: 8 16;
  font-size: 16;
}

.item-drop-down {
  font-size: 20;
  padding: 4;
  height: 50;
  width: 100%;
  border-width: 10;
  border-color: #000000;
  background-color: transparent;
}
</style>