package com.example.mvvmroom

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmroom.adapters.DeviceDataAdapter
import com.example.mvvmroom.databinding.ActivityMainBinding
import com.example.mvvmroom.interfaces.NetworkStateListener
import com.example.mvvmroom.room.entity.DeviceData
import com.example.mvvmroom.room.entity.LocalDeviceData
import com.example.mvvmroom.utils.DeviceDataDeserializer
import com.example.mvvmroom.utils.isConnected
import com.example.mvvmroom.viewmodels.DataViewModel
import com.example.mvvmroom.viewmodels.DeviceViewModel
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),NetworkStateListener {
    private lateinit var binding: ActivityMainBinding
    val dataViewModel : DataViewModel by viewModels()
    val deviceViewModel: DeviceViewModel by viewModels()
    val deviceList : ArrayList<LocalDeviceData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val baseAppInstance = BaseAppClass.instance
        baseAppInstance?.setNetworkStateListener(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        binding.progressBar.isVisible = true
        deviceViewModel.getAllDevices()
        deviceViewModel.localLiveData.observe(this@MainActivity) { jsonArray ->
            if (jsonArray.isNotEmpty()){
                binding.progressBar.isVisible = false
                deviceList.clear()
                deviceList.addAll(jsonArray)
                setView()
            } else {
                if (isConnected()) {
                    dataViewModel.getData()
                } else {
                    dialogBuilder()
                }
            }
        }
        dataViewModel.getLiveData.observe(this@MainActivity) { jsonArray ->
            if (jsonArray.length() > 0){
                binding.progressBar.isVisible = false
                val job = getDeviceData(jsonArray)
                job.invokeOnCompletion {
                    deviceViewModel.insertDevices(deviceList)
                }
            } else {
                binding.progressBar.isVisible = false
                binding.retryTv.isVisible = true
            }
        }
    }

    private fun setListeners() = binding.apply {
            retryTv.setOnClickListener {
                if (isConnected()) {
                    retryTv.isVisible = false
                    dataViewModel.getData()
                } else {
                    dialogBuilder()
                }
            }
    }

    fun setView()  {
        binding.progressBar.isVisible = false
        binding.retryTv.isVisible = false
        binding.deviceRecyclerView.isVisible = true
        binding.deviceRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = DeviceDataAdapter(this@MainActivity, deviceList)
        }
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
            if (isConnected){
                if (deviceList.isEmpty()) {
                    deviceViewModel.getAllDevices()
                }
            } else {
                if (deviceList.isEmpty()){
                    dialogBuilder()
                }
            }
    }

    fun getDeviceData(jsonArray: JSONArray) = CoroutineScope(Dispatchers.IO).launch{
        deviceList.clear()
        val gson = GsonBuilder()
            .registerTypeAdapter(DeviceData::class.java, DeviceDataDeserializer())
            .create()


            for (i in 0..<jsonArray.length()) {
                val device = gson.fromJson(jsonArray.opt(i).toString(), LocalDeviceData::class.java)
                deviceList.add(device)
            }
        withContext(Dispatchers.Main) {
            setView()
        }
    }

    private fun dialogBuilder(){
        val builder: AlertDialog.Builder = Builder(this@MainActivity)
        builder.setMessage("No internet connection")
        builder.setTitle("Alert !")
        builder.setCancelable(false)
        builder.setPositiveButton("Ok"
        ) { dialog: DialogInterface?, which: Int ->
            binding.progressBar.isVisible = false
            binding.retryTv.isVisible = true
            dialog?.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}