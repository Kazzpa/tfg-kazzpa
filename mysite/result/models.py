from django.db import models
from django.conf import settings


# Create your models here.
class Dataset(models.Model):
    name = models.CharField(max_length=250)
    user_uploaded = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    #Type of file
    file_type = models.CharField(max_length=5)
    #Size in Kb
    size = models.FloatField()
    file = models.FilePathField()
    hash = models.CharField(max_length=100)
    pub_date = models.DateTimeField()

    def __str__(self):
        return self.name


class Algorithm(models.Model):
    name = models.CharField(max_length=100)
    program_language = models.CharField(max_length=10)

    def __str__(self):
        return self.name

#TODO anyadir atributos seleccionados y numero de atributos utilizados para mostrar en vistas

class Result(models.Model):
    user_run = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    algorithm_used = models.ForeignKey(Algorithm, on_delete=models.CASCADE)
    dataset_used = models.ForeignKey(Dataset, on_delete=models.CASCADE)
    accuracy = models.FloatField(max_length=4)
    percentage_done = models.FloatField(max_length=4)
    atr_new = models.IntegerField(default=3)
    atr_old = models.IntegerField(default=10)

    def __str__(self):
        return self.user_run.get_username() + "" + self.algorithm_used.name +" " +self.dataset_used.name
