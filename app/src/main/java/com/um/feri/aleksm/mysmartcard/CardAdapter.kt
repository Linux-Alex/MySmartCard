package com.um.feri.aleksm.mysmartcard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.um.feri.aleksm.mysmartcard.R.id.cvCard


class CardAdapter(
    val context: Context,
    private val data:MySmartCard,
    private val onClickObject:CardAdapter.CardOnClick,
    private val onLongClickObject:CardAdapter.CardOnLongClick
    ) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    interface CardOnClick {
        fun onClick(p0:View?, position:Int)
    }

    interface CardOnLongClick {
        fun onClick(p0:View?, position:Int)
    }

    class ViewHolder(ItemView: View) :RecyclerView.ViewHolder(ItemView) {
        val logo:ImageView = itemView.findViewById(R.id.imgCardShopLogo)
        val ownerName: TextView = itemView.findViewById(R.id.lblCardOwnerName)
        val line: CardView = itemView.findViewById(cvCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = data.cards[position]

        holder.ownerName.text = ItemsViewModel.owner

        val shops: Array<String> = context.getResources().getStringArray(R.array.shop)
        val logos: Array<String> = context.getResources().getStringArray(R.array.logo)
//        Log.d("test", logos[shops.indexOf(ItemsViewModel.shop)])
        val choosenLogo: Int = context.getResources().getIdentifier(logos[shops.indexOf(ItemsViewModel.shop)], null, context.packageName)
//        Log.d("test2", context.resources.getIdentifier(logos[shops.indexOf(ItemsViewModel.shop)], "drawable", context.packageName).toString())
        holder.logo.setImageResource(context.resources.getIdentifier(logos[shops.indexOf(ItemsViewModel.shop)], "drawable", context.packageName))

        holder.line.setOnClickListener(object:View.OnClickListener {
            override fun onClick(p0: View?) {
                onClickObject.onClick(p0, holder.adapterPosition)
            }
        })

        holder.line.setOnLongClickListener(object:View.OnLongClickListener {
            override fun onLongClick(p0: View?) : Boolean{
                onLongClickObject.onClick(p0, holder.adapterPosition)
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return data.cards.size
    }
}
