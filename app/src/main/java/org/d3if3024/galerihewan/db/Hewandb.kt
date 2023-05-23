package org.d3if3024.galerihewan.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HewanEntity::class], version = 1, exportSchema = false)
abstract class Hewandb : RoomDatabase() {

    abstract val dao: HewanDao

    companion object {
        @Volatile
        private var INSTANCE: Hewandb? = null
        fun getInstance(context: Context): Hewandb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance =
                        Room.databaseBuilder(context.applicationContext, Hewandb::class.java, "hewan.db")
                            .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}