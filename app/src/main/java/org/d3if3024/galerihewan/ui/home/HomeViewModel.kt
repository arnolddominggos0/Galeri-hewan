package org.d3if3024.galerihewan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if3024.galerihewan.model.Hewan

class HomeViewModel() : ViewModel() {

    private val hasilHewan = MutableLiveData<Hewan?>()

    fun hasilInput(nama: String, latin: String, img: Int) {
        hasilHewan.value = Hewan(nama, latin, img)
    }

    fun getHasilHewan(): LiveData<Hewan?> = hasilHewan
}