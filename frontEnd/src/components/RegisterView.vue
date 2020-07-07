<template>
    <v-main>
        <v-row>
            <v-col/>
            <v-col>
                <v-card>
                    <v-card-title>
                        Registro
                        <v-icon>mdi-account-box</v-icon>
                    </v-card-title>
                    <v-card-text>
                        <v-form name="form" @submit.prevent="handleRegister">
                            <div v-if="!successful">
                                <v-text-field
                                        v-model="user.username"
                                        v-validate="'required|min:3|max:20'"
                                        type="text"
                                        class="form-control"
                                        name="username"
                                        label="Usuario"
                                />
                                <div
                                        v-if="submitted && errors.has('username')"
                                        class="alert-danger"
                                >{{errors.first('username')}}
                                </div>
                                <v-text-field
                                        v-model="user.email"
                                        v-validate="'required|email|max:50'"
                                        type="email"
                                        class="form-control"
                                        name="email"
                                        label="Correo"
                                />
                                <div
                                        v-if="submitted && errors.has('email')"
                                        class="alert-danger"
                                >{{errors.first('email')}}
                                </div>
                                <v-text-field
                                        v-model="user.password"
                                        v-validate="'required|min:6|max:40'"
                                        class="form-control"
                                        name="password"
                                        label="Contraseña"
                                        :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                                        :type="show1 ? 'text' : 'password'"
                                        @click:append="show1 = !show1"
                                />

                                <div
                                        v-if="submitted && errors.has('password')"
                                        class="alert-danger"
                                >{{errors.first('password')}}
                                </div>
                                <v-btn color="primary">Sign Up</v-btn>
                            </div>
                        </v-form>

                        <div
                                v-if="message"
                                class="alert"
                                :class="successful ? 'alert-success' : 'alert-danger'"
                        >{{message}}
                        </div>
                    </v-card-text>
                </v-card>
            </v-col>
            <v-col/>
        </v-row>
    </v-main>
</template>

<script>
    import User from "@/models/user";

    export default {
        name: "RegisterView",
        data() {
            return {
                user: new User('', '', ''),
                submitted: false,
                successful: false,
                message: ''
            };
        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        mounted() {
            if (this.loggedIn) {
                this.$router.push('/profile');
            }
        },
        methods: {
            handleRegister() {
                this.message = '';
                this.submitted = true;
                this.$validator.validate().then(isValid => {
                    if (isValid) {
                        console.log("Validacion ok");
                        this.$store.dispatch('auth/register', this.user).then(
                            data => {

                                this.message = data.message;
                                this.successful = true;
                            },
                            error => {
                                this.message =
                                    (error.response && error.response.data) ||
                                    error.message ||
                                    error.toString();
                                this.successful = false;
                            }
                        );
                    }

                    console.log("Validacion not ok");
                });
            }
        }
    }
</script>

<style scoped>

</style>