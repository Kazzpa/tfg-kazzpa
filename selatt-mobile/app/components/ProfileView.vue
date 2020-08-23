<template>
    <Page>
        <ActionBar>
            <NavigationButton text="Nav" android.systemIcon="ic_menu_more" @tap="$refs.drawer.nativeView.showDrawer()"/>
            <Label class="title" text="SelAtt - Profile" />
        </ActionBar>

        <RadSideDrawer ref="drawer">
            <StackLayout ~drawerContent backgroundColor="#ffffff">
                <StackLayout class="drawer-header">
                    <Label color="white" text="SelAtt"/>
                </StackLayout>
                <Label class="drawer-item" text="Login" @tap="goToLogin"/>
                <Label class="drawer-item" text="Datasets" @tap="goToDatasets"/>
                <Label class="drawer-item" text="Home" @tap="goToApp"/>
                <Label class="drawer-item" text="Results" @tap="goToResults"/>
            </StackLayout>

            <StackLayout ~mainContent columns="*" rows="*">
                <Label text="Perfil de:"/>
                <Label class="h2" :text="username"></Label>
                <Button text="Ejecuciones" @tap="goToAlgorithms"></Button>
                <Button class="h3 " @tap="log_out">
                    <FormattedString>
                        <Span text="LogOut" style="color: red"/>
                    </FormattedString>
                </Button>
            </StackLayout>
        </RadSideDrawer>
    </Page>
</template>
<script>
    import {mapActions, mapGetters} from 'vuex';
    import {ApplicationSettings} from "@nativescript/core";

    export default {
        data() {
            return {
                message: "",
                username: this.getUsername,
            }
        },
        computed: {
            ...mapGetters({
                getUser: 'auth/getUser',
                getUsername: 'auth/getUsername',
                getToken: 'auth/getToken',
            }),
        },
        methods: {
            ...mapActions({
                logout: 'auth/logout'
            }),
            goToLogin() {
                this.$navigator.navigate('login', {clearHistory: true});
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
            goToApp() {
                this.$navigator.navigate('/', {clearHistory: true});
            },
            log_out() {
                this.logout();
                try {

                    if (ApplicationSettings.hasKey("userData")) {
                        console.log("Removing appsettings");
                        ApplicationSettings.remove("userData");
                        console.log("Flushing appsettings");
                        ApplicationSettings.flush();
                        console.log("Navigating to Login View");
                        this.$navigator.navigate('login', {clearHistory: true});
                    }
                } catch (error) {
                    console.log("check2error");
                    console.log(error);
                }
            }
        }
    }

</script>
<style scoped>

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