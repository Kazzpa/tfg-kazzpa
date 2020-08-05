<template>
  <Page>
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
        <StackLayout>
          <Label class="h2" text="Seleccionar un dataset:"/>
          <Label class="h3" text="Dataset a procesar"/>
          <DropDown :items="datasetArray" :selectedIndex="datasetIndex"
                    @selectedIndexChange="selectedDatasetChanged"/>

          <StackLayout v-if="selectedDataset">
            <Switch v-model="chooseAlgorithm"></Switch>
            <StackLayout v-if="chooseAlgorithm">
              <Label class="h3 font-weight-bold" text="Seleccion de atributos"/>
              <DropDown @selectedIndexChange="selectedAlgorithmChanged" v-model="algorithm"
                        :items="algorithms"/>
              <Label v-if="selectedAlgorithm" :text="this.algorithmsValue[this.algorithmIndex]"></Label>
            </StackLayout>
            <StackLayout v-else>
              <Label class="h3 font-weight-bold" text="Clasificadores"/>
              <DropDown @selectedIndexChange="selectedClassifierChanged" :selectedIndex="classifierIndex"
                        :items="classifiers"/>
              <Label v-if="selectedClassifier" :text="this.classifiersValue[this.classifierIndex]"></Label>
            </StackLayout>
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
import routes from "./routes";
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
      datasetIndex: 0, algorithm: null, classifierIndex: 0,
      algorithms: [
        "Fast Correlation Based Filter",
        "Variable neighbourhood search",
        "Scatter Search",
      ],
      algorithmsValue: [
        "FCBF", "VNS", "ScS", "Nvb"],
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
    })
  },
  created() {
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
            console.log(error);
            console.log(typeof error);
            /*if (error.includes("401")) {
              this.logout();
              ApplicationSettings.remove("userData");
              this.$navigateTo(routes.App);
            }

             */

          });
    }
  },
  methods: {
    ...mapActions({
      process_algorithm: 'process/processed',
      logout: 'auth/logout',
    }),
    selectedDatasetChanged() {
      this.selectedDataset = true;
    },
    selectedAlgorithmChanged() {
      this.selectedAlgorithm = true;
    },
    selectedClassifierChanged() {
      this.selectedClassifier = true;
    },
    goToProfile() {
      this.$navigateTo(routes.ProfileView);
    },
    goToResults() {
      this.$navigateTo(routes.ResultsView);
    },
    goToApp() {
      this.$navigateTo(routes.App);
    },
    loggedIn() {
      return this.getUser != null;


    },
    processAlgorithm() {
      let process_path;
      if (this.chooseAlgorithm && this.selectedAlgorithm) {
        process_path = this.algorithmsValue[this.algorithm];
      } else if (!this.chooseAlgorithm && this.selectedClassifier) {
        process_path = this.classifiersValue[this.classifierIndex];
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