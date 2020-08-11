<template>
  <Page>
    <ActionBar>
      <GridLayout width="100%" columns="auto, *">
        <Label text="MENU" @tap="$refs.drawer.nativeView.showDrawer()" col="0"/>
        <Label class="title" text="Profile" col="1"/>
      </GridLayout>
    </ActionBar>

    <RadSideDrawer ref="drawer">
      <StackLayout ~drawerContent backgroundColor="#ffffff">
        <Label class="drawer-header" text="Drawer"/>
        <Label class="drawer-item" text="Login" @tap="goToLogin"/>
        <Label class="drawer-item" text="Home" @tap="goToApp"/>
        <Label class="drawer-item" text="Results" @tap="goToResults"/>
      </StackLayout>

      <StackLayout ~mainContent columns="*" rows="*">
        <Label text="Logado como:"/>
        <Label class="h2" :text="getUsername"></Label>
        <Label text="Token:"/>
        <Label class="h3" :text="getToken"></Label>
        <Button text="Ejecuciones" @tap="goToAlgorithms"></Button>
        <Button class="h3" text="LogOut" @tap="log_out"></Button>
      </StackLayout>
    </RadSideDrawer>
  </Page>
</template>
<script>
import {mapActions, mapGetters} from 'vuex';
import {ApplicationSettings} from "@nativescript/core";

export default {
  data() {
    return {
      message: ""
    }
  },
  computed: {
    ...mapGetters({
      getUser: 'auth/getUser',
      getUsername: 'auth/getUsername',
      getToken: 'auth/getToken',
    }),
  },
  methods: {
    ...mapActions({
      logout: 'auth/logout'
    }),
    goToLogin() {
      this.$navigator.navigate('login', {clearHistory: true});
    },
    goToAlgorithms() {
      this.$navigator.navigate('algorithms', {clearHistory: true});
    },
    goToResults() {
      this.$navigator.navigate('results', {clearHistory: true});
    },
    goToApp() {
      this.$navigator.navigate('/', {clearHistory: true});
    },
    log_out() {
      this.logout();
      try {

        if (ApplicationSettings.hasKey("userData")) {
          console.log("Removing appsettings");
          ApplicationSettings.remove("userData");
          console.log("Flushing appsettings");
          ApplicationSettings.flush();
          console.log("Navigating to Login View");
          this.$navigator.navigate('login', {clearHistory: true});
        }
      } catch (error) {
        console.log("check2error");
        console.log(error);
      }
    }
  }
}

</script>
<style scoped>

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