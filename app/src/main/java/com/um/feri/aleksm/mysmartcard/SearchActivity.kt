package com.um.feri.aleksm.mysmartcard

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat


class SearchActivity(var currentLocation: Location) : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentSearchActivityBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    lateinit var mapController: MapController
    lateinit var startLocation: IGeoPoint
    lateinit var mapFragment: SupportMapFragment
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchActivityBinding.inflate(inflater, container, false)
        return binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment


        mapFragment.getMapAsync(this)
    }

    override fun onDestroyView() { //because it can live also when it is not showen
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title(R.string.myCurrentLocation.toString())
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17F))
        googleMap.addMarker(markerOptions)
    }
}