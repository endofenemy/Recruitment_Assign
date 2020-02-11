package com.githubrepo.data.network

import com.githubrepo.data.db.entities.Comments
import com.githubrepo.data.db.entities.Issue
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface MyApi {

    @GET("issues")
    suspend fun getIssues(): Response<List<Issue>>

    @GET("issues/{id}/comments")
    suspend fun getComments(@Path("id") id: String): Response<List<Comments>>


    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.github.com/repos/firebase/firebase-ios-sdk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}