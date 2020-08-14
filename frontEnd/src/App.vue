<template>
  <v-app>

    <div>
      <vue-headful
          title="SelAtt"
          description="Aplicación para la selección de atributos"/>

    </div>
    <v-app-bar
        app
        color="primary"
        dark
    >
      <v-btn @click="goToHome" text class="d-flex">
        <v-img class="shadow" contain :src="require('./assets/tipografia2.png')" max-height="120" max-width="120"></v-img>
      </v-btn>

      <v-spacer/>
      <v-spacer/>

      <div class="d-none d-flex d-md-none">
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      </div>
      <div class="d-none d-md-flex">
        <v-btn v-if="this.$store.state.auth.user == null" v-on:click="goToLogin">
          Login
          <v-icon>mdi-login</v-icon>
        </v-btn>
        <div v-else>
          <v-btn class="mx-2" v-on:click="goToDatasets">Datasets</v-btn>
          <v-btn class="mx-2" v-on:click="goToResult">
            <v-badge
                color="green"
                content="6">
              Results
            </v-badge>
          </v-btn>
          <v-btn class="mx-2" v-on:click="goToProfile">
            Profile
            <v-icon>mdi-profile</v-icon>
          </v-btn>
          <v-btn class="mx-2" v-on:click="goToAlgorithm">
            Algoritmos
          </v-btn>

        </div>
      </div>
    </v-app-bar>

    <v-navigation-drawer
        v-model="drawer"
        absolute
        temporary
    >
      <v-list-item
          background-color="light-blue">
        <v-list-item-content>
          <v-list-item-title class="title">
            SelAtt
          </v-list-item-title>
          <v-list-item-subtitle>
            Navegación
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-divider></v-divider>
      <v-list>
        <div v-if="this.$store.state.auth.user == null">
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-login</v-icon>
            </v-list-item-icon>
            <v-list-item-content v-on:click="goToLogin">
              Login
            </v-list-item-content>
          </v-list-item>
        </div>
        <div v-else>
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-archive</v-icon>
            </v-list-item-icon>
            <v-list-item-content class="mx-2" v-on:click="goToDatasets">
              Datasets
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-login</v-icon>
            </v-list-item-icon>
            <v-list-item-content class="mx-2" v-on:click="goToAlgorithm">
              Algoritmos
            </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-login</v-icon>
            </v-list-item-icon>
            <v-list-item-content class="mx-2" v-on:click="goToResult">
              Resultados
            </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-login</v-icon>
            </v-list-item-icon>
            <v-list-item-content class="mx-2" v-on:click="goToProfile">
              Perfil
            </v-list-item-content>
          </v-list-item>
        </div>
      </v-list>

    </v-navigation-drawer>
    <router-view></router-view>
    <v-footer class="text-center">
      <v-col class="text-center">
        <a href="https://github.com/Kazzpa/tfg-kazzpa">
          <v-icon>mdi-github</v-icon>
          Kazzpa
        </a>
        <v-icon>mdi-copyright</v-icon>
        Copyright 2020.
      </v-col>
    </v-footer>
  </v-app>
</template>

<script>
import Vue from 'vue';
import VueRouter from 'vue-router';
import AlgorithmsView from '@/components/AlgorithmsView';
import RegisterView from "@/components/RegisterView";
import LoginView from "@/components/LoginView";
import ProfileView from "@/components/ProfileView";
import LandingView from "@/components/LandingView";
import ResultsView from "@/components/ResultsView";
import DatasetsView from "@/components/DatasetsView";
import MobileDocView from "@/components/MobileDocView";
import ApiDocView from "@/components/ApiDocView";
import WebDocView from "@/components/WebDocView";

Vue.use(VueRouter);
const profile_path = process.env.VUE_APP_PROFILE_PATH;
const register_path = process.env.VUE_APP_REGISTER_PATH;
const algorithm_path = process.env.VUE_APP_ALGORITHM_PATH;
const landing_path = process.env.VUE_APP_LANDING_PATH;
const login_path = process.env.VUE_APP_LOGIN_PATH;
const result_path = process.env.VUE_APP_RESULTS_PATH;
const dataset_path = process.env.VUE_APP_DATASETS_PATH;
const mobile_path = process.env.VUE_APP_MOBILE_PATH;
const api_path = process.env.VUE_APP_API_PATH;
const web_doc_path = process.env.VUE_APP_WEBDOC_PATH;


const router = new VueRouter({
  mode: "history",
  routes: [
    // dynamic segments start with a colon
    {path: login_path, component: LoginView},
    {path: profile_path, component: ProfileView},
    {path: algorithm_path, component: AlgorithmsView},
    {path: register_path, component: RegisterView},
    {path: result_path, component: ResultsView},
    {path: dataset_path, component: DatasetsView},
    {path: mobile_path, component: MobileDocView},
    {path: api_path, component: ApiDocView},
    {path: web_doc_path, component: WebDocView},
    //SET NEW VIEWS BEFORE LANDING PAGE
    {path: landing_path, component: LandingView},
  ]
})
export default {
  name: 'App',
  router,

  data: () => ({
    //
    drawer: null,
  }),
  created() {
    console.log(process.env.NODE_ENV);
    console.log(process.env.VUE_APP_API_SERVER_URL);
  },
  methods: {
    goToLogin() {
      router.push(login_path);
    },
    goToProfile() {
      router.push(profile_path);
    },
    goToHome() {
      router.push(landing_path);
    }
    ,
    goToResult() {
      router.push(result_path);
    },
    goToDatasets() {
      router.push(dataset_path);
    },
    goToAlgorithm() {
      this.$router.push(algorithm_path);
    },
  }
}

</script>
<style scoped>

.shadow {
  filter: drop-shadow(2px 2px 3px black);
}
</style>
