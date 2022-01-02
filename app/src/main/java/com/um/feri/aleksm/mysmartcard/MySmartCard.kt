package com.um.feri.aleksm.mysmartcard

import android.location.Location

class MySmartCard(
    var firstname:String,
    var lastname:String,
    var gender:Gender = Gender.Male,
    var cards:MutableList<Card> = mutableListOf(),
    var latestLocation:Location? = null
) {

}