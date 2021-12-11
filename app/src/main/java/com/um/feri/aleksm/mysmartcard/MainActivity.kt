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
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.um.feri.aleksm.mysmartcard.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

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
                    openMainFragment(HomeActivity())
                }
                R.id.menu_add -> {
                    openMainFragment(AddActivity())
                }
                R.id.menu_search -> {
                    openMainFragment(SearchActivity())
                }
                R.id.menu_settings -> {
                    openMainFragment(SettingsActivity())
                }
            }
        }

        if(savedInstanceState == null) {
            openMainFragment(HomeActivity())
        }
    }

    private fun openMainFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentMainWindow, fragment)
        transaction.commit()
    }


}