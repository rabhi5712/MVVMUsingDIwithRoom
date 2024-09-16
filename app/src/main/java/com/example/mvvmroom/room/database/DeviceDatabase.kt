package com.example.mvvmroom.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmroom.room.dao.DeviceDao
import com.example.mvvmroom.room.entity.LocalDeviceData

@Database(entities = [LocalDeviceData::class], version = 1, exportSchema = false)
abstract class DeviceDatabase : RoomDatabase(){
    abstract fun deviceDao() : DeviceDao
}