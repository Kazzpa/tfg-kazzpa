<template>
  <v-app>
    <div>
      <vue-headful
          title="SelAtt"
          description="Aplicación para la selección de atributos"/>

    </div>
    <v-app-bar
        app
        color="background"
        dark
    >
      <v-btn @click="goToHome" text class="d-flex">
        <v-img class="shadow" contain :src="require('./assets/tipografia2.png')" max-height="120"
               max-width="120"></v-img>
      </v-btn>

      <v-spacer/>
      <v-spacer/>

      <div class="d-none d-flex d-md-none">
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      </div>
      <div class="d-none d-md-flex">
        <v-btn class="secondaryAccent" v-if="this.$store.state.auth.user == null" v-on:click="goToLogin">
          Login
          <v-icon>mdi-login</v-icon>
        </v-btn>
        <div v-else>
          <v-btn class="secondary mx-2" v-on:click="goToDatasets">Datasets</v-btn>
          <v-menu
              :close-on-content-click="false" offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn class="secondary mx-2" dark v-bind="attrs" v-on="on">
                <v-badge color="success" :content="new_results_num" :value="new_results_num">
                  Results
                </v-badge>
              </v-btn>
            </template>
            <v-list>
              <v-list-item>
                <v-btn text @click="goToResult">
                  Resultados anteriores
                </v-btn>
              </v-list-item>
              <v-list-item v-if="this.new_results==null">
                <v-list-item-subtitle>No hay notificaciones nuevas.</v-list-item-subtitle>
              </v-list-item>
              <div v-else v-for="(result, index) in this.new_results" :key="index">
                <v-list-item>
                  <v-list-item-title v-if="result.correctlyClassified != null">{{
                      result.performed.filename
                    }} - Clasificado:
                    {{
                      Math.round(((result.correctlyClassified / result.numInstances) +
                          Number.EPSILON) *
                          10000) / 100
                    }}%
                  </v-list-item-title>
                  <v-list-item-title v-else>
                    {{ result.performed.filename }} -
                    {{ result.algorithm.name }} Filtrado
                  </v-list-item-title>
                  <v-list-item-action v-if="result.correctlyClassified != null"
                                      @click="setSeenResult(result,'/evaluate')">
                    <v-icon>mdi-close</v-icon>
                  </v-list-item-action>

                  <v-list-item-action v-else @click="setSeenResult(result,'/featureSelection')">
                    <v-icon>mdi-close</v-icon>
                  </v-list-item-action>
                </v-list-item>
              </div>
            </v-list>
          </v-menu>
          <v-btn class="secondary mx-2" v-on:click="goToAlgorithm">
            Algoritmos
          </v-btn>
          <v-btn class="secondaryAccent mx-2" v-on:click="goToProfile">
            {{ this.$store.state.auth.user.username }}
            <v-icon>mdi-account-box</v-icon>
          </v-btn>
        </div>
      </div>
    </v-app-bar>

    <v-navigation-drawer
        v-model="drawer"
        absolute
        temporary
    >
      <v-img src="@/assets/backgroundDrawer.jpg">
        <v-row align="end" class="lightbox white--text pa-2 fill-height">
          <v-col>
            <div class="text-h3">SelAtt</div>
            <div class="body-1">Navegación</div>
          </v-col>
        </v-row>
      </v-img>
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

          <v-list-item v-on:click="goToProfile">
            <v-list-item-icon>
              <v-icon>mdi-account-box</v-icon>
            </v-list-item-icon>
            <v-list-item-content text class="mx-2">
              Perfil
            </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
          <v-list-item v-on:click="goToAlgorithm">
            <v-list-item-icon>
              <v-icon>mdi-chevron-right</v-icon>
            </v-list-item-icon>
            <v-list-item-content text class="mx-2">
              Algoritmos
            </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
          <v-list-item v-on:click="goToResult">
            <v-list-item-icon>
              <v-icon>mdi-chevron-right</v-icon>
            </v-list-item-icon>
            <v-list-item-content text class="mx-2">
              Resultados
            </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
          <v-list-item v-on:click="goToDatasets">
            <v-list-item-icon>
              <v-icon>mdi-chevron-right</v-icon>
            </v-list-item-icon>
            <v-list-item-content text class="mx-2">
              Datasets
            </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
        </div>
      </v-list>

    </v-navigation-drawer>
    <router-view></router-view>
    <v-bottom-sheet
        bottom
        v-model="cookiesLaw"
        timeout="-1"
    >
      <v-sheet class="text-center py-5"
               min-height="150px">
        <div class="py-3">Las cookies se utilizan para mejorar la experiencia de uso de la aplicación, pero no
          son necesarias.
        </div>
        <div class="py-1">
          <v-btn
              small
              class="primary mx-2"
              @click="acceptCookies"
          >
            Aceptar
          </v-btn>
          <v-btn
              small
              class="secondary mx-2"
              @click="cancelCookies"
          >
            Cancelar
          </v-btn>
        </div>
      </v-sheet>
    </v-bottom-sheet>
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
import axios from "axios";

