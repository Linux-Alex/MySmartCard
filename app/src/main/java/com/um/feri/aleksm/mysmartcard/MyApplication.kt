package com.um.feri.aleksm.mysmartcard

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

class MyApplication: Application() {
    lateinit var data:MySmartCard
    val MY_SP_FILE_NAME = "myshared.data"
    val MY_FILE_NAME = "mydata.json"
    lateinit var sharedPreferences:SharedPreferences
    lateinit var gson:Gson
    lateinit var file: File


    override fun onCreate() {
        super.onCreate()

        initShared()
        initData()
    }

    fun initShared() {
        sharedPreferences = getSharedPreferences(MY_SP_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun initData() {
        data = try {
            gson = Gson()
            file = File(filesDir, MY_FILE_NAME)
            gson.fromJson(FileUtils.readFileToString(file), MySmartCard::class.java)
        } catch (e: IOException) {
            MySmartCard("John", "Doe")
        }
        Log.d("Info", "Number of saved cards: " + data.cards.size)
    }

    fun saveToFile() {
        try {
            FileUtils.writeStringToFile(file, gson.toJson(data))
        }
        catch (e: IOException) {
            Log.e("Error", "Can't save data to file.");
        }
    }

}