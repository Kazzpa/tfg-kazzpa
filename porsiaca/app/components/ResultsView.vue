<template>
  <Page>
    <StackLayout>
      <Label>
        Resultados de ejecuciones previos
      </Label>
      <TextField
          v-model="search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
      />
      <ListView for="item in countries">
        <v-template>
          <GridLayout columns="*, *, *">
            <Button :text="`${item} - B1`" col="0"
                    @tap="onTap(item, 'b1', $event)"/>
            <Button :text="`${item} - B2`" col="1"
                    @tap="onTap(item, 'b2', $event)"/>
            <Button :text="`${item} - B3`" col="2"
                    @tap="onTap(item, 'b3', $event)"/>

          </GridLayout>
        </v-template>
      </ListView>
      <template v-slot:item.actions="{ item }">
      </template>
      <Label v-if="resultChosen" v-model="resultChosen">
        <StackLayout>
          <Label>
            {{ this.resultChosen.jsonAttributes }}
          </Label>
          <Button @click="resultChosen = null">
            Cerrar
          </Button>
        </StackLayout>
      </Label>
    </StackLayout>
  </Page>
</template>

<script>
import axios from "axios";
import LandingView from "./LandingView";
import LoginView from "./LoginView";


const server_url = process.env.VUE_APP_API_SERVER_URL;


require('axios-debug-log');
localStorage.debug = "axios";

export default {
  name: "ResultsView",
  components: {LandingView},
  data: () => ({
    search: '',
    headers: [
      {text: "Dataset", align: "start", value: "performed.filename"},
      {text: "Algoritmo seleccionado", value: "algorithm.name"},
      {text: "Tamaño del dataset(bytes)", value: "performed.size"},
      {text: 'Finished Date', value: 'finishedDate', sortable: true},
      {text: 'Attributos seleccionados', value: 'actions', sortable: false},
    ],
    results: null,
    resultChosen: null,
  }), computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  methods: {
    viewItem(item) {
      this.resultChosen = item;
    }
  },
  created() {
    if (!this.loggedIn) {
      this.$navigateTo(LoginView);
    } else {
      let url = server_url + "/featureSelection/resultsByUser/";
      axios.get(url,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": "Bearer " + this.$store.state.auth.user.token,
            }
          })
          .then(response => {
            var res = response.data;
            res.forEach(value => {
                  value.jsonAttributes = JSON.parse(value.jsonAttributes);
                  var arr = [];
                  for (var i = 0; i < value.jsonAttributes.length; i++) {
                    arr[i] = value.jsonAttributes[i].id;
                  }
                  value.jsonAttributes = arr;
                  console.log(arr);
                }
            );
            this.results = response.data;


          })
          .catch(error => {
            console.log(error);

          });
    }
  }
}
</script>

<style scoped>

</style>