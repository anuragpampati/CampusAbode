package com.example.campusabode


import Item

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
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MyProperties : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener, FilterFragment.Parent , FilterFragment.OnFilterAppliedListener {
    lateinit var mainAct: DrawerLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter // Create a custom adapter
    private val itemList = mutableListOf<Item>() // Create a data model class (Item) for your Firebase data
    private lateinit var filterData: FilterData
    private lateinit var database: FirebaseDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_properties)
        filterData = FilterData(null,null,null,null,null)

        val toolbar = findViewById<Toolbar>(R.id.toolbars)
        setSupportActionBar(findViewById(R.id.toolbars))
        // Get support action bar
        val appBar = supportActionBar
        appBar!!.title = "My Properties"

        mainAct = findViewById(R.id.mainActs)
        val navView = findViewById<NavigationView>(R.id.navView)
        val toggle = ActionBarDrawerToggle(this, mainAct, toolbar, R.string.nav_open, R.string.nav_close)
        mainAct.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

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

        retrieveDataFromFirebase()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_item_1 -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_item_2 -> {

            }
            R.id.nav_item_3 -> {
                val intent = Intent(this, EditProfile::class.java)
                startActivity(intent)
            }
            R.id.nav_item_4 -> {
                userSignOut()
            }
        }
        mainAct.closeDrawer(GravityCompat.START)
        return true
    }
    fun userSignOut(){
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
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
                    //commeysbasadca
                    item.imageUrls = imageUrls

                    // Add the Item to the itemList
                    if(isItemMatchingFilter(item,filterData)){
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

        return bedroomsMatch && bathroomsMatch && priceMatch
    }
    override fun onBackPressed() {
        if (mainAct.isDrawerOpen(GravityCompat.START)) {
            mainAct.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
