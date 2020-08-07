<template>
    <v-main>
        <v-card>
            <v-card-title>
                <strong>{{currentUser.username}}</strong> <v-spacer/>Profile
            </v-card-title>
        </v-card>

        <v-btn v-on:click="goToAlgorithm">Algoritmos</v-btn>
    </v-main>
</template>

<script>
    import VueRouter from 'vue-router';
    import Vue from 'vue';
    Vue.use(VueRouter);


    const login_path = process.env.VUE_APP_LOGIN_PATH;
    const algorithm_path = process.env.VUE_APP_ALGORITHM_PATH;

    export default {
        name: "ProfileView",
        computed: {
            currentUser() {
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