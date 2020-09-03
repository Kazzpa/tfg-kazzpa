<template>
    <v-main>
        <v-row class="py-5 mx-2">

            <v-img contain max-height="50vh" fixed-ratio="16/9" :src="require('../assets/logotipo-texto.png')">
            </v-img>
        </v-row>
        <v-row class="justify-center">
            <div class="text-h2 text-center">Machine Learning aplicado a datos Biomedicos</div>
        </v-row>
        <v-row class="justify-center py-4 mx-2">

            <div class="font-weight-light text--lighten-2">Aplicación de data mining especializada en selección de
                atributos a servicio mediante
                frameworks de última generación como <strong>Spring Boot</strong> y <strong>VueJs</strong>
            </div>
        </v-row>
        <div style="background-color: lightgray">
        <v-row class="my-10 py-10" >
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
                        <v-btn class="primary" text v-on:click="goToApiDoc">Acceder</v-btn>

                    </v-card-actions>
                </v-card>
            </v-col>
            <v-col>

                <v-card class="mx-auto" max-width="400">
                    <v-img
                            class="white--text align-end img_blur"
                            height="200px"
                            src="https://images.pexels.com/photos/374016/pexels-photo-374016.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260">
                        <v-card-title>Servicio WEB</v-card-title>
                    </v-img>
                    <v-card-subtitle class="pb-0">Explore nuestros algoritmos haciendo uso de esta Aplicación web
                    </v-card-subtitle>
                    <v-card-actions>
                        <v-btn text v-on:click="goToWebDoc">Información</v-btn>
                        <v-btn class="primary" text v-on:click="goToAlgorithm">Acceder</v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
        <v-row>
            <v-col>

                <v-card class="mx-auto" max-width="400">
                    <v-img
                            class="white--text align-end img_blur"
                            height="200px"
                            src="https://images.pexels.com/photos/230544/pexels-photo-230544.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260">
                        <v-card-title>Aplicación movil</v-card-title>
                    </v-img>
                    <v-card-subtitle class="pb-0">Consulte los resultados de sus ejecuciones en la app nativa
                    </v-card-subtitle>
                    <v-card-actions>
                        <v-btn text>Información</v-btn>
                        <v-btn class="primary" text v-on:click="goToMobile">Acceder</v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
        </div>
    </v-main>
</template>

<script>

    const algorithm_path = process.env.VUE_APP_ALGORITHM_PATH;
    const mobile_path = process.env.VUE_APP_MOBILE_PATH;
    const api_path = process.env.VUE_APP_API_PATH;
    const webdoc_path = process.env.VUE_APP_WEBDOC_PATH;
    export default {
        name: "LandingView",
        methods: {
            goToAlgorithm() {
                this.$router.push(algorithm_path)
            },
            goToMobile() {
                this.$router.push(mobile_path);
            },
            goToApiDoc() {
                this.$router.push(api_path);
            },
            goToWebDoc() {
                this.$router.push(webdoc_path);
            }
        }
    }
</script>

<style scoped>
    .img_blur {
        text-shadow: 1px 1px 2px black;
    }
</style>