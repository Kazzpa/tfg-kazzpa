<template>
  <Page @loaded="on_load">
    <ActionBar>
      <NavigationButton text="Nav" android.systemIcon="ic_menu_more" @tap="$refs.drawer.nativeView.showDrawer()"/>
      <Label class="title" text="SelAtt - Auth"/>
    </ActionBar>

    <RadSideDrawer ref="drawer">
      <StackLayout ~drawerContent backgroundColor="#ffffff">
        <StackLayout class="drawer-header">
          <Label color="white" text="SelAtt"/>
        </StackLayout>
        <Label class="drawer-item" text="Profile" @tap="goToProfile"/>
        <Label class="drawer-item" text="Datasets" @tap="goToDatasets"/>
        <Label class="drawer-item" text="Home" @tap="goToApp"/>
      </StackLayout>

      <GridLayout ~mainContent columns="*" rows="*">
        <StackLayout>

          <Label v-if="resultChosen" v-model="resultChosen">
            <StackLayout>
              <Label>
                <FormattedString>
                  <span text="Resultado de:"></span>
                  <span :text="resultChosen.performed.filename"></span>
                  <span text="seleccionados:"></span>
                  <span :text="resultChosen.numAttributes"></span>
                  <span text=" atributos"></span>
                </FormattedString>
              </Label>
              <Button @tap="resultChosen = null">
                Cerrar
              </Button>
            </StackLayout>
          </Label>
          <Label class="h3 text-center">
            Resultados de ejecuciones previos
          </Label>
          <ListView v-if="resultsGrid != null" for="item in results">
            <v-template height="20">
              <StackLayout orientation="horizontal" height="20">
                <Label :text="item.performed.filename"/>
                <Label :text="item.algorithm.name"/>
                <Label :text="item.finishedDate"/>
                <!--@tap="itemButtonTapped(item)" -->
                <Button class="btn" text="Ver"/>
              </StackLayout>
            </v-template>
          </ListView>
          <SearchBar
              v-model="search"
          />
        </StackLayout>
      </GridLayout>
    </RadSideDrawer>

  </Page>
</template>
<script>

import {mapGetters, mapActions} from 'vuex';
import * as config from '../config.js';
import axios from 'axios';

var moment = require('moment'); // require
moment.locale("es");

export default {
  data() {
    return {
      search: '',
      headers: [
        {text: "Dataset", align: "start", value: "performed.filename"},
        {text: "Algoritmo seleccionado", value: "algorithm.name"},
        {text: 'Finished Date', value: 'finishedDate', sortable: true},
        {text: 'Attributos seleccionados', value: 'actions', sortable: false},
      ],
      results: null,
      resultChosen: null,
      resultsGrid: null,
    }
  }, computed: {
    ...mapGetters({
      getToken: 'auth/getToken',
      getUser: 'auth/getUser',
    }),
    loggedIn() {
      return this.getUser != null;
    }
  },
  methods: {
    goToLogin() {
      this.$navigator.navigate('login', {clearHistory: true});
    },
    goToProfile() {
      this.$navigator.navigate('profile', {clearHistory: true});
    },
    goToDatasets() {
      this.$navigator.navigate('datasets', {clearHistory: true});
    },
    goToAlgorithms() {
      this.$navigator.navigate('algorithms', {clearHistory: true});
    },
    goToApp() {
      this.$navigator.navigate('/', {clearHistory: true});
    },
    on_load() {
      if (!this.loggedIn) {
        this.$navigator.navigate('login', {clearHistory: true});
      } else {
        let url = config.BACKEND + "/featureSelection/results/";
        axios.get(url,
            {
              headers: {
                "Content-Type": "multipart/form-data",
                "Authorization": "Bearer " + this.getToken,
              }
            })
            .then(response => {
              response.data.forEach(elem => {
                elem.finishedDate = moment(new Date(elem.finishedDate)).fromNow();
              })
              console.log(response.data);
              this.results = response.data;
              this.genColumnsData();
            })
            .catch(error => {
              console.log(error);
            });
      }
    },
    genColumnsData() {
      var aux = [];
      var i = 0;
      var j = 0;
      this.results.forEach(elem => {
        aux.push({label: elem.performed.filename, id: i--, row: j++, col: 0});
      });
      console.log(aux);
      this.resultsGrid = aux;
    }
  },
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
  background: #2196f3;
  background-image: url("~/assets/images/backgroundDrawer.jpg");
  background-size: cover;
}

.drawer-item {
  padding: 8 16;
  font-size: 16;
}
</style>