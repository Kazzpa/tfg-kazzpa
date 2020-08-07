<template>
  <Page>

    <ActionBar title="SelAttactionbar">
      <StackLayout orientation="horizontal">
        <NavigationButton text="Menu" android.systemIcon="ic_menu_back"
                    @tap="showSideDrawer" verticalAlignment="left"/>
        <Label text="SelAtt" fontSize="24" verticalAlignment="center"/>
      </StackLayout>
    </ActionBar>
    <RadSideDrawer v-if="drawerShow">
      <StackLayout>
        <Button text="Prueba cerrar" @tap="closeSideDrawer"></Button>
      </StackLayout>
    </RadSideDrawer>
    <ScrollView>
      <StackLayout>
        <NavigationButton text="Menu" android.systemIcon="ic_menu_back"
                          @tap="showSideDrawer" verticalAlignment="left"/>
        <Label class="h2 font-weight-bold" text="Servicio API REST"/>
        <Image
            class="img_blur"
            height="20%"
            src="https://images.pexels.com/photos/1624895/pexels-photo-1624895.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260">
          <Label text="Servicio API REST"/>
        </Image>

        <Label class="h3 body" text="Explore nuestros algoritmos haciendo uso de nuestra api rest"/>

        <Button text="Acceder" class="-primary font-weight-bold"
                @tap="goToAlgorithm"/>
        <Button text="Información"/>
        <Label class="h2 font-weight-bold">Servicio WEB</Label>
        <Image
            class="img_blur"
            height="20%"
            src="https://images.pexels.com/photos/230544/pexels-photo-230544.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260">

        </Image>
        <Label class="h3 body" text="Explore nuestros algoritmos haciendo uso de esta Aplicación web"/>
        <Button class="-primary font-weight-bold" text="Acceder" @tap="goToAlgorithm"/>
        <Button text="Información"/>
      </StackLayout>
    </ScrollView>
  </Page>
</template>

<script>
import LoginView from "./LoginView";
import ProfileView from "./ProfileView";


export default {
  name: 'App',
  components: {
    LoginView,
  },
  data: () => ({
    drawerShow: false,
  }),
  methods: {
    goToAlgorithm() {
      this.$navigateTo(LoginView);
    },
    goToProfile() {
      this.$navigateTo(ProfileView);
    },
    goToResults() {
      //TODO:Implement when profile.view exists
    },
    showSideDrawer() {
      console.log(this.drawerShow + "Cambiara a true")
      this.drawerShow = true;
    },
    closeSideDrawer() {
      this.drawerShow = false;
    }

  }
};

</script>
