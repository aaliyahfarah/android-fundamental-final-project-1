package com.dicoding.usergithubapp

import com.dicoding.usergithubapp.Constanta.BASE_URL
import com.dicoding.usergithubapp.Constanta.GITHUB_TOKEN
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private fun client(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", GITHUB_TOKEN)
                val request = requestBuilder.build()
                it.proceed(request)
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    fun create(): ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiService::class.java)
}