package com.example.basicandroidassignment.data

import com.example.basicandroidassignment.models.VideoModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.QueryMap

private const val BASE_URL = "https://aipdbexnshbsexlzqhvt.supabase.co/rest/v1/"
private const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFpcGRiZXhuc2hic2V4bHpxaHZ0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTUxNTI3MDIsImV4cCI6MjAzMDcyODcwMn0.wxuXiefK_ct7NIFkCq1hzNguas52KeOQ5KyxCxVBRTE"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .build()

interface NetworkDataService {
    @Headers("apikey: $API_KEY")
    @GET("Videos")
    suspend fun getVideos(@QueryMap options: MutableMap<String,Any>): List<VideoModel>
}

object NetworkDataApi {
    val networkDataService: NetworkDataService by lazy {
        retrofit.create(NetworkDataService::class.java)
    }
}