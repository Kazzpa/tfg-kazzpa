<template>
    <v-main>
        <v-row>
            <v-col></v-col>
            <v-col>
                <div class="d-flex justify-space-around">
                    <v-img :src="require('../assets/logo.png')" max-height="120" max-width="120"></v-img>
                </div>
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
                    <v-btn class="mx-2" color="primary" type="submit" :loading="loading">
                        <span>Login</span>
                    </v-btn>
                    <v-btn class="secondary mx-2" v-on:click="goToRegister">Registro</v-btn>
                    <v-alert v-if="message" class="alert alert-danger" role="alert">{{ message }}</v-alert>
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

    Vue.use(VueRouter);
    Vue.use(VeeValidate);

    const profile_path = process.env.VUE_APP_PROFILE_PATH;
    const register_path = process.env.VUE_APP_REGISTER_PATH;
    export default {
        name: "LoginView",
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
            goToRegister() {
                this.$router.push(register_path);
            },
            handleLogin() {
                this.loading = true;
                this.$validator.validateAll().then(isValid => {
                    if (!isValid) {
                        this.loading = false;
                        return;
                    }

                    if (this.user.username && this.user.password) {
                        this.$store.dispatch("auth/login", this.user).then(
                            () => {
                                this.$router.push(profile_path);
                            },
                            error => {
                                this.loading = false;
                                if (error.response.status == "401") {
                                    this.message = "Credenciales incorrectas";
                                } else {
                                    this.message =
                                        (error.response && error.response.data) ||
                                        error.message ||
                                        error.toString();

                                }
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