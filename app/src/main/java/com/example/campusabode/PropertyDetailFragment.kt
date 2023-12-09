package com.example.campusabode

import Item
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.io.Serializable


private const val ARG_PROP = "property"
class PropertyDetailFragment : Fragment() {

    private var property: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            property = it.getSerializable(ARG_PROP) as Item
        }


    }

    override fun onStart() {
        super.onStart()
        loadPropertyInfo()
        setUpImageRecyclerView()
        setMapLinkClickListener()
    }

    private fun loadPropertyInfo() {
        val propAddress: TextView = requireActivity().findViewById(R.id.propAddress)
        val propPrice: TextView = requireActivity().findViewById(R.id.propPrice)
        val propOverview: TextView = requireActivity().findViewById(R.id.propOverview)
        val propBathroom: TextView = requireActivity().findViewById(R.id.brvalue)
        val propBedroom: TextView = requireActivity().findViewById(R.id.bedroomvalue)

        propAddress.text = property!!.location
        propPrice.text = "$" + property!!.price.toString()
        propOverview.text = property!!.description
        propBathroom.text = property!!.bathrooms.toString()
        propBedroom.text = property!!.bedrooms.toString()
        val youTubePlayerView: YouTubePlayerView = requireActivity().findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = property!!.youtube.toString()
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })

    }

    private fun setMapLinkClickListener() {
        val propMapLink: TextView = requireActivity().findViewById(R.id.mapLink)
        propMapLink.text = property!!.map.toString()
        propMapLink.setOnClickListener { openGoogleMaps() }
    }

    private fun openGoogleMaps() {
        val mapLink = property?.map.toString()

        // Create an Intent with the ACTION_VIEW action and the map link URI
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapLink))

        // Check if there's an app that can handle this Intent
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            // Start the activity
            startActivity(intent)
        } else {
            // If no app can handle the Intent, you may want to display a message to the user
             Toast.makeText(requireContext(), "No app to handle the map link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpImageRecyclerView() {
        // Set up the RecyclerView for displaying property images
        val imageRecyclerView: RecyclerView = requireActivity().findViewById(R.id.imageRecyclerView)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        imageRecyclerView.layoutManager = layoutManager

        val imageAdapter = ImageAdapter(property!!.imageUrls)
        imageRecyclerView.adapter = imageAdapter
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
        fun newInstance(param1: Item) =
            PropertyDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PROP, param1 as Serializable)
                }
            }
    }
}