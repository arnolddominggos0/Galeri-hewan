package org.d3if3024.galerihewan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3024.galerihewan.db.HewanDao
import org.d3if3024.galerihewan.db.HewanEntity
import org.d3if3024.galerihewan.model.Hewan

class HomeViewModel(private val db: HewanDao) : ViewModel() {

    private val hasilHewan = MutableLiveData<Hewan?>()

    fun hasilInput(nama: String, latin: String, img: Int) {
        hasilHewan.value = Hewan(nama, latin, img)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val dataHewan = HewanEntity(
                    nama = nama,
                    latin = latin,
                    img = img
                )
                db.insert(dataHewan)
            }
        }
    }

    fun getHasilHewan(): LiveData<Hewan?> = hasilHewan
}