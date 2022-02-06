package com.pluang.searchapp.ui.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluang.searchapp.data.model.SearchDataResponse
import com.pluang.stockapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository {
    private val isUpdated = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean>
        get() = isUpdated

    fun getImages(
        clientId: String,
        pageSize: Int,
        maxSize: Int,
        query: String
    ): MutableLiveData<SearchDataResponse> {
        val _stockList = MutableLiveData<SearchDataResponse>()
        isUpdated.setValue(true)
        ApiService.create().getSearchImage(clientId, pageSize, maxSize, query)
            ?.enqueue(object : Callback<SearchDataResponse?> {
                override fun onResponse(
                    call: Call<SearchDataResponse?>,
                    response: Response<SearchDataResponse?>
                ) {
                    if (response.isSuccessful) {
                        isUpdated.setValue(false)
                        _stockList.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<SearchDataResponse?>, t: Throwable) {
                    Log.e("SearchRepository", t.message.toString());
                    isUpdated.setValue(false)
                }
            })
        return _stockList
    }


}


