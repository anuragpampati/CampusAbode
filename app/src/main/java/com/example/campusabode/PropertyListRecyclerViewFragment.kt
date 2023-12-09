package com.example.campusabode

import Item
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PropertyListRecyclerViewFragment : Fragment(), RecyclerViewAdapter.MyItemClickListener, FilterFragment.Parent , FilterFragment.OnFilterAppliedListener {
    lateinit var myAdapter: RecyclerViewAdapter
    private lateinit var database: FirebaseDatabase
    private val itemList = mutableListOf<Item>()
    private lateinit var filterData: FilterData


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_property_list_recycler_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = requireActivity().findViewById<RecyclerView>(R.id.rv)
        filterData = FilterData(null,null,null,null)
        rv.hasFixedSize()
        rv.layoutManager =  LinearLayoutManager(view.context)
//         myAdapter = RecyclerViewAdapter(ArrayList(PropertyInit().propertyList))
        myAdapter = RecyclerViewAdapter(itemList)
        myAdapter.setMyItemClickListener(this)
        rv.adapter = myAdapter
        val flitersBtn = view.findViewById<Button>(R.id.filtersBtn)
        val clearFilBtn = view.findViewById<Button>(R.id.clearFiltersBtn)
        retrieveDataFromFirebase()

        flitersBtn.setOnClickListener{
            val fragment = parentFragmentManager.findFragmentByTag("FilterFragment")
            if (fragment != null) {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(fragment)
                transaction.commit()
            } else {
                val filterFragment = FilterFragment()
                filterFragment.setParent(this)
                filterFragment.setFilterListener(this)
                parentFragmentManager.beginTransaction()
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

    private fun retrieveDataFromFirebase(){

        database = FirebaseDatabase.getInstance()
        // val auth = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = database.reference.child("AllProperties")
        databaseReference.get().addOnSuccessListener { dataSnapshot ->
            itemList.clear()
            for (userSnapshot in dataSnapshot.children){

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
                val item = Item(name, email, location,description, emptyList(),price,phone,
                    bedrooms,bathrooms,availability,map,youtube) // Initialize with an empty list of image URLs

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
                if(isItemMatchingFilter(item,filterData)){
                    itemList.add(item)
                }
            }
            Log.e("PropertyListFragment", "Number of items retrieved: ${itemList.size}")

            myAdapter.notifyDataSetChanged()
        }
    }

    companion object;

    override fun onItemClickedFromAdapter(property: Item) {
        val manager = activity?.supportFragmentManager
        val transaction = manager?.beginTransaction()
        transaction?.replace(R.id.meContainer, PropertyDetailFragment.newInstance(property))
        transaction?.addToBackStack(null)
        transaction?.commit()
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

    override fun onItemLongClickedFromAdapter(property: Item) {
        Toast.makeText(activity, "You Click the ${property.location} card!", Toast.LENGTH_SHORT).show()
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
}