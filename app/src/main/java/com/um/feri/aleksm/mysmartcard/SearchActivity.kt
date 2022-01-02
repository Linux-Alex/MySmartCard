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


class SearchActivity : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentSearchActivityBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    lateinit var mapController: MapController
    lateinit var startLocation: IGeoPoint
    lateinit var mapFragment: SupportMapFragment
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var currentLocation: Location
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        findCurrentLocation()

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Location permissions are enabled", Toast.LENGTH_LONG);
            Log.i("MAP", "Location permissions are enabled")
        }
        else {
            Toast.makeText(context, "Location permissions are disabled", Toast.LENGTH_LONG);
            Log.i("MAP", "Location permissions are disabled")
            //ActivityCompat.requestPermissions(Activity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }

//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermissions(
//                requireActivity(), arrayOf(
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ),
//                1
//            )
//        } else {
//            Log.e("DB", "PERMISSION GRANTED")
//        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
            // Got last known location. In some rare situations this can be null.
            Log.i("MAP", "I got your last location: " + location?.latitude + ", " + location?.longitude)
            currentLocation = location!!
        }.addOnFailureListener {
            Log.i("MAP", "I don't have your last location. ERROR: " + it.toString())
        }.addOnCanceledListener {
            Log.i("MAP", "Location listening was canceled")
        }

        Log.i("MAP", "Do tukaj pride vredu 2. Lokacija je: " + currentLocation.latitude)

        mapFragment.getMapAsync(this)

        //findCurrentLocation()

//        binding?.map?.setTileSource(TileSourceFactory.MAPNIK)
//        binding?.map?.setMultiTouchControls(true)
//        mapController = binding?.map?.controller as MapController
//        mapController.setZoom(17.0)
//        mapController.setCenter(GeoPoint(46.5680205, 16.052918))
    }

    override fun onDestroyView() { //because it can live also when it is not showen
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())
        val markerOptions = MarkerOptions().position(latLng).title("Moja lokacija")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap.addMarker(markerOptions)
    }


    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            Log.i("MAP", "Imate že dovoljenje")
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }
}