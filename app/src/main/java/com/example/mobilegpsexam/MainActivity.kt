package com.example.mobilegpsexam

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation() // 위치 권한 요청
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationText: TextView = findViewById(R.id.locationText)
        val locationButton: Button = findViewById(R.id.locationButton)
        locationButton.setOnClickListener {
            getLocation(locationText)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(textView: TextView) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    val testLocal = Location("testPoint")
                    testLocal.apply {
                        latitude = 35.0
                        longitude = 120.0
                    }
                    textView.text = "${location.distanceTo(testLocal)}M"
                }
            }
            .addOnFailureListener { fail ->
                textView.text = fail.localizedMessage
            }
    }

    private fun getAddress(lat: Double, lng: Double): List<Address>? {
        lateinit var address: List<Address>

        return try {
            val geocoder = Geocoder(this, Locale.KOREA)
            address = geocoder.getFromLocation(lat, lng, 1) as List<Address>
            address
        } catch (e: IOException) {
            Toast.makeText(this, "주소를 가져 올 수 없습니다", Toast.LENGTH_SHORT).show()
            null
        }
    }
}