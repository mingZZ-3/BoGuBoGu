package com.graduate.howtospeak.Retrofit

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

open interface IUploadAPI {
    @Multipart
    @POST("/vowel_recognition/")
    fun post_image(
        @Part imageFile: MultipartBody.Part
    ) : Call<String>
}