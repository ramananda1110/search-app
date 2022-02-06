package com.pluang.searchapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluang.searchapp.data.model.SearchDataResponse
import com.pluang.searchapp.ui.repository.SearchRepository


class SearchDataViewModel(application: Application) : AndroidViewModel(application) {
    var repository: SearchRepository = SearchRepository()

    val updateStatus: LiveData<Boolean>
        get() = repository.updateStatus


    fun searchImages(clientId: String, query: String): MutableLiveData<SearchDataResponse> {
        return repository.getImages(clientId, 10, 20, query)
    }

}