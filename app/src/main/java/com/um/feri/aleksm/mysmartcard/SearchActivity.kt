package com.um.feri.aleksm.mysmartcard

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.um.feri.aleksm.mysmartcard.databinding.FragmentSearchActivityBinding
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.MapController

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
}