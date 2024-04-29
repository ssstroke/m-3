package com.example.firstlanaapplication.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.firstlanaapplication.entity.User

@Dao
interface Dao {
    @Insert
    suspend fun insertItem(user: User)
    @Update
    suspend fun updateItem(user: User)
    @Delete
    suspend fun deleteItem(user: User)

    @Query("SELECT * FROM user_table WHERE username = :usr")
    suspend fun getUserWithName(usr : String): List<User>

    @Query("SELECT * FROM user_table WHERE username = :usr AND password = :pass")
    suspend fun getUserWithNameAndPass(usr : String, pass : String): List<User>
}