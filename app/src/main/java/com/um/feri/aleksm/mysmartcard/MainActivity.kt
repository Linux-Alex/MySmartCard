package com.um.feri.aleksm.mysmartcard

import android.Manifest
import android.content.ClipData
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.um.feri.aleksm.mysmartcard.databinding.ActivityMainBinding
import org.osmdroid.config.Configuration

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

        // OSM maps
        Configuration.getInstance().load(applicationContext, this.getPreferences(Context.MODE_PRIVATE))

        // Permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)

        binding.navMain.setOnItemSelectedListener{
            when(it) {
                R.id.menu_home -> {
                    openMainFragment(HomeActivity(app))
                }
                R.id.menu_add -> {
                    openMainFragment(AddActivity(app, -1))
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
            openMainFragment(HomeActivity(app))
        }
    }

    fun openMainFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentMainWindow, fragment)
        transaction.commit()
    }

    fun btnAddCardSubmit(view: android.view.View) {
        if(findViewById<EditText>(R.id.txtCardNumber).text.toString() == "") {
            Snackbar.make(view, getString(R.string.notificationEnterCardNumber), Snackbar.LENGTH_LONG).show()
        }
        else {
            if(findViewById<Button>(R.id.btnAddCard).text == getString(R.string.edit)) {
                app.data.cards.forEach { it ->
                    if(it.cardNumber == findViewById<EditText>(R.id.txtCardNumber).text.toString()) {
                        it.owner = findViewById<EditText>(R.id.txtOwner).text.toString()
                        it.shop = findViewById<Spinner>(R.id.spinnerShop).selectedItem.toString()
                    }
                }
                openMainFragment(HomeActivity(app))
            }
            else {
                var cardAlreadyExsist:Boolean = false
                app.data.cards.forEach { it ->
                    if(it.cardNumber == findViewById<EditText>(R.id.txtCardNumber).text.toString())
                        cardAlreadyExsist = true
                }

                if(!cardAlreadyExsist) {
                    app.data.cards.add(Card(
                        findViewById<EditText>(R.id.txtCardNumber).text.toString(),
                        findViewById<Spinner>(R.id.spinnerShop).selectedItem.toString(),
                        findViewById<EditText>(R.id.txtOwner).text.toString()
                    ))
                    app.saveToFile()
                    Snackbar.make(view, getString(R.string.notificationSuccessfullyAdddedCard), Snackbar.LENGTH_LONG).show()
                    btnAddCardReset(view)
                    Log.d("Card status", "Successfully added one more card.")
                }
                else {
                    Snackbar.make(view, getString(R.string.notificationCardAlreadyExsist), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    fun btnAddCardReset(view: android.view.View) {
        if(findViewById<Button>(R.id.btnReset).text == getString(R.string.delete)) {
            for ((index, value) in app.data.cards.withIndex()) {
                if(value.cardNumber == findViewById<EditText>(R.id.txtCardNumber).text.toString())
                    app.data.cards.removeAt(index)
            }
            openMainFragment(HomeActivity(app))
            app.saveToFile()
        }
        else {
            findViewById<EditText>(R.id.txtCardNumber).setText("")
            findViewById<Spinner>(R.id.spinnerShop).setSelection(0)
            findViewById<EditText>(R.id.txtOwner).setText("")
        }
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