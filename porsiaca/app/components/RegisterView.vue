<template>
  <Page>
    <StackLayout>
      <Label>
        Registro
      </Label>
      <Label>
        <Label name="form" @submit.prevent="handleRegister">
          <div v-if="!successful">
            <TextField
                v-model="user.username"
                v-validate="'required|min:3|max:20'"
                type="text"
                class="form-control"
                name="username"
                label="Usuario"
            />
            <div
                v-if="submitted && errors.has('username')"
                class="alert-danger"
            >{{ errors.first('username') }}
            </div>
            <TextField
                v-model="user.email"
                v-validate="'required|email|max:50'"
                type="email"
                class="form-control"
                name="email"
                label="Correo"
            />
            <div
                v-if="submitted && errors.has('email')"
                class="alert-danger"
            >{{ errors.first('email') }}
            </div>
            <TextField
                v-model="user.password"
                v-validate="'required|min:6|max:40'"
                class="form-control"
                name="password"
                label="Contraseña"
                type="password"
            />

            <div
                v-if="submitted && errors.has('password')"
                class="alert-danger"
            >{{ errors.first('password') }}
            </div>
            <button type="submit">Sign Up</button>
          </div>
        </Label>

        <div
            v-if="message"
            class="alert"
            :class="successful ? 'alert-success' : 'alert-danger'"
        >{{ message }}
        </div>
      </Label>
    </StackLayout>
  </Page>
</template>

<script>
import User from "../models/user";

import LoginView from "./LoginView";
import ProfileView from "./ProfileView";

export default {
  name: "RegisterView",
  data() {
    return {
      user: new User('', '', ''),
      submitted: false,
      successful: false,
      message: ''
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  mounted() {
    if (this.loggedIn) {
      this.$navigateTo(ProfileView);
    }
  },
  methods: {
    goToLogin() {
      this.$navigateTo(LoginView);
    },
    handleRegister() {
      this.message = '';
      this.submitted = true;
      this.$store.dispatch('auth/register', this.user).then(
          data => {

            this.message = "Registrado con exito" + data.username;
            this.successful = true;
            this.goToLogin();
          },
          error => {
            this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString();
            this.successful = false;
          }
      );
    }
  }
}
</script>

<style scoped>

</style>