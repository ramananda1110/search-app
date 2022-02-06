package com.pluang.stockapp.network

import com.pluang.searchapp.BuildConfig
import com.pluang.searchapp.data.model.SearchDataResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    companion object Factory {
        fun create(): ApiService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_BASE_URL)
                .build()

            return retrofit.create(ApiService::class.java);

        }

    }

    @Headers("Accept: application/json")
    @GET("search/photos?")
    fun getSearchImage(
        @Query("client_id") clintId: String,
        @Query("per_page") pageSize: Int,
        @Query("page") maxSize: Int,
        @Query("query") query: String
    ): Call<SearchDataResponse?>?


}