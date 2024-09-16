package com.example.mvvmroom.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmroom.room.entity.LocalDeviceData

@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(device: List<LocalDeviceData>)

    @Query("SELECT * FROM device_table")
    suspend fun getAllDevices(): List<LocalDeviceData>

    @Query("DELETE FROM device_table")
    suspend fun clearDevices()
}