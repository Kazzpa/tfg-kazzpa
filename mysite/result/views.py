from django.http import HttpResponseRedirect
from django.shortcuts import get_object_or_404, render
from django.urls import reverse
from django.views import generic

from result.models import Result


class IndexView(generic.ListView):
    template_name = 'result/index.html'
    context_object_name = 'result_list'

    def get_queryset(self):
        """Return the last five published questions."""
        return Result.objects.order_by('-accuracy')[:5]


class DetailView(generic.DetailView):
    model = Result
    template_name = 'result/detail.html'
