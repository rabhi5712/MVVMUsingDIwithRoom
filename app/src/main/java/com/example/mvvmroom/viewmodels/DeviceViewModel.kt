package com.example.mvvmroom.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmroom.repository.DeviceRepository
import com.example.mvvmroom.room.entity.LocalDeviceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel() {

    fun insertDevices(devices: List<LocalDeviceData>) {
        viewModelScope.launch {
            try {
                repository.insertDevices(devices)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllDevices() {
        viewModelScope.launch {
            try {
                val devices = repository.getAllDevices()
                localDeviceData.postValue(devices)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val localDeviceData = MutableLiveData<List<LocalDeviceData>>()
    val localLiveData: LiveData<List<LocalDeviceData>> get() = localDeviceData
}
