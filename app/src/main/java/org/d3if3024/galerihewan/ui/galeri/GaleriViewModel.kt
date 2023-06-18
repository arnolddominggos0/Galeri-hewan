package org.d3if3024.galerihewan.ui.galeri

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3024.galerihewan.network.HewanApi
import org.d3if3024.galerihewan.network.UpdateWorker
import java.util.concurrent.TimeUnit

class GaleriViewModel : ViewModel() {

    private val data = MutableLiveData<List<org.d3if3024.galerihewan.model.HewanApiJson>>()
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

    fun getData(): LiveData<List<org.d3if3024.galerihewan.model.HewanApiJson>> = data
    fun getStatus(): LiveData<HewanApi.ApiStatus> = status

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

}
