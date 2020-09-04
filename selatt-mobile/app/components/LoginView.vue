<template>
  <Page @loaded="on_load">
    <ActionBar>
        <NavigationButton text="Nav" android.systemIcon="ic_menu_more" @tap="$refs.drawer.nativeView.showDrawer()"/>
        <Label class="title" text="SelAtt - Auth" />
    </ActionBar>

    <RadSideDrawer ref="drawer">
      <StackLayout ~drawerContent backgroundColor="#ffffff">
        <StackLayout class="drawer-header">
          <Label color="white" text="SelAtt"/>
        </StackLayout>
      </StackLayout>

      <GridLayout ~mainContent columns="*" rows="*">
        <StackLayout>
          <Image class="nt-form__logo" height="20%" src="~/assets/images/logotipo.png"/>
          <StackLayout class="input-field">
            <TextField hint="Usuario" autocorrect="false" autocapitalizationType="none"
                       v-model="user.username"
                       class="input"
            />
          </StackLayout>
          <StackLayout class="input">
            <TextField
                    hint="Contraseña" secure="true" autocorrect="false" autocapitalizationType="none"
                    v-model="user.password"
                    class="input"
            />
          </StackLayout>
          <Label v-if="message">{{ message }}</Label>
          <Button :isEnabled="!loading" class="btn btn-primary -primary" @tap="handleLogin">
            <ActivityIndicator :busy="loading"></ActivityIndicator>
            <span>Login</span>
          </Button>
          <Button>Registro</Button>
        </StackLayout>
      </GridLayout>
    </RadSideDrawer>


  </Page>
</template>
<script>
import User from "../models/user";
import {ApplicationSettings} from "@nativescript/core";
import axios from 'axios';
import {mapGetters, mapActions} from 'vuex';

import * as config from '../config.js';

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
      getUser: 'auth/getUser',
      getToken: 'auth/getToken'
    })
  },

  methods: {
    ...mapActions({
      login: 'auth/login',
      login_fail: 'auth/login_fail',
      login_saved: 'auth/login_saved'
    }),
    on_load() {
      /*
      var userData = ApplicationSettings.getString("userData");
      if (userData != null) {
        var parsed = JSON.parse(userData);
        console.log("Datos precargados:");
        console.log(parsed);

        this.login_saved(parsed);
        if (this.getToken != null) {
          this.$navigateTo(routes.ProfileView);
        }
      }
       */
      this.user.username = this.getUser.username;
      this.user.password = this.getUser.password;

    },
    handleLogin() {
      this.loading = true;
      this.message = '';
      console.log(this.user.username + " " + this.user.password);
      var user = this.user.username;
      var password = this.user.password;
      var url = config.BACKEND + "/auth/login"
      console.log(url);
      try {
        axios.post(url, {
          username: user,
          password: password,
        }).then(response => {
          this.loading = false;
          this.user.token = response.data.token;
          this.login(this.user);
          ApplicationSettings.setString(
              "userData",
              JSON.stringify(this.user)
          );
          this.$navigator.navigate('profile',{clearHistory: true});
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
  background: #2196f3
}

.drawer-item {
  padding: 8 16;
  font-size: 16;
}
</style>
