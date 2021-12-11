package com.um.feri.aleksm.mysmartcard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.um.feri.aleksm.mysmartcard.databinding.FragmentSearchActivityBinding

class SearchActivity : Fragment() {
    private var _binding: FragmentSearchActivityBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchActivityBinding.inflate(inflater, container, false)
        return binding!!.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroyView() { //because it can live also when it is not showen
        super.onDestroyView()
        _binding = null
    }
}