package com.rspl.rspl_utility_logger.services

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface FileUploadService {

    @Multipart
    @POST
    fun uploadLogFile(@Url url: String, @Part logFile: MultipartBody.Part): Call<FileUploadServiceResponse>
}