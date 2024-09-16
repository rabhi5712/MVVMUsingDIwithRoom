package com.example.mvvmroom.interfaces

import com.example.mvvmroom.utils.Apis
import retrofit2.Response
import retrofit2.http.GET


interface ApiInterface {
    @GET(Apis.GET_DATA)
    suspend fun GET_DATA(): Response<String?>?
}