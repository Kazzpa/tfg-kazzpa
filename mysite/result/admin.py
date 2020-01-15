from django.contrib import admin
from .models import Algorithm, Result, Dataset

# Register your models here.
admin.site.register(Algorithm)
admin.site.register(Dataset)
admin.site.register(Result)