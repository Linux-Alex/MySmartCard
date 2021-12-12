package com.um.feri.aleksm.mysmartcard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.um.feri.aleksm.mysmartcard.databinding.FragmentSettingsActivityBinding

class SettingsActivity : Fragment() {
    private var _binding: FragmentSettingsActivityBinding? = null
    lateinit var data:MySmartCard
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsActivityBinding.inflate(inflater, container, false)
        data = (activity as MainActivity).app.data

        binding?.txtSettingsFirstname?.setText(data.firstname)
        binding?.txtSettingsLastname?.setText(data.lastname)
        if(data.gender == Gender.Male) {
            binding?.rbtnSettingsGenderMale?.isChecked = true
        }
        else if(data.gender == Gender.Female) {
            binding?.rbtnSettingsGenderFemale?.isChecked = true
        }

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
