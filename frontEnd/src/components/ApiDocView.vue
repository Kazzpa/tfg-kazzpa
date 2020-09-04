<template>
  <v-main>
    <v-row>
      <v-col cols="md-2 d-sm-none d-md-flex"></v-col>
      <v-col cols="md-8">
        <div class="text-h3 mb-6">API REST PÚBLICA SELATT
        </div>
        <p>
          SelAtt proporciona servicio mediante una API RESTFul pública a que puede acceder cualquier persona registrada,
          para registrarse se puede hacer mediante la aplicación web o mediante la propia API.
          Los servicios de esta se detallaran en cada apartado</p>
        <p>Se usaran por defecto <strong>application/json</strong>
          como formato para las peticiones/respuestas</p>
        <div class="text-h5">
          Servicios de la API REST PUBLICA
        </div>
        <ol class="mx-2">
          <li><a>Autentificación</a></li>
          <li><a>Datasets</a></li>
          <li><a>Selección de atributos</a></li>
          <li><a>Clasificadores</a></li>
        </ol>
        <div class="mx-2 my-4 text-h4">
          Autentificación
        </div>
        <v-expansion-panels>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 green white--text">POST</v-btn>
              auth/login
              <v-spacer/>
              Devuelve un JWT de autentificación para las peticiones.
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>

                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>Usuario</td>
                  <td>
                    Ejemplo de Usuario
                    <pre>
{
"username": "usuario",
"password": "contrasenya"
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Ejemplo de respuesta
                    <pre>
{
"token": "DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 green white--text">POST</v-btn>
              auth/register
              <v-spacer/>
              Registro en el sistema.
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-divider/>
              <v-simple-table>
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>Usuario</td>
                  <td>
                    Ejemplo de Usuario
                    <pre>
{
   "username":"usuario",
   "password":"contrasenya",
   "email":"name@domain.com"
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-divider/>
              <v-simple-table>
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Ejemplo de respuesta
                    <pre>
{
   "id":"f186020d-b25e-4080-b175-395c6d999f5c",
   "username":"usuario",
   "password":"$2a$10$1wqk8jkyyWLypZPoOiRKkOIyGqFiXf7.SGBNlpGy.JZPJNBjSeHLS",
   "email":"andres@correo.es",
   "role":"USER"
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>500</td>
                  <td>
                    Algunos campos son invalidos
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>

        <div class="mx-2 my-4 text-h4">
          Datasets
        </div>
        <v-expansion-panels>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 blue white--text">GET</v-btn>
              featureSelection/datasets
              <v-spacer/>
              Devuelve los datasets subidos
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Lista de datasets
                    <pre>
{"0":{
  "id":"4dca1378-830d-4a37-ae3d-5cb38ea0940d",
  "filename":"supermarket.arff",
  "name":null,
  "fileDownloadUri":"/Users/uploads\\202008191115_supermarket.arff",
  "fileType":"arff",
  "userUploader":null,
  "size":1978
}
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 green white--text">POST</v-btn>
              fileManager/uploadDataset
              <v-spacer/>
              Subir un dataset para procesarlo posteriormente.
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-divider/>
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>MultiPartFile</td>
                  <td>
                    Formatos permitidos:
                    <ul>
                      <li>Json para weka</li>
                      <li>Arff</li>
                      <li>Csv</li>
                    </ul>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-divider/>
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Ejemplo de respuesta
                    <pre>
{
"id":"a3d7c84a-e76b-47b2-802d-ed7b8ad8be7b",
"filename":"iris.json",
"name":null,
"fileDownloadUri":"/Users/uploads\\202008311219_iris.json",
"fileType":"application/json",
"userUploader":{
  "id":"450973af-0d93-4356-aa3e-d3d5fa0ecdcb",
  "username":"kazzpa",
},
"size":15
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>500</td>
                  <td>
                    Algunos campos son invalidos
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>

        <div class="mx-2 my-4 text-h4">
          Selección de atributos
        </div>
        <v-expansion-panels>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 blue white--text">GET</v-btn>
              featureSelection/{algorithm}/{datasetName}
              <v-spacer/>
              Devuelve el resultado de realizar una selección de atributos
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>

                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>algorithm</td>
                  <td>Se trata del algoritmo que se usara para seleccionar los atributos. Los valores aceptados son:
                    <ul>
                      <li>vns</li>
                      <li>fcbf</li>
                      <li>scs</li>
                      <li>exhaustive</li>
                      <li>ranker</li>
                      <li>bestfirst</li>
                    </ul>
                  </td>
                </tr>
                <tr>
                  <td>datasetName</td>
                  <td>Se trata del nombre del dataset sobre el que se desea realizar la selección de atributos</td>
                </tr>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Ejemplo de respuesta
                    <pre>
{
"id":"0a9150f1-d828-4469-ab6b-2e32fc3e98ae",
"algorithm":{
  "name":"ScatterSearchV1",
  "language":"Weka package",
  "type":"Feature Selection"
},
"performed":{
  "id":"63eff198-1b73-4063-ab85-a076502cca4a",
  "filename":"iris.arff",
  "name":null,
  "fileDownloadUri":"/Users/uploads\\202008181629_iris.arff",
  "fileType":"arff",
  "size":7
},
"attributesSelected":"2, 3",
"finishedDate":"2020-08-30T14:05:13.647+0000",
"seen":false
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                <tr>
                  <td>404</td>
                  <td>
                    DatasetName not found
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 blue white--text">GET</v-btn>
              featureSelection/results
              <v-spacer/>
              Devuelve todos los resultados
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Lista de resultados:
                    <pre>
{
"0":{
"id":"0a9150f1-d828-4469-ab6b-2e32fc3e98ae",
"algorithm":{
   "name":"Ranker",
   "language":"Weka package",
   "type":"Feature Selection"
},
"performed":{
   "id":"a664e990-009b-4948-8bd6-df580ebcc85e",
   "filename":"supermarket.arff",
   "name":null,
   "fileDownloadUri":"/Users/uploads\\202008191115_supermarket.arff",
   "fileType":"arff",
   "userUploader":null,
   "size":1978
},
"attributesSelected":"33,152, 151, 150, 153, 155, 136, 156, 159, 158, 157, 149, 148, 147, 146, 139, 138, 137",
"finishedDate":"2020-08-19T09:15:21.620+0000",
"seen":true
}
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 blue white--text">GET</v-btn>
              featureSelection/results/new
              <v-spacer/>
              Devuelve los resultados que no han sido vistos aún
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>

                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Lista de resultados:
                    <pre>
{
"0":{
"id":"0a9150f1-d828-4469-ab6b-2e32fc3e98ae",
"algorithm":{
   "name":"Ranker",
   "language":"Weka package",
   "type":"Feature Selection"
},
"performed":{
   "id":"a664e990-009b-4948-8bd6-df580ebcc85e",
   "filename":"supermarket.arff",
   "name":null,
   "fileDownloadUri":"/Users/uploads\\202008191115_supermarket.arff",
   "fileType":"arff",
   "userUploader":null,
   "size":1978
},
"attributesSelected":"33,152, 151, 150, 153, 155, 136, 156, 159, 158, 157, 149, 148, 147, 146, 139, 138, 137",
"finishedDate":"2020-08-19T09:15:21.620+0000",
"seen":false
}
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 green white--text">POST</v-btn>
              featureSelection/result/seen
              <v-spacer/>
              Marca el resultado como visto.
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>Resultado</td>
                  <td>
                    Ejemplo de resultado:
                    <pre>
{
"id":"0a9150f1-d828-4469-ab6b-2e32fc3e98ae",
"algorithm":{
   "name":"Ranker",
   "language":"Weka package",
   "type":"Feature Selection"
},
"performed":{
   "id":"a664e990-009b-4948-8bd6-df580ebcc85e",
   "filename":"supermarket.arff",
   "name":null,
   "fileDownloadUri":"/Users/uploads\\202008191115_supermarket.arff",
   "fileType":"arff",
   "userUploader":null,
   "size":1978
},
"seen":false
}
                    </pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Resultado marcado como visto con exito.
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>

        <div class="mx-2 my-4 text-h4">
          Clasificadores
        </div>
        <v-expansion-panels>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 blue white--text">GET</v-btn>
              evaluate/{algorithm}/{datasetName}
              <v-spacer/>
              Devuelve el resultado de realizar una clasificación
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>algorithm</td>
                  <td>Se trata del algoritmo que se usara para clasificar. Los valores aceptados son:
                    <ul>
                      <li>naivebayes</li>
                      <li>bayesnet</li>
                      <li>hnb</li>
                      <li>ibk</li>
                      <li>mlp</li>
                    </ul>
                  </td>
                </tr>
                <tr>
                  <td>datasetName</td>
                  <td>Se trata del nombre del dataset sobre el que se desea realizar la clasificación</td>
                </tr>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Ejemplo de respuesta
                    <pre>
{
"id":"15f60eb0-3af5-4c69-a6fd-16f84c062232",
"algorithm":{
  "name":"IBk",
  "language":"Weka Package",
  "type":"Classifier"
},
"performed":{
  "id":"f8bc21ee-8906-43c1-a6d3-d617c8d60899",
  "filename":"iris.arff",
  "name":null,
  "fileDownloadUri":"/Users/uploads\\202008181629_iris.arff",
  "fileType":"arff",
  "size":7
},
"finishedDate":"2020-08-25T12:03:34.700+0000",
"seen":true,
"featureAlgorithm":null,
"numInstances":150,
"correctlyClassified":142,
"meanAbsoluteError":0.04428518341561824
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                <tr>
                  <td>404</td>
                  <td>
                    DatasetName not found
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 blue white--text">GET</v-btn>
              evaluate/results
              <v-spacer/>
              Devuelve todos los resultados
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>

                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Lista de resultados:
                    <pre>
{
"0":{
"id":"15f60eb0-3af5-4c69-a6fd-16f84c062232",
"algorithm":{
  "name":"IBk",
  "language":"Weka Package",
  "type":"Classifier"
},
"performed":{
  "id":"f8bc21ee-8906-43c1-a6d3-d617c8d60899",
  "filename":"iris.arff",
  "name":null,
  "fileDownloadUri":"/Users/uploads\\202008181629_iris.arff",
  "fileType":"arff",
  "size":7
},
"finishedDate":"2020-08-25T12:03:34.700+0000",
"seen":true,
"featureAlgorithm":null,
"numInstances":150,
"correctlyClassified":142,
"meanAbsoluteError":0.04428518341561824
}
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 blue white--text">GET</v-btn>
              evaluate/results/new
              <v-spacer/>
              Devuelve los resultados que no han sido vistos aún
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Lista de resultados:
                    <pre>
{
"0":{
"id":"15f60eb0-3af5-4c69-a6fd-16f84c062232",
"algorithm":{
  "name":"IBk",
  "language":"Weka Package",
  "type":"Classifier"
},
"performed":{
  "id":"f8bc21ee-8906-43c1-a6d3-d617c8d60899",
  "filename":"iris.arff",
  "name":null,
  "fileDownloadUri":"/Users/uploads\\202008181629_iris.arff",
  "fileType":"arff",
  "size":7
},
"finishedDate":"2020-08-25T12:03:34.700+0000",
"seen":false,
"featureAlgorithm":null,
"numInstances":150,
"correctlyClassified":142,
"meanAbsoluteError":0.04428518341561824
}
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-btn max-width="5vh" small class="mx-2 green white--text">POST</v-btn>
              evaluate/result/seen
              <v-spacer/>
              Marca el resultado como visto.
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              Parametros:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>JWT</td>
                  <td>
                    Token de autorización en <strong>headers</strong> de petición
                    <pre>
{
"Authorization": "Bearer DA248EEAFFA573DA8C323C3EB56AA",
}</pre>
                  </td>
                </tr>
                <tr>
                  <td>Resultado</td>
                  <td>
                    Ejemplo de resultado:
                    <pre>
{
"id":"15f60eb0-3af5-4c69-a6fd-16f84c062232",
"algorithm":{
  "name":"IBk",
  "language":"Weka Package",
  "type":"Classifier"
},
"performed":{
  "id":"f8bc21ee-8906-43c1-a6d3-d617c8d60899",
  "filename":"iris.arff",
  "name":null,
  "fileDownloadUri":"/Users/uploads\\202008181629_iris.arff",
  "fileType":"arff",
  "size":7
},
"seen":false
}
                    </pre>
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
              Respuesta:
              <v-simple-table>
                <col style="width:10%">
                <col style="width:90%">
                <thead>
                <th class="text-left">Nombre</th>
                <th class="text-left">Descripción</th>
                </thead>
                <tbody>
                <tr>
                  <td>200</td>
                  <td>
                    Resultado marcado como visto con exito.
                  </td>
                </tr>
                <tr>
                  <td>401</td>
                  <td>
                    Not Authorized
                  </td>
                </tr>
                </tbody>
              </v-simple-table>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-col>
      <v-col cols="md-2 d-sm-none d-md-flex"></v-col>
    </v-row>
  </v-main>
</template>

<script>
export default {
  name: "ApiDocView"
}
</script>

<style scoped>

</style>