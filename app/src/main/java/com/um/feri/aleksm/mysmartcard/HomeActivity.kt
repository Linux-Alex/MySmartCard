package com.um.feri.aleksm.mysmartcard

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.um.feri.aleksm.mysmartcard.databinding.FragmentHomeActivityBinding
import java.io.IOException

class HomeActivity : Fragment() {
    private var _binding: FragmentHomeActivityBinding? = null //we have fragment_my.xml layout
    lateinit var data:MySmartCard
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeActivityBinding.inflate(inflater, container, false)
        data = (activity as MainActivity).app.data
        binding?.lblUsername?.setText(data.firstname + " " + data.lastname)
        binding?.recyclerViewAllCards?.layoutManager = LinearLayoutManager(context)
        val adapter = CardAdapter(requireContext(), data, object:CardAdapter.CardOnClick {
            override fun onClick(p0: View?, position: Int) {
                //TODO("Not yet implemented")
                if(data.cards[position].cardNumber != "") {
                    displayBitmap(data.cards[position].cardNumber)
                }
                else {
                    Snackbar.make(binding!!.imgMainBarcode, "Invalid card code", Snackbar.LENGTH_LONG).show()
                }
            }
        },
        object:CardAdapter.CardOnLongClick {
            override fun onClick(p0: View?, position: Int) {
                //TODO("Not yet implemented")
            }
        })
        binding?.recyclerViewAllCards?.adapter = adapter
        binding!!.recyclerViewAllCards.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        return binding!!.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        Log.d("Card numbers", "Number of cards: " + data.cards.size)

        if(data.cards.size > 0)
            displayBitmap(data.cards[0].cardNumber)

    }
    override fun onDestroyView() { //because it can live also when it is not showen
        super.onDestroyView()
        _binding = null
    }

    private fun displayBitmap(value:String) {
        var barcodeImageView: ImageView = binding!!.imgMainBarcode;
        val imageContainerWidth = 200*2 // barcodeImageView.background.toBitmap().width
        val imageContainerHeight = 40*2 // barcodeImageView.background.toBitmap().height

        Log.i("dimensions", "width: " + imageContainerWidth + ", height: " + imageContainerHeight)

        barcodeImageView.setImageBitmap(
            createBarcodeBitmap(
                value, resources.getColor(R.color.black), resources.getColor(R.color.white), imageContainerWidth, imageContainerHeight
            )
        )
    }

    private fun createBarcodeBitmap(
        barcodeValue: String,
        @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int
    ): Bitmap {
        val bitMatrix = Code128Writer().encode(
            barcodeValue,
            BarcodeFormat.CODE_128,
            widthPixels,
            heightPixels
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap
    }

}