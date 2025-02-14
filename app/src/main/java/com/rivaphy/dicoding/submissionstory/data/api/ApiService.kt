package com.rivaphy.dicoding.submissionstory.data.api

import com.rivaphy.dicoding.submissionstory.data.api.response.ListResponse
import com.rivaphy.dicoding.submissionstory.data.api.response.LoginResponse
import com.rivaphy.dicoding.submissionstory.data.api.response.RegisterResponse
import com.rivaphy.dicoding.submissionstory.data.api.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("/v1/register")
     suspend fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ListResponse

    @Multipart
    @POST("stories")
    suspend fun postUserStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?
    ): StoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): ListResponse
}