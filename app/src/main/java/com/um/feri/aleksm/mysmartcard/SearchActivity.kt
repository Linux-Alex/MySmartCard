package com.um.feri.aleksm.mysmartcard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.um.feri.aleksm.mysmartcard.databinding.FragmentSearchActivityBinding
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController

class SearchActivity : Fragment() {
    private var _binding: FragmentSearchActivityBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    lateinit var mapController: MapController
    lateinit var startLocation: IGeoPoint
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchActivityBinding.inflate(inflater, container, false)
        return binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.map?.setTileSource(TileSourceFactory.MAPNIK)
        binding?.map?.setMultiTouchControls(true)
        mapController = binding?.map?.controller as MapController
        mapController.setZoom(9.5)
        mapController.setCenter(GeoPoint(48.8583, 2.2944))
    }
    override fun onDestroyView() { //because it can live also when it is not showen
        super.onDestroyView()
        _binding = null
    }
}