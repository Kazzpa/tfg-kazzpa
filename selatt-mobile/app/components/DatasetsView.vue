<template>
    <Page @loaded="on_load">
        <ActionBar>
            <NavigationButton text="Nav" android.systemIcon="ic_menu_more" @tap="$refs.drawer.nativeView.showDrawer()"/>
            <Label class="title" text="SelAtt - Datasets" />
        </ActionBar>

        <RadSideDrawer ref="drawer">
            <StackLayout class="drawer-header">
                <Label color="white" text="SelAtt"/>
            </StackLayout>
            <GridLayout ~mainContent columns="*" rows="*">
                <StackLayout>
                    <Label class="h3">
                        Datasets
                    </Label>
                    <ListView v-if="datasetsGrid != null" for="item in datasets">
                        <v-template>
                            <StackLayout orientation="horizontal">
                                <Label :text="item.filename"/>
                                <Label :text="item.size"/>
                                <Label :text="item.units"/>
                            </StackLayout>
                        </v-template>
                    </ListView>
                </StackLayout>
            </GridLayout>
        </RadSideDrawer>

    </Page>
</template>
<script>
    import {mapGetters, mapActions} from 'vuex'
    import axios from 'axios';
    import * as config from '../config.js';

    export default {
        data() {
            return {
                msg: 'Hello World!',
                datasets: null,
                datasetsGrid: null,
                search: '',
            }
        },
        computed: {
            ...mapGetters({
                getUser: 'auth/getUser',
            })
        },
        methods: {
            ...mapActions({}),
            goToLogin() {

                this.$navigator.navigate('login', {clearHistory: true});
            },
            goToProfile() {
                this.$navigator.navigate('profile', {clearHistory: true});
            },
            goToAlgorithms() {
                this.$navigator.navigate('algorithms', {clearHistory: true});
            },
            goToResults() {
                this.$navigator.navigate('results', {clearHistory: true});
            },
            genColumnsData() {
                var aux = [];
                var i = 0;
                var j = 0;
                this.datasets.forEach(elem => {
                    aux.push({label: elem.filename, id: i--, row: j++, col: 0});
                });
                console.log(aux);
                this.datasetsGrid = aux;
            },
            on_load() {
                let url = config.BACKEND + "/featureSelection/datasets/";
                return axios.get(url,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "Authorization": "Bearer " + this.$store.state.auth.user.token,
                        }
                    })
                    .then(response => {
                        response.data.forEach(elem => {
                            if (elem.size > 1024) {
                                elem.size = Math.round(((elem.size + Number.EPSILON) / 1024) * 100) / 100;
                                elem.units = "Mb";
                                if (elem.size > 1024) {
                                    elem.size = Math.round(((elem.size + Number.EPSILON) / 1024) * 100) / 100;
                                    elem.units = "Gb";
                                }
                            } else {
                                elem.units = "Kb";
                            }
                        });
                        console.log(response.data);
                        this.datasets = response.data;
                        this.genColumnsData();
                    })
                    .catch(error => {
                        console.log(error);

                    });
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
        background: #2196f3;
        background-image: url("~/assets/images/backgroundDrawer.jpg");
        background-size: cover;
    }

    .drawer-item {
        padding: 8 16;
        font-size: 16;
    }
</style>
