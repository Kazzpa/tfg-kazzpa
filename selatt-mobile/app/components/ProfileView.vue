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
        <Label class="drawer-item" text="Item 2"/>
        <Label class="drawer-item" text="Item 3"/>
      </StackLayout>

      <StackLayout ~mainContent columns="*" rows="*">
        <Label text="Logado como:"/>
        <Label class="h2" :text="getUser.username"></Label>
        <Label text="Token:"/>
        <Label class="h3" :text="getUser.token"></Label>
        <Button text="Ejecuciones" @tap="goToAlgorithms"></Button>
      </StackLayout>
    </RadSideDrawer>
  </Page>
</template>
<script>
import {mapActions, mapGetters} from 'vuex';
import routes from "./routes";

export default {
  data() {
    return {
      message: ""
    }
  },
  computed: {
    ...mapGetters({
      getUser: 'auth/getUser'
    }),
  },
  methods: {
    goToAlgorithms() {
      this.$navigateTo(routes.AlgorithmsView);
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