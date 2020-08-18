<template>
    <v-main>
        <v-card class="mx-2 my-2" v-if="resultChosen" v-model="resultChosen">
            <v-card-title>
                Lista de attributos seleccionados
            </v-card-title>
            <v-card-text class="text--primary">
                Atributos: '{{ resultChosen.attributesSelected }}'
            </v-card-text>
            <v-card-actions>
                <v-btn class="secondary" text @click="resultChosen = null">
                    Cerrar
                </v-btn>
                <v-btn class="primary" @click="downloadCsv">CSV
                    <v-icon>mdi-download</v-icon>
                </v-btn>
            </v-card-actions>
        </v-card>
        <!--

                        {text: 'Instancias correctamente clasificadas', value: 'correctlyClassified', sortable: true},
                        {text: 'Mediana de error absoluto', value: 'meanAbsoluteError', sortable: true},-->
        <v-card class="mx-2 my-2" v-if="classifierChosen" v-model="classifierChosen">
            <v-card-title>
                Clasificador {{ classifierChosen.algorithm.name }}
            </v-card-title>
            <v-card-text class="text--primary">
                <p> Numero de instancias:{{ classifierChosen.numInstances }}</p>
                <p>Instancias correctamente clasificadas: {{ classifierChosen.correctlyClassified}}</p>
                <p>Mediana de Error absoluto: {{ classifierChosen.meanAbsoluteError}}</p>
            </v-card-text>
            <v-card-actions>
                <v-btn class="secondary" text @click="classifierChosen = null">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-card-actions>
        </v-card>
        <v-card class="mx-2 my-2">
            <v-card-title>
                Resultados de ejecuciones previos
            </v-card-title>
            <v-spacer/>
            <v-text-field
                    v-model="search"
                    append-icon="mdi-filter-variant"
                    label="Filtrar"
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
                            @click="viewFeature(item)"
                    >
                        mdi-file-chart
                    </v-icon>
                </template>
            </v-data-table>
        </v-card>
        <v-card class="mx-2 my-2">
            <v-card-title>
                Clasificaciones previas
            </v-card-title>
            <v-spacer/>
            <v-text-field
                    v-model="classifierSearch"
                    append-icon="mdi-filter-variant"
                    label="Filtrar"
                    single-line
                    hide-details
            />
            <v-data-table v-if="classifierResults != null" :headers="classifierHeaders"
                          :items="classifierResults"
                          :search="classifierSearch">

                <template v-slot:item.percentage="{ item }">
                    {{item.percentage}}%
                </template>
                <template v-slot:item.actions="{ item }">
                    <v-icon
                            small
                            class="mr-2"
                            @click="viewClassifier(item)"
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
                {text: "Algoritmo seleccionado", value: "algorithm.name"},
                {text: "Tamaño del dataset(bytes)", value: "performed.size"},
                {text: 'Fecha', value: 'finishedDate', sortable: true},
                {text: 'Attributos seleccionados', value: 'actions', sortable: false},
            ],
            classifierHeaders: [
                {text: "Dataset", align: "start", value: "performed.filename"},
                {text: "Algoritmo seleccionado", value: "algorithm.name"},
                {text: 'Fecha', value: 'finishedDate', sortable: true},
                {text: 'Porcentaje de acierto', value: 'percentage', sortable: true},
                {text: 'Info', value: 'actions', sortable: true},
            ],
            results: null,
            resultChosen: null,
            classifierSearch: null,
            classifierResults: null,
            classifierChosen: null,

        }), computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        methods: {
            viewFeature(item) {
                this.resultChosen = item;
                this.classifierChosen = null;
            },
            viewClassifier(item) {
                this.classifierChosen = item;
                this.setSeenClassifier(item);
                this.resultChosen = null;
            }, downloadCsv() {
                let csvContent = "data:text/csv;charset=utf-8,";
                csvContent += [this.resultChosen.attributesSelected
                ]
                    .join("\n")
                    .replace(/(^\[)|(\]$)/gm, "");

                const data = encodeURI(csvContent);
                const link = document.createElement("a");
                link.setAttribute("href", data);
                link.setAttribute("download", this.resultChosen.performed.filename+".csv");
                link.click();
            },
            setSeenClassifier(item){
                let url = server_url + "/evaluate/result_seen";
                axios.post(url,{
                    "id" : item.id,
                    "algorithm" : item.algorithm,
                    "performed" : item.performed,
                    "seen" : item.seen,
                },{
                    headers: {
                        "Authorization": "Bearer " + this.$store.state.auth.user.token,
                    }
                })
            }
        },
        created() {
            if (!this.loggedIn) {
                this.$router.push(login_path);
            } else {
                let url = server_url + "/featureSelection/results/";
                axios.get(url,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "Authorization": "Bearer " + this.$store.state.auth.user.token,
                        }
                    })
                    .then(response => {

                        const options = {
                            weekday: 'long',
                            month: 'long',
                            year: 'numeric',
                            day: 'numeric',
                            hour: 'numeric',
                            minute: 'numeric',
                            second: 'numeric',
                            hour12: false
                        };
                        response.data.forEach(elem=>{
                            elem.finishedDate = Intl.DateTimeFormat("es-ES", options).format(new Date(elem.finishedDate));
                        })

                        this.results = response.data;
                    })
                    .catch(error => {
                        console.log(error);
                        console.log(error.response);


                    });

                url = server_url + "/evaluate/results/";
                axios.get(url,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "Authorization": "Bearer " + this.$store.state.auth.user.token,
                        }
                    })
                    .then(response => {
                        console.log(response);
                        const options = {
                            weekday: 'long',
                            month: 'short',
                            year: 'numeric',
                            day: 'numeric',
                            hour: 'numeric',
                            minute: 'numeric',
                            second: 'numeric',
                            hour12: false
                        };
                        response.data.forEach(elem => {
                            let fecha = new Date(elem.finishedDate);
                            console.log(fecha);
                            elem.finishedDate = Intl.DateTimeFormat("es-ES", options).format(fecha);
                            elem.percentage = Math.round(((elem.correctlyClassified / elem.numInstances) +
                                Number.EPSILON) *
                                10000) / 100;
                            console.log(elem.percentage);
                        })
                        console.log(response.data);
                        this.classifierResults = response.data;
                    })
                    .catch(error => {
                        console.log(error);
                        console.log(error.response);


                    });
            }
        }
    }
</script>

<style scoped>

</style>