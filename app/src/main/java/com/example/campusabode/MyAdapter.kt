package com.example.campusabode

import Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MyAdapter(private val itemList: List<Item>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val recyclerViewImages: RecyclerView = itemView.findViewById(R.id.recyclerViewImages)
        val bedRooms: TextView = itemView.findViewById(R.id.bedRooms)
        val bathRooms: TextView = itemView.findViewById(R.id.bathRooms)
        val cost: TextView = itemView.findViewById(R.id.cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Bind data to the TextViews
        holder.locationTextView.text = currentItem.location
        holder.bedRooms.text = currentItem.bedrooms + " Bed Rooms"
        holder.bathRooms.text = currentItem.bathrooms + " Bath Rooms"
        holder.cost.text = "$"+currentItem.price
        // Set up the RecyclerView for images
        val layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerViewImages.layoutManager = layoutManager
        val imageAdapter = ImageAdapter(currentItem.imageUrls)
        holder.recyclerViewImages.adapter = imageAdapter
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
