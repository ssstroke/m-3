package com.example.firstlanaapplication

import android.app.Application
import com.example.firstlanaapplication.database.MainDB

class App : Application() {
    val database by lazy { MainDB.createDataBase(this) }
}