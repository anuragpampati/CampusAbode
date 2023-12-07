package com.example.campusabode

import FirebaseProperty
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.Serializable

private const val ARG_PROP = "property"
class PropertyDetailFragment : Fragment() {

    private var property: FirebaseProperty? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            property = it.getSerializable(ARG_PROP) as FirebaseProperty
        }

    }

    override fun onStart() {
        super.onStart()
        loadPropertyInfo()
    }

    private fun loadPropertyInfo() {
        val propAddress: TextView = requireActivity().findViewById(R.id.propAddress)
//        val movieID: TextView = requireActivity().findViewById(R.id.movieID)
        val propPrice: TextView = requireActivity().findViewById(R.id.propPrice)
        val propOverview: TextView = requireActivity().findViewById(R.id.propOverview)
//        val movieYear: TextView = requireActivity().findViewById(R.id.movieYear)
//        val movieImage: ImageView = requireActivity().findViewById(R.id.movieImage)


        propAddress.text = property!!.address
        propPrice.text = property!!.price.toString()
        propOverview.text = property!!.overview
//        movieYear.text = property!!.release_date
//        movieImage.setImageResource(property!!.poster_pos!!)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_property_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: FirebaseProperty) =
            PropertyDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PROP, param1 as Serializable)
                }
            }
    }
}