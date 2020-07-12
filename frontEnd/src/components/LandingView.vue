<template>
    <v-main>
        <v-row>
            <v-col>
                <v-card class="mx-auto" max-width="400">
                    <v-img
                            class="white--text align-end img_blur"
                            height="200px"
                            src="https://images.pexels.com/photos/1624895/pexels-photo-1624895.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260">
                        <v-card-title>Servicio API REST</v-card-title>
                    </v-img>

                    <v-card-subtitle class="pb-0">Explore nuestros algoritmos haciendo uso de nuestra api rest
                    </v-card-subtitle>
                    <v-card-actions>
                        <v-btn text>Información</v-btn>
                        <v-btn color="blue" text v-on:click="goToAlgorithm">Acceder</v-btn>

                    </v-card-actions>
                </v-card>
            </v-col>
            <v-col>
                <v-card class="mx-auto" max-width="400">
                    <v-img
                            class="white--text align-end img_blur"
                            height="200px"
                            src="https://images.pexels.com/photos/230544/pexels-photo-230544.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260">
                        <v-card-title>Servicio WEB</v-card-title>
                    </v-img>
                    <v-card-subtitle class="pb-0">Explore nuestros algoritmos haciendo uso de esta Aplicación web
                    </v-card-subtitle>
                    <v-card-actions>
                        <v-btn text>Información</v-btn>
                        <v-btn color="blue" text v-on:click="goToAlgorithm">Acceder</v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
    </v-main>
</template>

<script>
    const algorithm_path = process.env.VUE_APP_ALGORITHM_PATH;
    export default {
        name: "LandingView",
        methods:{
            goToAlgorithm(){
                this.$router.push(algorithm_path)
            }
        }
    }
</script>

<style scoped>
.img_blur{
    text-shadow: 1px 1px 2px black;
}
</style>