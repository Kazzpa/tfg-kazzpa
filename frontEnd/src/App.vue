<template>
    <v-app>
        <v-app-bar
                app
                color="primary"
                dark
        >
            <div class="d-flex align-center" >
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
    import HelloWorld from '@/components/HomeView';
    import UserView from "@/components/UserView";

    Vue.use(VueRouter);
    const router = new VueRouter({
        routes: [
            // dynamic segments start with a colon
            {path: '/User', component: UserView},
            {path: '/', component: HelloWorld}
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
                router.push("/User");
            },
            goToHome() {
                router.push("/");
            }
        }
    };
</script>
