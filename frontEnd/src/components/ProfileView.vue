<template>
  <v-main>
    <v-card v-if="currentUser" class="mx-2 my-2">
      <v-card-title>
        <v-icon>mdi-face</v-icon>
        <div class="mx-2"><span>Username:</span><span><strong>{{ currentUser.username }}</strong></span></div>
      </v-card-title>
      <v-card-text>
        Comience subiendo un dataset de los archivos soportados:<br/>
        <ul>
          <li>JSON para weka</li>
          <li>arff</li>
          <li>csv</li>
        </ul>
        <v-btn class="mx-2 my-2 primary" @click="goToDatasets">Subir Archivo</v-btn>
      </v-card-text>
    </v-card>
    <v-btn class="primaryAccent white--text mx-2 my-2" @click="logOut">LogOut</v-btn>
  </v-main>
</template>

<script>
import VueRouter from 'vue-router';
import Vue from 'vue';

Vue.use(VueRouter);


const login_path = process.env.VUE_APP_LOGIN_PATH;
const result_path = process.env.VUE_APP_RESULTS_PATH;
const dataset_path = process.env.VUE_APP_DATASETS_PATH;
const algorithm_path = process.env.VUE_APP_ALGORITHM_PATH;

export default {
  name: "ProfileView",
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push(login_path);
    }
  },
  methods: {
    goToAlgorithm() {
      this.$router.push(algorithm_path);
    },
    goToResult() {
      this.$router.push(result_path);
    },
    goToDatasets() {
      this.$router.push(dataset_path);
    },
    logOut() {
      this.$store.dispatch("auth/logout", this.user).then(
          () => {
            this.$router.push(login_path);
          },
          error => {
            this.loading = false;
            this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString();
          }
      );
    }
  }
}
</script>

<style scoped>

</style>