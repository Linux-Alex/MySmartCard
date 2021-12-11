package com.um.feri.aleksm.mysmartcard

class MySmartCard(
    var firstname:String,
    var lastname:String,
    var gender:Gender = Gender.Male,
    var cards:MutableList<Card> = mutableListOf()
) {

}