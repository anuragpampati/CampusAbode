package com.example.campusabode

import FirebaseProperty
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(val items: ArrayList<FirebaseProperty>):
    RecyclerView.Adapter<RecyclerViewAdapter.PropertyViewHolder>() {
    var myListener: MyItemClickListener? = null

    interface MyItemClickListener {

        fun onItemClickedFromAdapter(property: FirebaseProperty)
        fun onItemLongClickedFromAdapter(property: FirebaseProperty)
    }
    fun setMyItemClickListener(listener: MyItemClickListener) {
        this.myListener = listener
    }

    inner class PropertyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val propertyCard = view.findViewById<CardView>(R.id.card)
        val propertyPoster = view.findViewById<ImageView>(R.id.rvPoster)
        val propertyAddress = view.findViewById<TextView>(R.id.rvAddress)
        val propertyOverview = view.findViewById<TextView>(R.id.rvOverview)
        val propertyPrice = view.findViewById<TextView>(R.id.rvprice)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.activity_card_1, parent, false)
        return PropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = items[position]
        holder.propertyAddress.text = property.address.toString()
        holder.propertyOverview.text = property.description.toString()
        holder.propertyPrice.text = property.price.toString()
        holder.propertyPoster.setImageResource(R.drawable.default_img)
        Log.d(ContentValues.TAG, "${property.address}")
        Log.d(ContentValues.TAG, "${property.description}")
        Log.d(ContentValues.TAG, "${holder.propertyPrice.text}")
//        Log.d(ContentValues.TAG, "${holder.propertyAddress.text}")

//        Picasso.get().load(property.url).into(holder.propertyPoster)
        holder.propertyCard.setOnClickListener {
            myListener!!.onItemClickedFromAdapter(property)
        }
        holder.propertyCard.setOnLongClickListener {
            myListener!!.onItemLongClickedFromAdapter(property)
            true
        }
    }
}