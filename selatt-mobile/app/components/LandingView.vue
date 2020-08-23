<template>
    <Page>
        <ActionBar>
            <NavigationButton text="Nav" android.systemIcon="ic_menu_more" @tap="$refs.drawer.nativeView.showDrawer()"/>
            <Label class="title" text="SelAtt" />
        </ActionBar>

        <RadSideDrawer ref="drawer">
            <StackLayout ~drawerContent backgroundColor="#ffffff">
                <StackLayout class="drawer-header">
                    <Label color="white" text="SelAtt"/>
                </StackLayout>
                <Label v-if="getUser!=null" class="drawer-item" text="Login" @tap="goToLogin"/>
                <template v-else>
                    <Label class="drawer-item" text="Profile" @tap="goToProfile"/>
                    <Label class="drawer-item" text="Datasets" @tap="goToDatasets"/>
                    <Label class="drawer-item" text="Algorithms" @tap="goToAlgorithms"/>
                    <Label class="drawer-item" text="Results" @tap="goToResults"/>

                </template>
            </StackLayout>

            <GridLayout ~mainContent columns="*" rows="*">
                <StackLayout>
                    <Image class="nt-form__logo" height="20%" src="~/assets/images/logotipo-texto.png"/>
                    <Label textWrap="true">
                        <FormattedString>
                        <span text="Aplicación de data mining especializada en selección de atributos a servicio"
                              class="text-center"/>
                            <span :text="newLine"/>
                            <span class="text-center" text=" mediante frameworks de última generación como"/>
                            <span class="text-center" text ="   Spring Boot y VueJs" fontWeight="bold"/>
                        </FormattedString>
                    </Label>
                </StackLayout>
            </GridLayout>
        </RadSideDrawer>

    </Page>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex'

    export default {
        data() {
            return {
                msg: 'Hello World!',
                newLine: '\n',
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
            goToDatasets() {
                this.$navigator.navigate('datasets', {clearHistory: true});
            },
            goToAlgorithms() {
                this.$navigator.navigate('algorithms', {clearHistory: true});
            },
            goToResults() {
                this.$navigator.navigate('results', {clearHistory: true});
            },
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
