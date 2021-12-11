package com.um.feri.aleksm.mysmartcard

class Card(
    var cardNumber:String,
    var shop:String,
    var owner:String
) : Comparable<Card> {
    override fun compareTo(other: Card): Int {
        return shop.compareTo(other.shop)
    }

}