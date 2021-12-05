package com.um.feri.aleksm.mysmartcard

import android.content.ClipData
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.view.children
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.um.feri.aleksm.mysmartcard.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val homeFragment = HomeActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        supportActionBar?.hide()
        findViewById<ChipNavigationBar>(R.id.navMain).setItemSelected(R.id.menu_home)

        binding.navMain.setOnItemSelectedListener{
            when(it) {
                R.id.menu_home -> {
                    openMainFragment()
                }
                R.id.menu_add -> {

                }
                R.id.menu_search -> {

                }
                R.id.menu_settings -> {

                }
            }
        }

        if(savedInstanceState == null) {
            openMainFragment()
        }
    }

    private fun openMainFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentMainWindow, homeFragment)
        transaction.commit()
    }


}