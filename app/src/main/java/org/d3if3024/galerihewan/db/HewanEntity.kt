package org.d3if3024.galerihewan.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hewan")
data class HewanEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var tanggal: Long = System.currentTimeMillis(),
    var nama: String,
    var pengertian: String,
    var sumber: String,
    var img: String
)