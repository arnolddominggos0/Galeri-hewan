package org.d3if3024.galerihewan.ui.galeri

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3024.galerihewan.model.Hewan
import org.d3if3024.galerihewan.network.HewanApi

class GaleriViewModel : ViewModel() {

    private val data = MutableLiveData<List<Hewan>>()
    private val status = MutableLiveData<HewanApi.ApiStatus>()

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(HewanApi.ApiStatus.LOADING)
            try {
                val result = HewanApi.service.getHewan()
                status.postValue(HewanApi.ApiStatus.SUCCESS)
                data.postValue(result)
                Log.d("GaleriViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("GaleriViewModel", "Failure: ${e.message}")
                status.postValue(HewanApi.ApiStatus.FAILED)
            }
        }
    }

    fun getData(): LiveData<List<Hewan>> = data
    fun getStatus(): LiveData<HewanApi.ApiStatus> = status
}
