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

      <GridLayout ~mainContent columns="*" rows="*">
        Logado como: <Label :text="getUser.username"></Label>
        <Button text="Ejecuciones"></Button>
      </GridLayout>
    </RadSideDrawer>
  </Page>
</template>
<script>
import {mapActions, mapGetters} from 'vuex';

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
  background: ""
}

.drawer-item {
  padding: 8 16;
  font-size: 16;
}
</style>