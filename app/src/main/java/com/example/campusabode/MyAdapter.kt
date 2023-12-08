package com.example.campusabode

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
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val recyclerViewImages: RecyclerView = itemView.findViewById(R.id.recyclerViewImages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Bind data to the TextViews
        holder.nameTextView.text = currentItem.name
        holder.emailTextView.text = currentItem.email
        holder.locationTextView.text = currentItem.location

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
