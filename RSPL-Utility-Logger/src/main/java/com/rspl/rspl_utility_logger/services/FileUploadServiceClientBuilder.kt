package com.rspl.rspl_utility_logger.services

import com.rspl.rspl_utility_logger.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object FileUploadServiceClientBuilder {

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client by lazy {
        OkHttpClient.Builder().run {
            if (BuildConfig.DEBUG) {
                addInterceptor(httpLoggingInterceptor)
            }
            //addInterceptor(HeaderInterceptor())
            build()
        }
    }

    fun build(): FileUploadService {
        return Retrofit.Builder().run {
            baseUrl("https://127.0.0.1/apis/")
            client(client)
            addConverterFactory(GsonConverterFactory.create())
            build().create(FileUploadService::class.java)
        }
    }
}

private class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
            .header("Content-Type", "text/plain; charset=UTF-8")
            .method(original.method, original.body)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
