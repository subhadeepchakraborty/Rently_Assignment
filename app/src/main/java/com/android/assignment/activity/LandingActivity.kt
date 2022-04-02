package com.android.assignment.activity

import android.R.attr.delay
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.assignment.R
import com.android.assignment.databinding.ActivityMainBinding
import com.android.assignment.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar


class LandingActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var KochiLatLong: LatLng
    private lateinit var CoimbatoreLatLong: LatLng
    private lateinit var MaduraiLatLong: LatLng
    private lateinit var MunnarLatLong: LatLng
    private lateinit var googleMap: GoogleMap
    private var latLongList: List<LatLng>? =null
    private var locationTitleList: List<String>? =null
    private lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread
    private val delayInMillis = 3000L
    private lateinit var runnable: Runnable
    var options:PolylineOptions ? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Handler
        handlerThread= HandlerThread("Map Handling Background Handler")
        handlerThread.start()
        handler= Handler(handlerThread.looper)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_google_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        // Check For Connection
        if(!Constants.checkForIntenetConnection(this)){
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_SHORT).show()
            finish()
        }

        Snackbar.make(binding.root,"Please Hold.While we are Loading the map...",Snackbar.LENGTH_LONG).show()

        binding.fab.setOnClickListener(this)

    }

    // Initialize LatLong Variables
    private  fun handleLatLonValue() {
        KochiLatLong=LatLng(Constants.KOCHI_LAT.toDouble(),Constants.KOCHI_LONG.toDouble())
        CoimbatoreLatLong=LatLng(Constants.COIMBATORE_LAT.toDouble(),Constants.COIMBATORE_LONG.toDouble())
        MaduraiLatLong=LatLng(Constants.MADURAI_LAT.toDouble(),Constants.MADURAI_LONG.toDouble())
        MunnarLatLong=LatLng(Constants.MUNNAR_LAT.toDouble(),Constants.MUNNAR_LONG.toDouble())

        latLongList= listOf<LatLng>(KochiLatLong,CoimbatoreLatLong,MaduraiLatLong,MunnarLatLong,KochiLatLong)
        locationTitleList =  listOf<String>("Kochi","Coimbatore","Madurai","Munnar","Kochi")
        options = PolylineOptions().width(10f).color(Color.BLUE).geodesic(true)
    }


    override fun onMapReady(map: GoogleMap?) {
        handleLatLonValue()
        googleMap = map!!
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.setAllGesturesEnabled(true)
        handleMapHandler()

    }

    // Handle Navigation
    private fun handleMapHandler() {
        var i = 0
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable,delayInMillis)
            if(i == latLongList!!.size){
                options = PolylineOptions().width(10f).color(Color.BLUE).geodesic(true)
                resetMap()
                i=0
            }
            Log.d("subhadeep,",i.toString())
            val point: LatLng = latLongList!!.get(i)
            handlerMapNavigation(googleMap,point,locationTitleList!!.get(i).toString())
            i+=1

        }.also { runnable = it }, delayInMillis)


    }

    //Clear Map
    fun resetMap(){
        runOnUiThread {
        googleMap.clear()
        }
    }

    // Customization to Map
    private fun handlerMapNavigation(googleMap: GoogleMap, point: LatLng, titleText: String) {

        options!!.add(point)
        runOnUiThread {
        googleMap.addMarker(MarkerOptions().position(point).title(titleText))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,9f))
        googleMap.addPolyline(options)
        }
    }


    // Handle Elements Listeners
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.fab ->{
                         Snackbar.make(binding.root, "Pointing to Starting point", Snackbar.LENGTH_LONG).show()
                         handler.removeCallbacksAndMessages(null);
                         options = PolylineOptions().width(10f).color(Color.BLUE).geodesic(true)
                         resetMap()
                         handleMapHandler()
                       }
        }
    }


}