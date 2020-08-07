<template>
    <v-main>
        <v-card>
            <v-card-title>
                Resultados de ejecuciones previos
            </v-card-title>
            <v-spacer/>
            <v-text-field
                    v-model="search"
                    append-icon="mdi-magnify"
                    label="Search"
                    single-line
                    hide-details
            />
            <v-data-table v-if="results != null" :headers="headers"
                          :items="results"
                          :search="search">
                <template v-slot:item.actions="{ item }">
                    <v-icon
                            small
                            class="mr-2"
                            @click="viewItem(item)"
                    >
                        mdi-file-chart
                    </v-icon>
                </template>
            </v-data-table>
            <v-dialog v-if="resultChosen" v-model="resultChosen">
                <v-card>
                    <v-card-text>
                        {{ this.resultChosen.jsonAttributes }}
                    </v-card-text>
                    <v-card-actions>
                        <v-btn color="green darken-1" text @click="resultChosen = null">
                            Cerrar
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </v-card>
    </v-main>
</template>

<script>
    import axios from "axios";
    import Vue from 'vue';
    import VueRouter from 'vue-router';

    Vue.use(VueRouter);

    const login_path = process.env.VUE_APP_LOGIN_PATH;
    const server_url = process.env.VUE_APP_API_SERVER_URL;


    require('axios-debug-log');
    localStorage.debug = "axios";

    export default {
        name: "ResultsView",
        data: () => ({
            search: '',
            headers: [
                {text: "Dataset", align: "start", value: "performed.filename"},
                {text: "Algoritmo seleccionado", value: "algorithm.name"},
                {text: "Tamaño del dataset(bytes)", value: "performed.size"},
                {text: 'Finished Date', value:'finishedDate',sortable: true},
                {text: 'Attributos seleccionados', value: 'actions', sortable: false},
            ],
            results: null,
            resultChosen: null,
        }), computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        methods: {
            viewItem(item) {
                this.resultChosen = item;
            }
        },
        created() {
            if (!this.loggedIn) {
                this.$router.push(login_path);
            } else {
                let url = server_url + "/featureSelection/resultsByUser/";
                axios.get(url,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "Authorization": "Bearer " + this.$store.state.auth.user.token,
                        }
                    })
                    .then(response => {
                        this.results = response.data;
                    })
                    .catch(error => {
                        console.log(error);

                    });
            }
        }
    }
</script>

<style scoped>

</style>