package com.um.feri.aleksm.mysmartcard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.um.feri.aleksm.mysmartcard.databinding.ActivityMainBinding
import org.osmdroid.config.Configuration

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var app: MyApplication

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        supportActionBar?.hide()
        findViewById<ChipNavigationBar>(R.id.navMain).setItemSelected(R.id.menu_home)

        // OSM maps
        //Configuration.getInstance().load(applicationContext, this.getPreferences(Context.MODE_PRIVATE))

        binding.navMain.setOnItemSelectedListener{
            when(it) {
                R.id.menu_home -> {
                    openMainFragment(HomeActivity(app))
                }
                R.id.menu_add -> {
                    openMainFragment(AddActivity(app, -1))
                }
                R.id.menu_search -> {
                    if(app.data.latestLocation != null)
                        openMainFragment(SearchActivity(app.data.latestLocation!!))
                }
                R.id.menu_settings -> {
                    openMainFragment(SettingsActivity())
                }
            }
        }

        if(savedInstanceState == null) {
            openMainFragment(HomeActivity(app))
        }

        requireLocationPermission()
        getLocation()
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun requireLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.i("MAP", "Precise location access granted.")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.i("MAP", "Only approximate location access granted.")
                } else -> {
                    Log.i("MAP", "No location access granted.")
                }
            }
        }

        // ...

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            Log.i("MAP", "Last location: " + location?.latitude + ", " + location?.longitude)
            if (location != null) {
                app.data.latestLocation = location
            }
        }
    }
}