package com.um.feri.aleksm.mysmartcard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ColorSpace
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.um.feri.aleksm.mysmartcard.databinding.FragmentSearchActivityBinding
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.MapController
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.requireViewById
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.app.NotificationManager
import android.content.Context


class SearchActivity(var app:MyApplication) : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentSearchActivityBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    lateinit var mapFragment: SupportMapFragment
    lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentLocation: Location? = app.data.latestLocation

    // Map Request
    lateinit var connection : HttpURLConnection
    lateinit var reader: BufferedReader
    
    // JSON Reader
    val client = OkHttpClient()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchActivityBinding.inflate(inflater, container, false)
        return binding!!.root
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //app = application as MyApplication
        /*var mainActivity = activity as MainActivity
        mainActivity.openMainFragment(HomeActivity(app))*/
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        //val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)


        requireLocationPermission()
        getLocation()

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onDestroyView() { //because it can live also when it is not showen
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title(R.string.myCurrentLocation.toString())
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17F))
        googleMap.addMarker(markerOptions)

        /*if(currentLocation!!.latitude != null && currentLocation!!.longitude != null) {
            val shopRequestUrl: String =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?type=supermarket&location=" + currentLocation!!.latitude + "," + currentLocation!!.longitude + "&radius=5000&key=AIzaSyCehGSFc6QlrESQsjDV-UCfhmqUa-1ukdQ"
            val url = URL(shopRequestUrl)
            connection = url.openConnection() as HttpURLConnection
            Log.i("URL CONNECTION", connection.toString())
            connection.connect()
            val stream: InputStream = connection.inputStream
            reader = BufferedReader(InputStreamReader(stream))
            var results: String = reader.readLines().toString()

            val read = JSONObject(results)
            Log.i("READ MAP", read.toString())
        }*/


        run("https://maps.googleapis.com/maps/api/place/nearbysearch/json?type=supermarket&location=" + currentLocation!!.latitude + "," + currentLocation!!.longitude + "&radius=5000&key=AIzaSyCehGSFc6QlrESQsjDV-UCfhmqUa-1ukdQ")
    }

    fun run(url:String) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("MAP SEARCH", "Ne dela")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                var str_response = response.body()!!.string()
                //creating json object  
                val json_contact:JSONObject = JSONObject(str_response)
                //creating json array  
                var jsonarray_info: JSONArray = json_contact.getJSONArray("results")
                var size:Int = jsonarray_info.length()
                Log.i("MAP", jsonarray_info.toString())
                val shopList: Array<String> = context?.getResources()!!.getStringArray(R.array.shop)
                var nearestShop:String = ""
                var nearestShopDistance:Double = Double.MAX_VALUE
                for(i in 0.. size-1) {
                    var checkIfIsStore:Boolean = false
                    for(k in 0 .. shopList.size-1) {
                        if(jsonarray_info.getJSONObject(i).getString("name").contains(shopList[k])) {
                            checkIfIsStore = true
                            Log.i("FOUND STORE", jsonarray_info.getJSONObject(i).getString("name"))
                            Log.i("FOUND STORE", jsonarray_info.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat"))
                            var calculatedDistance = distance(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude,
                                jsonarray_info.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat").toDouble(),
                                jsonarray_info.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat").toDouble())
                            if(calculatedDistance < nearestShopDistance) {
                                nearestShopDistance = calculatedDistance
                                nearestShop = shopList[k]
                            }
                        }
                    }
                }
                Snackbar.make(binding?.lblSearchTitle!!, "Najbližnja trgovina: " + nearestShop, Snackbar.LENGTH_LONG).show()

                /*var builder = NotificationCompat.Builder(requireContext(), "CHANNEL_ID")
                    .setSmallIcon(R.drawable.ic_small_icon)
                    .setContentTitle(R.string.foundNearestShop.toString())
                    .setContentText("Najbližnja trgovina: " + nearestShop)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                val channel = NotificationChannel("CHANNEL_ID", "Found store", NotificationManager.IMPORTANCE_DEFAULT)
                channel.enableVibration(true)
                notificationManager.createNotificationChannel(channel)
                builder.setChannelId("CHANNEL_ID")
                val notification = builder.build()
                notificationManager.notify(1, notification)*/
                //var mainActivity = activity as MainActivity
                //mainActivity.createNotificationChannel("Najbližnja trgovina: " + nearestShop, R.string.foundNearestShop.toString())
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requireLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.i("MAP", "Precise location access granted.")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.i("MAP", "Only approximate location access granted.")
                } else -> {
                Log.i("MAP", "No location access granted.")
                }
            }
        }

        // ...

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            Log.i("MAP", "Last location readed from fragment: " + location?.latitude + ", " + location?.longitude)
            if (location != null) {
                app.data.latestLocation = location
            }
        }
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}