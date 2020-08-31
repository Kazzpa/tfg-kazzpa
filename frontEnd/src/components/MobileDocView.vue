<template>
  <v-main>
      <div class="text-h4">
        Instalacion de apk:
      </div>
    <v-container>
    <v-layout justify-center>
      <v-img class="my-2 mx-2" max-width="60vh" aspect-ratio="16/9"
             src="https://images.pexels.com/photos/34407/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260">

      </v-img>
    </v-layout></v-container>
      <v-dialog v-model="dialog" persistent max-width="290">
        <template v-slot:activator="{ on, attrs }">
          <v-btn class="mx-2 my-2"
              color="primary"
              dark
              v-bind="attrs"
              v-on="on"
          >
            Tutorial externo
          </v-btn>
        </template>
        <v-card>
          <v-card-title class="headline">Atención</v-card-title>
          <v-card-text>Va a acceder a una pagina externa a SelAtt, verifique la url previamente.</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" text @click="dialog = false">Cancelar</v-btn>
            <v-btn color="secondary"
                   href="https://www.xatakandroid.com/tutoriales/como-instalar-aplicaciones-en-apk-en-un-movil-android"
                   target="_blank" @click="dialog = false">Acceder
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

    <v-container class="justify-center">
      <div>
        Las funcionalidades de la aplicación estan limitadas.
        Por el momento no se pueden subir datasets desde la aplicación movil, pero si se pueden hacer todas las otras
        tareas.
      </div>
      <v-btn disabled> Descargar APK</v-btn>
    </v-container>
  </v-main>
</template>

<script>
export default {
  name: "MobileDocView",
  data: () => {
    return {
      dialog: false,
    }
  },
  methods: {}
}
</script>

<style scoped>

</style>