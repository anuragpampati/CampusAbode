package com.example.campusabode

import Item
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
    }

    private fun loadPropertyInfo() {
        val propAddress: TextView = requireActivity().findViewById(R.id.propAddress)
//        val movieID: TextView = requireActivity().findViewById(R.id.movieID)
        val propPrice: TextView = requireActivity().findViewById(R.id.propPrice)
        val propOverview: TextView = requireActivity().findViewById(R.id.propOverview)
//        val movieYear: TextView = requireActivity().findViewById(R.id.movieYear)
//        val movieImage: ImageView = requireActivity().findViewById(R.id.movieImage)


        propAddress.text = property!!.location
        propPrice.text = property!!.price.toString()
        propOverview.text = property!!.description
//        movieYear.text = property!!.release_date
//        movieImage.setImageResource(property!!.poster_pos!!)
        val youTubePlayerView: YouTubePlayerView = requireActivity().findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "S0Q4gqBUs7c"
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })

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