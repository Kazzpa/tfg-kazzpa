<template>
    <v-main>
        <v-row>
            <v-col></v-col>
            <v-col>
                <v-form name="form" @submit.prevent="handleLogin">
                    <v-text-field
                            v-model="user.username"
                            v-validate="'required'"
                            type="text"
                            name="username"
                            label="Usuario"
                    />
                    <div
                            v-if="errors.has('username')"
                            class="alert alert-danger"
                            role="alert"
                    >Username is required!
                    </div>
                    <v-text-field
                            v-model="user.password"
                            v-validate="'required'"
                            type="password"
                            name="password"
                            label="Contraseña"
                    />
                    <div
                            v-if="errors.has('password')"
                            class="alert alert-danger"
                            role="alert"
                    >Password is required!
                    </div>
                    <v-btn color="primary" type="submit" :disabled="loading">
                        <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                        <span>Login</span>
                    </v-btn>
                    <v-alert v-if="message" class="alert alert-danger" role="alert">{{message}}</v-alert>
                </v-form>
            </v-col>
            <v-col col=""></v-col>
        </v-row>
    </v-main>
</template>

<script>
    import Vue from 'vue';
    import VueRouter from 'vue-router';
    import User from '../models/user';
    import VeeValidate from 'vee-validate';
    import HelloWorld from '@/components/HomeView';
    import RegisterView from "@/components/RegisterView";
    import ProfileView from "@/components/ProfileView";

    Vue.use(VueRouter);
    Vue.use(VeeValidate);

    const profile_path = process.env.VUE_APP_PROFILE_PATH;
    const home_path = process.env.VUE_APP_HOME_PATH;
    const register_path = process.env.VUE_APP_REGISTER_PATH;
    const router = new VueRouter({
        routes: [
            // dynamic segments start with a colon
            {path: home_path, component: HelloWorld},
            {path: register_path, component: RegisterView},
            {path: profile_path, component: ProfileView}
        ]
    })
    export default {
        name: "LoginView",
        router,
        data() {
            return {
                user: new User('', ''),
                loading: false,
                message: ''
            };
        }, computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        created() {
            if (this.loggedIn) {
                this.$router.push(profile_path);
            }
        },
        methods: {
            handleLogin() {
                console.log("Prueba login");
                console.log(this.user.username, this.user.password);
                this.loading = true;
                this.$validator.validateAll().then(isValid => {
                    if (!isValid) {
                        this.loading = false;
                        console.log("No es valido el formulario");
                        return;
                    }

                    console.log("Es valido el formulario");
                    if (this.user.username && this.user.password) {
                        this.$store.dispatch("auth/login", this.user).then(
                            () => {
                                this.$router.push(profile_path);
                            },
                            error => {
                                this.loading = false;
                                this.message =
                                    (error.response && error.response.data) ||
                                    error.message ||
                                    error.toString();
                            }
                        );
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>