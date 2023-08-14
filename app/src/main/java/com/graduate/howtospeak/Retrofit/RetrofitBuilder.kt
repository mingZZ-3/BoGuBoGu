package com.graduate.howtospeak.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object RetrofitBuilder {
    val api: IUploadAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("url")  // http://13.124.114.1/
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(IUploadAPI::class.java)
    }
}
