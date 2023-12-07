package com.example.campusabode

import FirebaseProperty
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore

class PropertyListRecyclerViewFragment : Fragment(), RecyclerViewAdapter.MyItemClickListener {
    lateinit var myAdapter: RecyclerViewAdapter

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
        val movieList =ArrayList<FirebaseProperty>()
        val db = Firebase.firestore
        val query = db.collection("123")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document == null) {
                        Log.d(TAG, "Document is null.")
                        break // exit the loop if document is null
                    }
                    // Assuming your FirebaseProperty class has a constructor that takes a Map<String, Any>
                    val primaryID = document.getLong("index").toString()
                    val id = document.getLong("id").toString()
                    val address = document.getString("address")
                    val description = document.getString("description")
                    val image = document.getString("image")
                    val url = ""
                    val overview = document.getString("overview")
                    val price = document.getString("price")
                    val video = document.getBoolean("video")

                    // Create a FirebaseProperty object with the extracted fields
                    val firebaseProperty = FirebaseProperty(
                        primaryID,
                        id,
                        address,
                        description,
                        image,
                        url,
                        overview,
                        price,
                        video
                    )
                    movieList.add(firebaseProperty)

                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                myAdapter = RecyclerViewAdapter(movieList)
                myAdapter.setMyItemClickListener(this)
                rv.adapter = myAdapter

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
//        val query = FirebaseDatabase.getInstance().reference.child("123").limitToFirst(50)
//        query.get().addOnSuccessListener {
//            for (item in it.children) {
//                movieList.add(item.getValue<FirebaseProperty>()!!)
//            }
//            myAdapter = RecyclerViewAdapter(movieList)
//            myAdapter.setMyItemClickListener(this)
//            rv.adapter = myAdapter

    }

    companion object;

override fun onItemClickedFromAdapter(property: FirebaseProperty) {
        val manager = activity?.supportFragmentManager
        val transaction = manager?.beginTransaction()
        transaction?.replace(R.id.meContainer, PropertyDetailFragment.newInstance(property))
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    override fun onItemLongClickedFromAdapter(property: FirebaseProperty) {
        Toast.makeText(activity, "You Click the ${property.address} card!", Toast.LENGTH_SHORT).show()
    }
}