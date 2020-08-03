<template>
  <Page>
    <StackLayout>
      <Label><strong v-if="currentUser">{{ currentUser.username }}</strong> Perfil
      </Label>
      <Button class="nt-button" @tap="goToAlgorithm" v-model="message">Algoritmos</Button>

    </StackLayout>
  </Page>
</template>
<script>
import {mapGetters, mapActions} from "vuex";


export default {
  data() {
    return {
      message: '',
    }
  },
  computed: {
    ...mapGetters({
      getUser: 'auth/getUser'
    }),
    currentUser() {
      return this.getUser;
    }
  },
  methods: {
    goToAlgorithm() {
      this.message = "Button works";
      //do nothing for now
    }
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