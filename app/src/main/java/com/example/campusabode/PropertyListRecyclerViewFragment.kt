package com.example.campusabode

import Item
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PropertyListRecyclerViewFragment : Fragment(), RecyclerViewAdapter.MyItemClickListener {
    lateinit var myAdapter: RecyclerViewAdapter
    private lateinit var database: FirebaseDatabase
    private val itemList = mutableListOf<Item>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_property_list_recycler_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(view.context)
        val rv = requireActivity().findViewById<RecyclerView>(R.id.rv)
        rv.hasFixedSize()
        rv.layoutManager = layoutManager
//         myAdapter = RecyclerViewAdapter(ArrayList(PropertyInit().propertyList))
        database = FirebaseDatabase.getInstance()
        val auth = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = database.reference.child("users")

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
                itemList.add(item)
                }

                myAdapter = RecyclerViewAdapter(itemList)
                myAdapter.setMyItemClickListener(this)
                rv.adapter = myAdapter
            }


//        val query = db.collection("123")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    if (document == null) {
//                        Log.d(TAG, "Document is null.")
//                        break // exit the loop if document is null
//                    }
//                    // Assuming your FirebaseProperty class has a constructor that takes a Map<String, Any>
//                    val name = document.getString("name")
//                    val email = document.getString("email")
//                    val address = document.getString("address")
//                    val description = document.getString("description")
//                    val image = document.getString("image")
//                    val url = ""
//                    val overview = document.getString("overview")
//                    val price = document.getString("price")
//                    val video = document.getBoolean("video")
//
//                    // Create a FirebaseProperty object with the extracted fields
//                    val firebaseProperty = Item(
//                        primaryID,
//                        id,
//                        address,
//                        description,
//                        emptyList(),
//                        url,
//                        overview,
//                        price,
//                        video
//                    )
//                    propList.add(firebaseProperty)
//
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//                myAdapter = RecyclerViewAdapter(propList)
//                myAdapter.setMyItemClickListener(this)
//                rv.adapter = myAdapter
//
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }

    }

    companion object;

    override fun onItemClickedFromAdapter(property: Item) {
        val manager = activity?.supportFragmentManager
        val transaction = manager?.beginTransaction()
        transaction?.replace(R.id.meContainer, PropertyDetailFragment.newInstance(property))
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    override fun onItemLongClickedFromAdapter(property: Item) {
        Toast.makeText(activity, "You Click the ${property.location} card!", Toast.LENGTH_SHORT).show()
    }
}