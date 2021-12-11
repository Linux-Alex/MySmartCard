package com.um.feri.aleksm.mysmartcard

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract
import android.util.Log
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
            gson.fromJson(FileUtils.readFileToString(file), MySmartCard::class.java)
        } catch (e: IOException) {
            MySmartCard("John", "Doe")
        }
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