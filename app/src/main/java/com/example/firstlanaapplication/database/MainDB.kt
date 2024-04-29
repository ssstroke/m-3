package com.example.firstlanaapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.firstlanaapplication.dao.Dao
import com.example.firstlanaapplication.entity.User

@Database(
    entities = [
        User::class
    ],
    version = 1
)
abstract class MainDB : RoomDatabase() {
    abstract val dao: Dao
    companion object {
        fun createDataBase(context: Context): MainDB {
            return Room.databaseBuilder(
                context,
                MainDB::class.java,
                "user_database.db"
            ).build()
        }
    }
}