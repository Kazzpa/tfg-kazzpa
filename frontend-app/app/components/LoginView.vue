<template>
  <Page>
    <StackLayout class="form nt-form">
      <Image class="nt-form__logo" height="10%" src="~/assets/logo.png"/>
      <StackLayout class="input-field">
        <TextField hint="Usuario" autocorrect="false" autocapitalizationType="none"
                   v-model="user.username"
                   class="input"
                   type="text"
                   name="username"
                   label="Usuario"
        />
      </StackLayout>
      <StackLayout class="input">
        <TextField
            hint="Contraseña" secure="true" autocorrect="false" autocapitalizationType="none"
            v-model="user.password"
            class="input"
        />
      </StackLayout>
      <Button class="btn btn-primary -primary" @tap="handleLogin" :disabled="loading">
        <span v-show="loading" class="spinner-border spinner-border-sm"></span>
        <span>Login</span>
      </Button>
      <Button @tap="goToRegister">Registro</Button>
      <Label v-if="message" class="alert alert-danger" role="alert">{{ message }}</Label>
    </StackLayout>
  </Page>
</template>

<script>
import User from '../models/user';

import ProfileView from "./ProfileView";
//import RegisterView from "./RegisterView";
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
      console.log(this.$store.state);
      return false;
      //return this.$store.state.auth.status.loggedIn;
    }
  },
  mounted() {
    if (this.loggedIn) {
      //this.$navigateTo(ProfileView);
    }
  },
  methods: {
    goToRegister() {

      //this.$navigateTo(RegisterView);
    },
    handleLogin() {
      this.loading = true;
      if (this.user.username && this.user.password) {
        console.log("usuario" + this.user);
        console.log("store statte");
        console.log(this.$store.state);
        //this.$store.state.status.user = this.user;
        this.$navigateTo(ProfileView);

        this.$store.dispatch("auth/login", this.user).then(
            () => {
              console.log("store statte");
              console.log(this.$store.state);
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