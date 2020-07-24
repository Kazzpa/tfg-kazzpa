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
                {text: "Algorithm", value: "algorithm.name"},
                {text: "Dataset size (bytes)", value: "performed.size"},
                {text: 'Actions', value: 'actions', sortable: false},
            ],
            results: null,
        }), computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        methods: {
            viewItem(item) {
                let url = server_url + "/featureSelection/result/" + item.id;
                console.log(url);
                axios.get(url,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "Authorization": "Bearer " + this.$store.state.auth.user.token,
                        }
                    })
                    .then(response => {
                        response.data.forEach(elem => {
                            elem.value = elem.filename;
                            elem.text = elem.filename;
                        });
                        this.results = response.data;


                    })
                    .catch(error => {
                        console.log(error);

                    });
            }
        },
        created() {
            console.log("We are in");
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
                        response.data.forEach(elem => {
                            elem.value = elem.filename;
                            elem.text = elem.filename;
                        });
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