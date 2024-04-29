package com.example.firstlanaapplication.screens

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.firstlanaapplication.MainActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import org.json.JSONException
import org.json.JSONObject
import java.util.Date

private const val apiKey = "36c3ae034901faa7c467ec25f1b76b13"
private var latitude : String = ""
private var longitude : String = ""
private const val url = "https://api.openweathermap.org/data/2.5/weather?"

private var location by mutableStateOf("")

private var temperature by mutableStateOf("")
private var windSpeed by mutableStateOf("")
private var sunset by mutableStateOf("")

private var path by mutableStateOf("https://api.openweathermap.org/img/w/03n.png")

private var locationRequest = LocationRequest();


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherScreen(
    context : Context,
    mainActivity: MainActivity
) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Погода в вашем городе!",
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                modifier = Modifier
                    .fillMaxWidth())

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Введите город") },
                    keyboardOptions = KeyboardOptions.Default,
                    keyboardActions = KeyboardActions(onDone = { /* do something on Done */ })
                )

                // Погода в городе
                Button(onClick = {
                    getCurrentLocation(context, mainActivity)

                    val tempUrl : String = if (location != "") {
                        url + "q=" + location + "&appid=" + apiKey
                    } else {
                        url + "lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey
                    }
                    val stringRequest = StringRequest(
                        Request.Method.POST,
                        tempUrl,
                        { response ->
                            try {
                                val jsonObject = JSONObject(response)

                                val icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon")
                                val temperatureValue = jsonObject.getJSONObject("main").getDouble("temp") - 273.15
                                val speedValue = jsonObject.getJSONObject("wind").getDouble("speed")
                                val sunsetValue = Date(
                                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000)

                                path = "https://openweathermap.org/img/w/$icon.png"


                                temperature = String.format("%.3f", temperatureValue) + " C"
                                windSpeed = speedValue.toString()
                                sunset = sunsetValue.toString()

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        },
                        { _ ->
                            Toast.makeText(context, "Ошибка! Мы не располагаем погодными данными по введённому городу.", Toast.LENGTH_SHORT).show()
                        }
                    )
                    val requestQueue = Volley.newRequestQueue(context)
                    requestQueue.add(stringRequest) }) {
                    Text("Узнать погоду")
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column (
                    horizontalAlignment = Alignment.Start
                ) {
                    GlideImage(model = path, contentDescription = "loadImage",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp),)

                    Text(
                        "Температура: $temperature",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal
                        )
                    )

                    Text(
                        "Скорость ветра: $windSpeed",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal
                        ))

                    Text(
                        "Закат: $sunset",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal
                        ))
                }
            }
        }
    }
}

private fun getCurrentLocation(context: Context, mainActivity: MainActivity) {
    locationRequest = LocationRequest.create()
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    locationRequest.setInterval(5000);
    locationRequest.setFastestInterval(2000);

    if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
        turnOnGPS(mainActivity)
        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(mainActivity)
                        .removeLocationUpdates(this)
                    if (locationResult.locations.isNotEmpty()) {
                        val index = locationResult.locations.size - 1
                        latitude = locationResult.locations[index].latitude.toString()
                        longitude = locationResult.locations[index].longitude.toString()
                    }
                }
            }, Looper.getMainLooper())
    } else {
        requestPermissions(mainActivity, arrayOf(ACCESS_FINE_LOCATION), 1)
    }
}

private fun turnOnGPS(mainActivity: MainActivity) {
    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
    builder.setAlwaysShow(true)
    val result = LocationServices.getSettingsClient(mainActivity)
        .checkLocationSettings(builder.build())
    result.addOnCompleteListener { task ->
        try {
            task.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            when (e.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    val resolvableApiException = e as ResolvableApiException
                    resolvableApiException.startResolutionForResult(mainActivity, 2)
                } catch (ex: IntentSender.SendIntentException) {
                    ex.printStackTrace()
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    // Device does not have location
                }
            }
        }
    }
}
