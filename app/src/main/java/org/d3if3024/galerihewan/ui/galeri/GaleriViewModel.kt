package org.d3if3024.galerihewan.ui.galeri

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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
                val hewanList = parseJsonToHewanList(result)
                data.postValue(hewanList)
                Log.d("GaleriViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("GaleriViewModel", "Failure: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun parseJsonToHewanList(response: String): List<Hewan> {
        val gson = Gson()
        return gson.fromJson(response, Array<Hewan>::class.java).toList()
    }

    fun getData(): LiveData<List<Hewan>> = data
}
