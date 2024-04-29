package com.example.firstlanaapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firstlanaapplication.screens.MainScreen
import com.example.firstlanaapplication.screens.WeatherScreen

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(applicationContext, "Так, ну вроде работает", Toast.LENGTH_SHORT).show()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Routes.weatherScreen, builder = {
                composable(Routes.loginScreen) {
                    Row (
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        MainScreen(context = applicationContext, navController = navController)
                    }
                }
                composable(Routes.weatherScreen) {
                    WeatherScreen(applicationContext, this@MainActivity)
                }
            })
        }
    }
}

