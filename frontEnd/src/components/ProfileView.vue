<template>
    <v-main>
        <v-card>
            <v-card-title>
                <strong>{{currentUser.username}}</strong> Profile
            </v-card-title>
        </v-card>
        <p>
            <strong>Token:</strong>
            {{currentUser.token.substring(0, 20)}} ...
            {{currentUser.token.substr(currentUser.token.length - 20)}}
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
        <v-btn v-on:click="goToAlgorithm">Algoritmos</v-btn>
    </v-main>
</template>

<script>
    import VueRouter from 'vue-router';
    import Vue from 'vue';
    Vue.use(VueRouter);


    const login_path = process.env.VUE_APP_LOGIN_PATH;
    const algorithm_path = process.env.VUE_APP_ALGORITHM_PATH;
    console.log(login_path);

    export default {
        name: "ProfileView",
        computed: {
            currentUser() {
                console.log(this.$store.state.auth.user);
                return this.$store.state.auth.user;
            }
        },
        mounted() {
            if (!this.currentUser) {
                this.$router.push(login_path);
            }
        },
        methods: {
            goToAlgorithm(){
                this.$router.push(algorithm_path);
            }
        }
    }
</script>

<style scoped>

</style>