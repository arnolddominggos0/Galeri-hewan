package org.d3if3024.galerihewan.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HewanDao {
    @Insert
    fun insert(hewan: HewanEntity)

    @Query("SELECT * FROM hewan ORDER BY id DESC")
    fun getLastBmi(): LiveData<List<HewanEntity?>>

    @Query("DELETE FROM hewan")
    fun clearData()
}