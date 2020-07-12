<template>
    <v-app>
        <v-app-bar
                app
                color="primary"
                dark
        >
            <div class="d-flex align-center">
                <v-icon @click="goToHome">mdi-home</v-icon>
                SelAtt
            </div>

            <v-spacer></v-spacer>
            <v-spacer></v-spacer>
            <v-btn v-on:click="goToLogin">
                Login
                <v-icon>mdi-login</v-icon>
            </v-btn>
        </v-app-bar>


        <router-view></router-view>
    </v-app>
</template>

<script>
    import Vue from 'vue';
    import VueRouter from 'vue-router';
    import AlgorithmsView from '@/components/AlgorithmsView';
    import RegisterView from "@/components/RegisterView";
    import LoginView from "@/components/LoginView";
    import ProfileView from "@/components/ProfileView";
    import LandingView from "@/components/LandingView";

    Vue.use(VueRouter);
    const profile_path = process.env.VUE_APP_PROFILE_PATH;
    const register_path = process.env.VUE_APP_REGISTER_PATH;
    const algorithm_path = process.env.VUE_APP_ALGORITHM_PATH;
    const landing_path = process.env.VUE_APP_LANDING_PATH;
    const login_path = process.env.VUE_APP_LOGIN_PATH;

    const router = new VueRouter({
        routes: [
            // dynamic segments start with a colon
            {path: login_path, component: LoginView},
            {path: profile_path, component: ProfileView},
            {path: algorithm_path, component: AlgorithmsView},
            {path: register_path, component: RegisterView},
            {path: landing_path, component: LandingView}
        ]
    })
    export default {
        name: 'App',
        router,

        data: () => ({
            //
        }),
        methods: {
            goToLogin() {
                router.push(login_path);
            },
            goToHome() {
                router.push(landing_path);
            }
        }
    };
</script>
