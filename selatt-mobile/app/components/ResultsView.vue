<template>
  <Page>
    <ActionBar>
      <GridLayout width="100%" columns="auto, *">
        <Label text="MENU" @tap="$refs.drawer.nativeView.showDrawer()" col="0"/>
        <Label class="title" text="Home" col="1"/>
      </GridLayout>
    </ActionBar>

    <RadSideDrawer ref="drawer">
      <StackLayout ~drawerContent backgroundColor="#ffffff">
        <Label class="drawer-header" text="Drawer"/>

        <Label class="drawer-item" text="Login" @tap="goToLogin"/>
        <Label class="drawer-item" text="Item 2"/>
        <Label class="drawer-item" text="Item 3"/>
      </StackLayout>

      <GridLayout ~mainContent columns="*" rows="*">
        <Label>
          Resultados de ejecuciones previos
        </Label>
        <SearchBar
            v-model="search"
        />
        <ListView for="item in results">
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
            <Button @tap="resultChosen = null">
              Cerrar
            </Button>
          </StackLayout>
        </Label>
      </GridLayout>
    </RadSideDrawer>

  </Page>
</template>
<script>

import {mapGetters, mapActions} from 'vuex'
import routes from "./routes";
import * as config from '../config.js';
import axios from 'axios';

export default {
  data() {
    return {
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
    }
  }, computed: {
    ...mapGetters({
      getToken: 'auth/getToken',
      getUser: 'auth/getUser',
    }),
    loggedIn() {
      return this.getUser != null;
    }
  },
  methods: {
    viewItem(item) {
      this.resultChosen = item;
    },
    goToLogin() {
      this.$navigateTo(routes.LoginView);
    },
    created() {
      if (!this.loggedIn) {
        this.$navigateTo(routes.LoginView);
      } else {
        let url = config.BACKEND + "/featureSelection/resultsByUser/";
        axios.get(url,
            {
              headers: {
                "Content-Type": "multipart/form-data",
                "Authorization": "Bearer " + this.getToken,
              }
            })
            .then(response => {
              console.log(response.data);
              this.results = response.data;


            })
            .catch(error => {
              console.log(error);

            });
      }
    }
  },
}
</script>