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

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = HewanApi.service.getHewan()
                data.postValue(result)
                Log.d("GaleriViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("GaleriViewModel", "Failure: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun getData(): LiveData<List<Hewan>> = data
}
