package com.um.feri.aleksm.mysmartcard

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
//import android.R




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide();

        displayBitmap("978107189178")
        //findViewById<ImageView>(R.id.imgMainBarcode).
    }

    private fun displayBitmap(value:String) {
        var barcodeImageView: ImageView = findViewById<ImageView>(R.id.imgMainBarcode);
        val imageContainerWidth = 200*2 // barcodeImageView.background.toBitmap().width
        val imageContainerHeight = 40*2 // barcodeImageView.background.toBitmap().height

        Log.i("dimensions", "width: " + imageContainerWidth + ", height: " + imageContainerHeight)

        barcodeImageView.setImageBitmap(
            createBarcodeBitmap(
                value, getColor(R.color.black), getColor(R.color.white), imageContainerWidth, imageContainerHeight
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