package com.example.mvvmroom.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_table")
data class LocalDeviceData(
    @PrimaryKey val id: String,
    val name: String,
    @Embedded val data: DeviceData?
)

data class DeviceData(
    val color: String? = null,
    val capacity: String? = null,
    val capacityGB: Int? = null,
    val price: Double? = null,
    val year: Int? = null,
    val cpuModel: String? = null,
    val hardDiskSize: String? = null,
    val strapColour: String? = null,
    val caseSize: String? = null,
    val description: String? = null,
    val screenSize: Double? = null,
    val generation: String? = null
)