Vue.use(VueRouter);

const server_url = process.env.VUE_APP_API_SERVER_URL;
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
  ], scrollBehavior() {
    return {x: 0, y: 0}
  }

})
export default {
  name: 'App',
  router,

  data: () => ({
    new_results_retrieved: null,
    new_results: [],
    new_results_num: 0,
    drawer: null,
    delay_newResults: 5000,
  }),
  created() {
    console.log(process.env.NODE_ENV);
    console.log(process.env.VUE_APP_API_SERVER_URL);
    this.interval = setInterval(() => this.checkNewResults(), this.delay_newResults);
  },
  computed: {
    cookiesLaw() {
      return true;
      //return !this.$store.state.cookielaw.set;
    },
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
    },
    goToResult() {
      router.push(result_path);
    },
    goToDatasets() {
      router.push(dataset_path);
    },
    goToAlgorithm() {
      router.push(algorithm_path);
    },
    acceptCookies() {
      this.$store.dispatch("cookielaw/acceptcookies");
    },
    cancelCookies() {
      this.$store.dispatch("cookielaw/cancelcookies");
    },
    setSeenResult(item, type) {
      this.new_results_num -= 1;
      let index = this.new_results.indexOf(item);
      this.new_results.splice(index, 1);
      if (this.new_results_retrieved.length > 0) {
        this.new_results.push(this.new_results_retrieved.pop());
      }
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
    }
    , checkNewResults() {
      if (this.$store.state.auth.user != null) {
        let url = process.env.VUE_APP_API_SERVER_URL + "/evaluate/results/new";
        axios.get(url,
            {
              headers: {
                "Content-Type": "multipart/form-data",
                "Authorization": "Bearer " + this.$store.state.auth.user.token,
              }
            })
            .then(response => {
                  console.log(response.data);
                  this.new_results_retrieved = response.data;
                  this.new_results_num = response.data.length;

                  url = process.env.VUE_APP_API_SERVER_URL + "/featureSelection/results/new";
                  axios.get(url,
                      {
                        headers: {
                          "Content-Type": "multipart/form-data",
                          "Authorization": "Bearer " + this.$store.state.auth.user.token,
                        }
                      })
                      .then(response => {
                            console.log(response.data);
                            this.new_results_retrieved.push.apply(this.new_results_retrieved, response.data);
                            this.new_results_num += response.data.length;
                            var size = this.new_results_retrieved.length;
                            for (var i = 0; i < size && this.new_results.length < 5; i++) {
                              console.log(i,5);
                              this.new_results.push(this.new_results_retrieved.pop());
                            }
                            if (this.delay_newResults == 5000) {
                              this.delay_newResults = 100000;
                              clearInterval(this.interval);
                              this.interval = setInterval(() => this.checkNewResults(), this.delay_newResults);
                            }

                          }
                      );
                }
            );

      }
    }

  }
}


</script>
<style scoped>

.shadow {
  filter: drop-shadow(2px 2px 3px black);
}
</style>
