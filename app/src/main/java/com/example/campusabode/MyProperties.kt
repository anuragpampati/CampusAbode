package com.example.campusabode


import android.annotation.SuppressLint
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
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth


class MyProperties : AppCompatActivity(),FilterFragment.Parent,FilterFragment.OnFilterAppliedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter // Create a custom adapter
    private val itemList = mutableListOf<Item>() // Create a data model class (Item) for your Firebase data
    private lateinit var filterData: FilterData
    private lateinit var database: FirebaseDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_properties)
        filterData = FilterData(null,null,null,null)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(itemList) // Pass your data model list to the adapter
        recyclerView.adapter = adapter
        val addNewProperty = findViewById<Button>(R.id.addProperty)
        val flitersBtn = findViewById<Button>(R.id.filtersBtn)
        val clearFilBtn = findViewById<Button>(R.id.clearFiltersBtn)

        database = FirebaseDatabase.getInstance()

        // Retrieve data from Firebase
        retrieveDataFromFirebase()

        addNewProperty.setOnClickListener{
            val intent = Intent(this, AddProperty::class.java)
            startActivity(intent)
        }
        flitersBtn.setOnClickListener{
            val fragment = supportFragmentManager.findFragmentByTag("FilterFragment")

            if (fragment != null) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.remove(fragment)
                transaction.commit()
            } else {
                val filterFragment = FilterFragment()
                filterFragment.setParent(this)
                filterFragment.setFilterListener(this)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, filterFragment).commit()
            }
        }

        clearFilBtn.setOnClickListener{
            filterData.minPrice=null
            filterData.maxPrice=null
            filterData.bedrooms=null
            filterData.bathrooms = null
            retrieveDataFromFirebase()

        }

    }
    override fun onFilterApplied(selectedBedrooms: String, selectedBathrooms: String, minPrice: String, maxPrice: String) {
        // Handle filter logic here
        // You can use the selectedBedrooms and selectedBathrooms to filter your data
        // Update your data source or perform any other actions based on the filter

        filterData.bathrooms=selectedBathrooms
        filterData.bedrooms = selectedBedrooms
        filterData.minPrice = minPrice
        filterData.maxPrice = maxPrice

        Log.e("FilterFragment", "Selected Bedrooms: ${filterData.bedrooms}, Selected Bathrooms: ${filterData.bathrooms}")
        retrieveDataFromFirebase()
    }

    private fun retrieveDataFromFirebase() {
        val auth = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = database.reference.child("users").child(auth.toString())


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                itemList.clear()

                for (userSnapshot in dataSnapshot.children) {

                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    val description = userSnapshot.child("description").getValue(String::class.java)
                    val location = userSnapshot.child("location").getValue(String::class.java)
                    val price = userSnapshot.child("price").getValue(String::class.java)
                    val phone = userSnapshot.child("phone").getValue(String::class.java)
                    val bedrooms = userSnapshot.child("bedrooms").getValue(String::class.java)
                    val bathrooms =userSnapshot.child("bathrooms").getValue(String::class.java)
                    val availability =userSnapshot.child("availability").getValue(String::class.java)
                    val map= userSnapshot.child("map").getValue(String::class.java)
                    val youtube =userSnapshot.child("youtube").getValue(String::class.java)

                    // Create an instance of your data model class (Item)
                    val item = Item(name, email, location,description, emptyList(),price,phone,bedrooms,bathrooms,availability,map,youtube) // Initialize with an empty list of image URLs

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
                    if (isItemMatchingFilter(item, filterData)) {
                        itemList.add(item)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("SecondActivity", "Error in retrieving data from Firebase: ${databaseError.message}")
            }
        })
    }
    private fun isItemMatchingFilter(item: Item, filterData: FilterData): Boolean {
        var bedroomsMatch:Boolean
        var bathroomsMatch:Boolean
        var priceMatch:Boolean

        if(filterData.bedrooms.equals(">4")){
            Log.e("SecondActivity", (Integer.parseInt(item.bedrooms).toString()))
            bedroomsMatch =  Integer.parseInt(item.bedrooms)>=4
            Log.e("SecondActivity", bedroomsMatch.toString())
        }else{
            bedroomsMatch = filterData.bedrooms.isNullOrBlank()|| filterData.bedrooms=="" || item.bedrooms?.toString().equals(filterData.bedrooms)

        }
        if(filterData.bathrooms.equals(">4")){
            bathroomsMatch =  Integer.parseInt(item.bathrooms)>=4
        }else{
            bathroomsMatch = filterData.bathrooms.isNullOrBlank()||filterData.bathrooms=="" || item.bathrooms?.toString().equals(filterData.bathrooms)
        }
        val itemPrice = item.price?.toDoubleOrNull()
        val minPrice = filterData.minPrice?.toDoubleOrNull() ?: Double.MIN_VALUE
        val maxPrice = filterData.maxPrice?.toDoubleOrNull() ?: Double.MAX_VALUE

        priceMatch = itemPrice != null && itemPrice in minPrice..maxPrice
        Log.e("SecondActivity", bathroomsMatch.toString())
//        Log.e("FilterFragment", "item Bedrooms: ${item.bedrooms}, item Bathrooms: ${item.bedrooms}")
        // Return true if the item matches all filter criteria
        return bedroomsMatch && bathroomsMatch && priceMatch
    }
}
