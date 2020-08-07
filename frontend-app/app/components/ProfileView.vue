<template>
  <Page>
    <ActionBar title="SelAtt"></ActionBar>
    <StackLayout>
      <Label>
        <strong v.if="currentUser">{{ currentUser.username }}</strong> Perfil
      </Label>
    </StackLayout>

    <Button class="nt-button" v-on:click="goToAlgorithm">Algoritmos</Button>
  </Page>
</template>

<script>
import LoginView from "./LoginView";


export default {
  name: "ProfileView",
  computed: {
    currentUser() {
      return this.$store.state.auth.user(LoginView).catch(err => console.log(err));

    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$navigateTo(LoginView);
    }
  },
  methods: {
    goToAlgorithm() {
      //this.$navigateTo(AlgorithmsView);
    }
  }
}
</script>

<style scoped>

</style>