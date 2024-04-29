package com.example.firstlanaapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val username: String,
    val password: String
)