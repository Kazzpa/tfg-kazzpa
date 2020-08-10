<template>
  <v-main>
    <v-card class="mx-2 my-2">
      <v-card-title>
        <div class="mx-2">Username: <strong>{{ currentUser.username }}</strong></div>
      </v-card-title>
      <v-card-text>
        <v-icon>mdi-face</v-icon>
        <div class="mx-2">Correo: {{ currentUser.email }}</div>
      </v-card-text>
    </v-card>
    <v-btn class="mx-2" @click="logOut">LogOut</v-btn>

    <v-btn class="mx-2" v-on:click="goToAlgorithm">Algoritmos</v-btn>
  </v-main>
</template>

<script>
import VueRouter from 'vue-router';
import Vue from 'vue';

Vue.use(VueRouter);


const login_path = process.env.VUE_APP_LOGIN_PATH;
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