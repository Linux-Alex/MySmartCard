package com.um.feri.aleksm.mysmartcard

import android.content.ClipData
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.um.feri.aleksm.mysmartcard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
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

        //findViewById<TextView>(R.id.lblUsername).setText(app.data.firstname + " " + app.data.lastname)
    }

    private fun openMainFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentMainWindow, fragment)
        transaction.commit()
    }

    fun btnAddCardSubmit(view: android.view.View) {
        if(findViewById<EditText>(R.id.txtCardNumber).text.toString() == "") {
            Snackbar.make(view, "Enter a card number", Snackbar.LENGTH_LONG).show()
        }
        else {
            app.data.cards.add(Card(
                findViewById<EditText>(R.id.txtCardNumber).text.toString(),
                findViewById<Spinner>(R.id.spinnerShop).selectedItem.toString(),
                findViewById<EditText>(R.id.txtOwner).text.toString()
            ));
            app.saveToFile()
            Snackbar.make(view, "You added a card", Snackbar.LENGTH_LONG).show()
            btnAddCardReset(view)
            Log.d("Card status", "Successfully added one more card.")
        }
    }

    fun btnAddCardReset(view: android.view.View) {
        findViewById<EditText>(R.id.txtCardNumber).setText("")
        findViewById<Spinner>(R.id.spinnerShop).setSelection(0)
        findViewById<EditText>(R.id.txtOwner).setText("")
    }

    fun settingsSave(view: android.view.View) {
        app.data.firstname = findViewById<EditText>(R.id.txtSettingsFirstname).text.toString()
        app.data.lastname = findViewById<EditText>(R.id.txtSettingsLastname).text.toString()
        if(findViewById<RadioButton>(R.id.rbtnSettingsGenderMale).isChecked)
            app.data.gender = Gender.Male
        else if(findViewById<RadioButton>(R.id.rbtnSettingsGenderFemale).isChecked)
            app.data.gender = Gender.Female
        app.saveToFile()
    }


}