<template>
  <Page>
    <ActionBar>
      <GridLayout width="100%" columns="auto, *">
        <Label text="MENU" @tap="$refs.drawer.nativeView.showDrawer()" col="0"/>
        <Label class="title" text="Home" col="1"/>
      </GridLayout>
    </ActionBar>

    <RadSideDrawer ref="drawer">
      <StackLayout ~drawerContent backgroundColor="#ffffff">
        <Label class="drawer-header" text="Drawer"/>
        <Label v-if="this.getUser!=null" class="drawer-item" text="Login" @tap="goToLogin"/>
        <template v-else>
          <Label class="drawer-item" text="Profile" @tap="goToProfile"/>
          <Label class="drawer-item" text="Algorithms" @tap="goToAlgorithms"/>
          <Label class="drawer-item" text="Results" @tap="goToResults"/>

        </template>
      </StackLayout>

      <GridLayout ~mainContent columns="*" rows="*">
      </GridLayout>
    </RadSideDrawer>

  </Page>
</template>

<script>
import {mapGetters, mapActions} from 'vuex'
import routes from "./routes";

export default {
  data() {
    return {
      msg: 'Hello World!'
    }
  },
  computed: {
    ...mapGetters({
      getUser: 'auth/getUser',
    })
  },
  methods: {
    ...mapActions({
    }),
    goToLogin() {
      this.$navigateTo(routes.LoginView,{clearHistory: true});
    },
    goToProfile() {
      this.$navigateTo(routes.ProfileView,{clearHistory: true});
    },
    goToAlgorithms() {
      this.$navigateTo(routes.AlgorithmsView,{clearHistory: true});
    },
    goToResults() {
      this.$navigateTo(routes.ResultsView,{clearHistory: true});
    },
  }
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
