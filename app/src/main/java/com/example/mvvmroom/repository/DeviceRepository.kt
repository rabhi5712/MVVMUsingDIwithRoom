package com.example.mvvmroom.repository

import com.example.mvvmroom.room.dao.DeviceDao
import com.example.mvvmroom.room.entity.LocalDeviceData
import javax.inject.Inject

class DeviceRepository @Inject constructor(private val deviceDao: DeviceDao) {

    suspend fun insertDevices(devices: List<LocalDeviceData>) {
        deviceDao.insertDevices(devices)
    }

    suspend fun getAllDevices(): List<LocalDeviceData> {
        return deviceDao.getAllDevices()
    }

    suspend fun clearDevices() {
        deviceDao.clearDevices()
    }
}
