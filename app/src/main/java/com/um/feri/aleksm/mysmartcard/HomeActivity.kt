package com.um.feri.aleksm.mysmartcard

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat.getColor
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.um.feri.aleksm.mysmartcard.databinding.FragmentHomeActivityBinding

class HomeActivity : Fragment() {
    private var _binding: FragmentHomeActivityBinding? = null //we have fragment_my.xml layout
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeActivityBinding.inflate(inflater, container, false)
        return binding!!.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.buttonFirst.setOnClickListener {
            Timber.d(“Do some action here”);
        }*/

        displayBitmap("978107189178")
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