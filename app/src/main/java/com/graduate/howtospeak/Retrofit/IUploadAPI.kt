package com.graduate.howtospeak.Retrofit

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.opencv.photo.Photo
import retrofit2.Call
import retrofit2.http.*

open interface IUploadAPI {
    //@FormUrlEncoded
    @Multipart
    //@POST("vowel_recognition")
    @POST("media/images/2021/08/24")
    //@POST("srv")
    fun post_image(
        @Part PostImg: MultipartBody.Part
    ) :  Call<String>
}

//   cd /srv/django_aws_test/media/images/2021/08/24/