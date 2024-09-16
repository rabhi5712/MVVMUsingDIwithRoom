package com.example.mvvmroom.utils

import com.example.mvvmroom.BaseAppClass
import com.example.mvvmroom.MainActivity
import com.example.mvvmroom.room.entity.DeviceData
import com.example.mvvmroom.room.entity.LocalDeviceData
import com.google.gson.GsonBuilder
import org.json.JSONArray
import retrofit2.Response

fun Response<String?>?.hitApi(invokeOnCompletion: (JSONArray) -> Unit) {
    try {
        lateinit var jsonObject: JSONArray
        try {
            jsonObject = if (this!!.isSuccessful) {
                JSONArray(this.body().toString())
            } else {
                JSONArray(this.errorBody()?.string()!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            invokeOnCompletion.invoke(jsonObject)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun isConnected(): Boolean = BaseAppClass.instance?.networkMonitor?.state!!
