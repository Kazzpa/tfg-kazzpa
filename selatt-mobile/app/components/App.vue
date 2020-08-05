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
        <Label v-else class="drawer-item" text="Profile" @tap="goToProfile"/>
        <Label v-else class="drawer-item" text="Algorithms" @tap="goToAlgorithms"/>
        <Label v-else class="drawer-item" text="Results" @tap="goToResults"/>
      </StackLayout>

      <GridLayout ~mainContent columns="*" rows="*">
        <Label class="message" :text="msg" col="0" row="0"/>
        <Button :text="getText" @tap="testFunction"/>
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
      getText: 'auth/getText',
      getUser: 'auth/getUser',
    })
  },
  methods: {
    ...mapActions({
      setText: 'auth/test'
    }),
    testFunction() {
      this.setText('vuex is working')
    },
    goToLogin() {
      this.$navigateTo(routes.LoginView);
    },
    goToProfile() {
      this.$navigateTo(routes.ProfileView);
    },
    goToAlgorithms() {
      this.$navigateTo(routes.AlgorithmsView);
    },
    goToResults() {
      this.$navigateTo(routes.ResultsView);
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
