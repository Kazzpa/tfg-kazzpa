<template>
    <Page @loaded="on_load">
        <ActionBar>
            <NavigationButton text="Nav" android.systemIcon="ic_menu_more" @tap="$refs.drawer.nativeView.showDrawer()"/>
            <Label class="title" text="SelAtt - Algorithms"/>
        </ActionBar>

        <RadSideDrawer ref="drawer">
            <StackLayout ~drawerContent backgroundColor="#ffffff">
                <StackLayout class="drawer-header">
                    <Label color="white" text="SelAtt"/>
                </StackLayout>
                <Label class="drawer-item" text="Home" @tap="goToApp"/>
                <Label class="drawer-item" text="Profile" @tap="goToProfile"/>
                <Label class="drawer-item" text="Results" @tap="goToResults"/>
            </StackLayout>

            <StackLayout ~mainContent>
                <Label class="h5" :text="chooseAlgorithmLabel"></Label>
                <Switch
                        @checkedChange="switchAlgorithmLabel"
                        v-model="chooseAlgorithm"></Switch>
                <StackLayout v-if="datasetsLoaded">
                    <Label class="h4" text="Seleccionar un dataset:"/>
                    <Label class="h4" text="Dataset a procesar"/>
                    <DropDown v-if="chooseAlgorithm"
                              class="h3"
                              :items="datasetArray"
                              :selectedIndex="datasetIndex"
                              @selectedIndexChange="selectedDatasetChanged"/>
                    <DropDown v-else
                              :items="classDatasetArray"
                              class="h3"
                              :selectedIndex="classDataIndex"
                              @selectedIndexChange="selectedClassDatasetChanged"/>

                    <Button v-if="!selectedDataset" @tap="selectedDataset=true" text="Seleccionar"></Button>
                </StackLayout>
                <StackLayout v-if="selectedDataset">

                    <StackLayout orientation="horizontal">

                    </StackLayout>

                    <StackLayout v-if="chooseAlgorithm">
                        <Label class="h5 font-weight-bold" text="Seleccion de atributos"/>
                        <DropDown @selectedIndexChange="selectedAlgorithmChanged"
                                  v-model="algorithmIndex"
                                  class="h3"
                                  :selectedIndex="algorithmIndex"
                                  :items="algorithms"/>
                        <Button v-if="!selectedAlgorithm" @tap="selectedAlgorithm=true" text="Seleccionar"></Button>
                        <Label v-if="selectedAlgorithm" :text="algorithmsValue[algorithmIndex]"></Label>
                    </StackLayout>

                    <StackLayout v-else>
                        <Label class="h5 font-weight-bold" text="Clasificadores"/>
                        <DropDown @selectedIndexChange="selectedClassifierChanged"
                                  class="h3"
                                  :selectedIndex="classifierIndex"
                                  :items="classifiers"/>
                        <Button v-if="!selectedClassifier" @tap="selectedClassifier=true"
                                text="Seleccionar"></Button>
                        <Label v-if="selectedClassifier"
                               :text="classifiersValue[classifierIndex]"></Label>
                    </StackLayout>
                    {{ datasetArray[datasetIndex] }}
                    <Button v-if="selectedAlgorithm||selectedClassifier" @tap="processAlgorithm">Evaluar</Button>
                    <Label v-if="responseProcess">
                        {{ responseProcess }}</Label>
                    <StackLayout orientation="horizontal" v-if="responseData!=null">
                        <Label :text="responseData.performed.filename"></Label>
                        <Label :text="responseData.finishedDate"></Label>
                        <Label :text="responseData.attributesSelected"></Label>
                        <NavigationButton @tap="responseData=null"  android.systemIcon="ic_menu_close_clear_cancel" text="Cerrar"></NavigationButton>
                    </StackLayout>

                </StackLayout>
            </StackLayout>
        </RadSideDrawer>

    </Page>
