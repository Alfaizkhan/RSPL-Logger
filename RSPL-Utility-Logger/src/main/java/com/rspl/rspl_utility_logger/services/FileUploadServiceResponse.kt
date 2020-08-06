package com.rspl.rspl_utility_logger.services

import com.google.gson.annotations.SerializedName

data class FileUploadServiceResponse(

    @SerializedName("isSuccess")
    val isSuccess: Boolean,

    @SerializedName("message")
    val message: String
)