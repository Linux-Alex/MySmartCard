package com.um.feri.aleksm.mysmartcard

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import com.um.feri.aleksm.mysmartcard.databinding.FragmentAddActivityBinding
import com.um.feri.aleksm.mysmartcard.databinding.FragmentHomeActivityBinding


class AddActivity(var app:MyApplication, var position:Int) : Fragment() {
    private var _binding: FragmentAddActivityBinding? = null
    //lateinit var data:MySmartCard
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)

        if(position != -1) {
            binding?.txtCardNumber?.setText(app.data.cards[position].cardNumber)
            binding?.txtOwner?.setText(app.data.cards[position].owner)

            val shopList: Array<String> = context?.getResources()!!.getStringArray(R.array.shop)
            binding?.spinnerShop?.setSelection(shopList.indexOf(app.data.cards[position].shop))

            binding?.btnAddCard?.setText(R.string.edit)
            binding?.btnReset?.setText(R.string.delete)
            binding?.txtCardNumber?.isEnabled = false
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