</template>
<script>
    import {mapGetters, mapActions} from 'vuex'
    import axios from 'axios';
    import * as config from '../config.js';
    import {ApplicationSettings} from "@nativescript/core"


    export default {
        data() {
            return {
                msg: '',
                selectedIndex: 1,
                inputFile: null,
                inputFileName: null,
                responseFileUpload: null,
                responseFileDownload: null,
                responseProcess: null,
                responseProcessStatus: "success",
                loading: true,
                datasetsLoaded: false,
                datasets: null,
                classifierDatasets: null,
                responseData: null,
                chooseAlgorithm: true,
                chooseAlgorithmLabel: "Selección de Atributos",
                datasetArray: [],
                classDatasetArray: [],
                classDataset: [],
                selectedDataset: false, selectedAlgorithm: false, selectedClassifier: false,
                datasetIndex: 0,
                classDataIndex: 0, algorithmIndex: 0, classifierIndex: 0,
                algorithms: [
                    "Fast Correlation Based Filter",
                    "Variable neighbourhood search",
                    "Scatter Search",
                    "Ranker",
                    "Best First",
                ],
                algorithmsValue: [
                    "fcbf", "vns", "scs", "ranker", "bestfirst"
                ],
                algDict: {
                    "FastCorrelationBasedFilter": "FCBF",
                    "Naive Bayes": "naivebayes",
                    "ScatterSearchV1": "Scs",
                    "VariableNeighbourhoodSearch": "VNS",
                    "BestFirst": "BestFirst",
                    "Ranker": "Ranker",
                }
                ,
                classifiers: [
                    "Naive Bayes",
                ],
                classifiersValue: [
                    "naivebayes",
                ]
            }
        },
        computed: {
            ...mapGetters({
                getUser: 'auth/getUser',
                getToken: 'auth/getToken',
                getProcess: 'process/getProcess'
            })
        },
        methods: {
            ...mapActions({
                process_algorithm: 'process/processed',
                logout: 'auth/logout',
            }),
            on_load() {
                this.selectedDataset = false;
                this.selectedAlgorithm = false;
                this.selectedClassifier = false;
                if (this.loggedIn()) {
                    let url = config.BACKEND + "/featureSelection/datasets/";
                    axios.get(url,
                        {
                            headers: {
                                "Content-Type": "multipart/form-data",
                                "Authorization": "Bearer " + this.getToken,
                            }
                        })
                        .then(response => {
                            response.data.forEach(elem => {
                                elem.value = elem.filename;
                                elem.text = elem.filename;
                                this.datasetArray.push(elem.filename);
                            });
                            this.datasets = response.data;
                            console.log("Result feature");
                            console.log(this.datasetArray);
                            //Nested request
                            url = config.BACKEND + "/evaluate/datasets/";
                            axios.get(url,
                                {
                                    headers: {
                                        "Content-Type": "multipart/form-data",
                                        "Authorization": "Bearer " + this.getToken,
                                    }
                                })
                                .then(response => {
                                    console.log(response.data);
                                    response.data.forEach(elem => {
                                        elem.value = elem.performed.filename;
                                        elem.text = elem.performed.filename + "-" + this.algDict[elem.algorithm.name];
                                        this.classDatasetArray.push(elem.text);
                                    });
                                    this.classDataset.push.apply(this.classDataset, response.data);
                                    this.classDatasetArray.push.apply(this.classDatasetArray, this.datasetArray);
                                    console.log("evaluate results");
                                    console.log(this.classDatasetArray);
                                    this.classifierDatasets = response.data;
                                    this.classifierDatasets.push.apply(this.classifierDatasets, this.datasets);
                                    this.datasetsLoaded = true;


                                })
                                .catch(error => {
                                    console.log("error evaluate");
                                    console.log(error);

                                });

                        })
                        .catch(error => {
                            console.log(this.getToken);
                            console.log(error);
                            console.log(typeof error);

                        });
                }
            },
            selectedClassDatasetChanged(args) {
                let picker = args.object;
                console.log("dataset classifier cambiado");
                console.log(picker.selectedIndex);
                if (this.classDataIndex !== picker.selectedIndex) {
                    this.selectedDataset = true;
                    this.classDataIndex = picker.selectedIndex;
                    console.log("Indice cambiado:" + this.classDataIndex);
                }
            },
            selectedDatasetChanged(args) {
                let picker = args.object;
                console.log("dataset feature cambiado");
                console.log(picker.selectedIndex);
                if (this.datasetIndex !== picker.selectedIndex) {
                    this.selectedDataset = true;
                    this.datasetIndex = picker.selectedIndex;
                    console.log("Indice cambiado:" + this.datasetIndex);
                }
            },
            selectedAlgorithmChanged(args) {
                let picker = args.object;
                console.log("algoritmo cambiado");
                console.log(picker.selectedIndex);
                if (this.algorithmIndex !== picker.selectedIndex) {
                    this.selectedAlgorithm = true;
                    this.selectedClassifier = false;
                    this.algorithmIndex = picker.selectedIndex;
                }
            },
            selectedClassifierChanged(args) {
                let picker = args.object;
                console.log("clasificador cambiado");
                console.log(picker.selectedIndex);
                if (this.classifierIndex !== picker.selectedIndex) {
                    this.selectedClassifier = true;
                    this.selectedAlgorithm = false;
                    this.classifierIndex = picker.selectedIndex;
                }
            },
            switchAlgorithmLabel() {
                this.chooseAlgorithmLabel = this.chooseAlgorithm ? 'Selección de atributos' : 'Clasificador';
            },
            goToProfile() {
                this.$navigator.navigate('profile', {clearHistory: true});
            },
            goToResults() {
                this.$navigator.navigate('results', {clearHistory: true});
            },
            goToApp() {
                this.$navigator.navigate('/', {clearHistory: true});
            },
            loggedIn() {
                return this.getUser != null;
            },
            processAlgorithm() {
                let process_path, filename;
                if (this.chooseAlgorithm && this.selectedAlgorithm) {
                    process_path = "/featureSelection/" + this.algorithmsValue[this.algorithmIndex];
                    filename = this.datasetArray[this.datasetIndex];
                } else if (!this.chooseAlgorithm && this.selectedClassifier) {
                    process_path = "/evaluate/" + this.classifiersValue[this.classifierIndex];
                    filename = this.classifierDatasets[this.classDataIndex].value;
                }
                let url = config.BACKEND + process_path;
                console.log(this.algorithmsValue);
                console.log(filename);
                let isFeatureResult = this.algorithmsValue.some(elem => {
                    return filename.includes(elem);
                });
                if (isFeatureResult) {
                    url += "_filtered";
                    console.log("its filtered", url, filename);
                    axios
                        .get(url + "/" + filename, {
                            headers: {
                                "Authorization": "Bearer " + this.getToken,
                            }
                        })
                        .then(response => {
                            this.process_algorithm(response.data);
                            this.responseProcess = "Ejecucion finalizada " + response;
                            console.log(JSON.stringify(response));

                        }).catch(error => {
                        console.log("error Process filtered");
                        console.log(error);
                    });
                } else {
                    console.log(url, filename);
                    axios
                        .get(url + "/" + filename, {
                            headers: {
                                "Authorization": "Bearer " + this.getToken,
                            }
                        })
                        .then(response => {
                            this.process_algorithm(response.data);
                            this.responseProcess = "Ejecucion finalizada " + response;
                            console.log(JSON.stringify(response));
                            this.responseData = response.data;

                        })
                        .catch(error => {
                            console.log("error Process");
                            console.log(error);
                        });
                }

            }
        },
        logout() {
            this.logout();
            ApplicationSettings.remove("userData");
            this.$navigator.navigate('login', {clearHistory: true});
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
        background: #2196f3;
        background-image: url("~/assets/images/backgroundDrawer.jpg");
        background-size: cover;
    }

    .drawer-item {
        padding: 8 16;
        font-size: 16;
    }

    .item-drop-down {
        font-size: 20;
        padding: 4;
        height: 50;
        width: 100%;
        border-width: 10;
        border-color: #000000;
        background-color: transparent;
    }

    .picker {
        height: 10;
        border-radius: 2;
        border-width: 1;
    }
</style>