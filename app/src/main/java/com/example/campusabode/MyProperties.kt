package com.example.campusabode


import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log
import android.widget.Button


class MyProperties : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter // Create a custom adapter
    private val itemList = mutableListOf<Item>() // Create a data model class (Item) for your Firebase data

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_properties)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(itemList) // Pass your data model list to the adapter
        recyclerView.adapter = adapter
        val addNewProperty = findViewById<Button>(R.id.addProperty)

        database = FirebaseDatabase.getInstance()

        // Retrieve data from Firebase
        retrieveDataFromFirebase()

        addNewProperty.setOnClickListener{
            val intent = Intent(this, AddProperty::class.java)
            startActivity(intent)
        }
    }

    private fun retrieveDataFromFirebase() {
        val databaseReference = database.reference.child("users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                itemList.clear()

                for (userSnapshot in dataSnapshot.children) {
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    val location = userSnapshot.child("location").getValue(String::class.java)
                    val price = userSnapshot.child("price").getValue(String::class.java)
                    val phone = userSnapshot.child("phone").getValue(String::class.java)
                    val bedrooms = userSnapshot.child("bedrooms").getValue(String::class.java)
                    val bathrooms =userSnapshot.child("bathrooms").getValue(String::class.java)
                    val availability =userSnapshot.child("availability").getValue(String::class.java)
                    val map= userSnapshot.child("map").getValue(String::class.java)
                    val youtube =userSnapshot.child("youtube").getValue(String::class.java)

                    // Create an instance of your data model class (Item)
                    val item = Item(name, email, location, emptyList(),price,phone,bedrooms,bathrooms,availability,map,youtube) // Initialize with an empty list of image URLs

                    // Retrieve image URLs from the "images" node
                    val imagesSnapshot = userSnapshot.child("images")
                    val imageUrls = mutableListOf<String>()

                    for (imageSnapshot in imagesSnapshot.children) {
                        val imageUrl = imageSnapshot.getValue(String::class.java)
                        imageUrl?.let { imageUrls.add(it) }
                    }

                    // Set the list of image URLs to the Item
                    item.imageUrls = imageUrls

                    // Add the Item to the itemList
                    itemList.add(item)
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("SecondActivity", "Error in retrieving data from Firebase: ${databaseError.message}")
            }
        })
    }

}
