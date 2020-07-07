<template>
    <v-container>
        <v-jumbotron>
            <h3>
                <strong>{{currentUser.username}}</strong> Profile
            </h3>
        </v-jumbotron>
        <p>
            <strong>Token:</strong>
            {{currentUser.accessToken.substring(0, 20)}} ...
            {{currentUser.accessToken.substr(currentUser.accessToken.length - 20)}}
        </p>
        <p>
            <strong>Id:</strong>
            {{currentUser.id}}
        </p>
        <p>
            <strong>Email:</strong>
            {{currentUser.email}}
        </p>
        <strong>Authorities:</strong>
        <ul>
            <li v-for="(role,index) in currentUser.roles" :key="index">{{role}}</li>
        </ul>
    </v-container>
</template>

<script>
    import VueRouter from 'vue-router';
    import Vue from 'vue';
    import LoginView from "@/components/LoginView";
    import RegisterView from "@/components/RegisterView";
    Vue.use(VueRouter);


    const login_path = process.env.VUE_APP_LOGIN_PATH;
    const profile_path = process.env.VUE_APP_PROFILE_PATH;
    const register_path = process.env.VUE_APP_REGISTER_PATH;
    console.log(login_path);

    const router = new VueRouter({
        routes: [
            // dynamic segments start with a colon
            {path: profile_path, component: LoginView},
            {path: register_path, component: RegisterView}
        ]
    })
    export default {
        name: "ProfileView",
        router,
        computed: {
            currentUser() {
                return this.$store.state.auth.user;
            }
        },
        mounted() {
            if (!this.currentUser) {
                this.$router.push(login_path);
            }
        }
    }
</script>

<style scoped>

</style>