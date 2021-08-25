package com.graduate.howtospeak.Retrofit

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.opencv.photo.Photo
import retrofit2.Call
import retrofit2.http.*

open interface IUploadAPI {
    @Multipart
    @POST("/vowel_recognition/")
    fun post_image(
        @Part imageFile: MultipartBody.Part
    ) : Call<String>
}

//   cd /srv/django_aws_test/media/tmp/