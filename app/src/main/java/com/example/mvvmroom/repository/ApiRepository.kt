package com.example.mvvmroom.repository

import com.example.mvvmroom.interfaces.ApiInterface


class ApiRepository(private val apiInterface: ApiInterface) {
    suspend fun getData() = apiInterface.GET_DATA()
}