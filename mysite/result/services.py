from django.shortcuts import request

class services():
# TODO change url to use from properties
    def get_results(self):
        url = 'http://localhost:8082/featureReduction/pca'
        # params = {'success': year, 'author': author}ç
        # r = request.get(url, params=params)
        r = request.get(url)
        result = r.json()
        result_list = {'results': result['results']}
        return result_list
