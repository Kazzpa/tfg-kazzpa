<template>
    <v-main>
        <v-form name="form" @submit.prevent="handleLogin">
            <div class="form-group">
                <v-text-field
                        v-model="user.username"
                        v-validate="'required'"
                        type="text"
                        class="form-control"
                        name="username"
                        Label="Username"
                />
                <div
                        v-if="errors.has('username')"
                        class="alert alert-danger"
                        role="alert"
                >Username is required!
                </div>
            </div>
            <div class="form-group">
                <v-text-field
                        v-model="user.password"
                        v-validate="'required'"
                        type="password"
                        class="form-control"
                        name="password"
                        label="Password"
                />
                <div
                        v-if="errors.has('password')"
                        class="alert alert-danger"
                        role="alert"
                >Password is required!
                </div>
            </div>
            <div class="form-group">
                <v-btn class="btn btn-primary btn-block" :disabled="loading">
                    <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                    <span>Login</span>
                </v-btn>
            </div>
            <div class="form-group">
                <v-alert v-if="message" class="alert alert-danger" role="alert">{{message}}</v-alert>
            </div>
        </v-form>
    </v-main>
</template>

<script>
    import Vue from 'vue';
    import VueRouter from 'vue-router';
    import User from '../models/user';
    import HelloWorld from '@/components/HomeView';

    Vue.use(VueRouter);
    const router = new VueRouter({
        routes: [
            // dynamic segments start with a colon
            {path: '/', component: HelloWorld}
        ]
    })
    export default {
        name: "Login",
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
                this.$router.push('/profile');
            }
        },
        methods: {
            handleLogin() {
                this.loading = true;
                this.$validator.validateAll().then(isValid => {
                    if (!isValid) {
                        this.loading = false;
                        return;
                    }

                    if (this.user.username && this.user.password) {
                        this.$store.dispatch('auth/login', this.user).then(
                            () => {
                                this.$router.push('/profile');
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