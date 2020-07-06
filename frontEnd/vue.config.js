module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  devServer:{
    proxy: {
      '/fileUpload/uploadDataset': {
        target: 'http://localhost:8082',
        ws: true,
        changeOrigin: true
      }
    }
  }
}