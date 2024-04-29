package com.example.firstlanaapplication.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.example.firstlanaapplication.App
import com.example.firstlanaapplication.Routes
import com.example.firstlanaapplication.database.MainDB
import com.example.firstlanaapplication.entity.User
import kotlinx.coroutines.launch

class MainViewModel(private val database: MainDB) : ViewModel() {
    val loginText = mutableStateOf("")
    val loginPassword = mutableStateOf("")
    val registrationText = mutableStateOf("")
    val registrationPass = mutableStateOf("")
    val repeatedPass = mutableStateOf("")

    @SuppressLint("ShowToast")
    fun insertItem(context : Context) = viewModelScope.launch {
        if (registrationText.value.isEmpty()) {
            Toast.makeText(context, "Неверный формат ввода данных", Toast.LENGTH_LONG).show()
        } else if (registrationPass.value != repeatedPass.value) {
            Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_LONG).show()
        } else if (database.dao.getUserWithName(registrationText.value).isNotEmpty()) {
            Toast.makeText(context, "Пользователь с таким именем уже существует", Toast.LENGTH_LONG).show()
        } else {
            val user = User(username = registrationText.value, password = registrationPass.value)
            database.dao.insertItem(user)
            Toast.makeText(context, "Регистрация завершена успешно!", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("ShowToast", "CoroutineCreationDuringComposition")
    fun loginUser(context: Context, navController: NavController) = viewModelScope.launch {
        if (database.dao.getUserWithNameAndPass(loginText.value, loginPassword.value).isEmpty()) {
            Toast.makeText(context, "Неправильное имя пользователя или пароль", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Харош", Toast.LENGTH_LONG).show()
            navController.navigate(Routes.weatherScreen)
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                return MainViewModel(database) as T
            }
        }
    }
}