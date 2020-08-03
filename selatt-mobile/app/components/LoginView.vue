<template>
  <Page>
    <StackLayout>
      <Image class="nt-form__logo" height="10%" src="~/assets/images/NativeScript-Vue.png"/>
      <StackLayout class="input-field">
        <TextField hint="Usuario" autocorrect="false" autocapitalizationType="none"
                   v-model="user.username"
                   class="input"
                   type="text"
                   name="username"
        />
      </StackLayout>
      <StackLayout class="input">
        <TextField
            hint="Contraseña" secure="true" autocorrect="false" autocapitalizationType="none"
            v-model="user.password"
            class="input"
        />
      </StackLayout>
      <Label v-if="message" class="alert alert-danger">{{ message }}</Label>
      <Button class="btn btn-primary -primary" @tap="handleLogin">
        <span v-show="loading" class="spinner-border spinner-border-sm"></span>
        <span>Login</span>
      </Button>
      <Button>Registro</Button>
    </StackLayout>
  </Page>
</template>
<script>
import User from "../models/user";
import ProfileView from "./ProfileView";

import axios from 'axios';
import {mapGetters, mapActions} from 'vuex';

export default {
  data() {
    return {
      user: new User('', ''),
      loading: false,
      message: '',
    }
  },
  computed: {
    ...mapGetters({
      getUser: 'auth/getUser'
    })
  },

  methods: {
    ...mapActions({
      login: 'auth/login',
      login_fail: 'auth/login_fail'
    }),
    handleLogin() {
      this.loading = true;
      this.message = '';
      console.log(this.user.username + " " + this.user.password);
      var user = this.user.username;
      var password = this.user.password;
      var url = "http://10.0.2.2:8082" + "/auth/login"
      console.log(url);
      try {
        axios.post(url, {
          username: user,
          password: password,
        }).then(response => {
          this.loading = false;
          console.log(response);
          console.log(response.data.accessToken);
          this.login(this.user, response.data.accessToken);
          this.message = "Acceso permitido";
          this.$navigateTo(ProfileView);
        }).catch(error => {
          console.log(error);
          this.loading = false;
          this.login_fail(this.user);
          this.message = error.message;
        });

      } catch (error) {
        this.loading = false;
        console.log(error);
      }
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
}

.drawer-item {
  padding: 8 16;
  font-size: 16;
}
</style>
