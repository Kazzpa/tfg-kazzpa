# Generated by Django 2.2.6 on 2020-01-15 15:30

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('result', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='dataset',
            name='file',
        ),
        migrations.AlterField(
            model_name='dataset',
            name='pub_date',
            field=models.DateTimeField(),
        ),
    ]