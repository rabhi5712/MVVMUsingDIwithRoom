package com.example.mvvmroom.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmroom.repository.ApiRepository
import com.example.mvvmroom.utils.hitApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    private val dataMutable: MutableLiveData<JSONArray> = MutableLiveData()

    val getLiveData: LiveData<JSONArray>
        get() = dataMutable

    fun getData() = viewModelScope.launch(handler) {
        repository.getData().hitApi {
            dataMutable.postValue(it)
        }
    }
}