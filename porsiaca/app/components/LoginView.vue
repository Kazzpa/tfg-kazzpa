<template>
  <Page>
    <StackLayout name="form" @submit.prevent="handleLogin">
      <TextField
          v-model="user.username"
          v-validate="'required'"
          type="text"
          name="username"
          label="Usuario"
      />
      <div
          v-if="errors.has('username')"
          class="alert alert-danger"
          role="alert"
      >Username is required!
      </div>
      <TextField
          v-model="user.password"
          v-validate="'required'"
          type="password"
          name="password"
          label="Contraseña"
      />
      <Label
          v-if="errors.has('password')"
          class="alert alert-danger"
          role="alert"
      >Password is required!
      </Label>
      <Button  :disabled="loading">
        <span v-show="loading" class="spinner-border spinner-border-sm"></span>
        <span>Login</span>
      </Button>
      <Button  v-on:click="goToRegister">Registro</Button>
      <Label v-if="message" class="alert alert-danger" role="alert">{{ message }}</Label>
    </StackLayout>
  </Page>
</template>

<script>
import User from '../models/user';
import ProfileView from "./ProfileView";
import RegisterView from "./RegisterView";

export default {
  name: "LoginView",
  data() {
    return {
      user: new User('', ''),
      loading: false,
      message: ''
    };
  }, computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  created() {
    if (this.loggedIn) {
      this.$navigateTo(ProfileView);
    }
  },
  methods: {
    goToRegister() {

      this.$navigateTo(RegisterView);
    },
    handleLogin() {
      this.loading = true;
      if (this.user.username && this.user.password) {
        console.log(this.user);
        this.$store.dispatch("auth/login", this.user).then(
            () => {
              this.$navigateTo(ProfileView);
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
}
</script>

<style scoped>

</style>