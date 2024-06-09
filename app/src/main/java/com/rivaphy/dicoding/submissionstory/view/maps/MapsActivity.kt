package com.rivaphy.dicoding.submissionstory.view.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.rivaphy.dicoding.submissionstory.R
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem
import com.rivaphy.dicoding.submissionstory.databinding.ActivityMapsBinding
import com.rivaphy.dicoding.submissionstory.view.UserViewModelFactory
import com.rivaphy.dicoding.submissionstory.view.ViewModelFactory
import com.rivaphy.dicoding.submissionstory.view.main.MainActivity
import com.rivaphy.dicoding.submissionstory.view.main.MainViewModel
import com.rivaphy.dicoding.submissionstory.view.story.TellYoursViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, UserViewModelFactory.getInstance(this)).get(MapsViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
        viewModel.getMapStories()
    }

    private fun setupViewModel() {

        viewModel.mapStories.observe(this) { stories ->
            if (!stories.isNullOrEmpty()) {
                mMap.clear()
                for (story in stories) {
                    val position = LatLng(story.lat, story.lon)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(story.name)
                            .snippet(story.description)
                    )
                }
                val firstStory = stories.first()
                val firstPosition = LatLng(firstStory.lat, firstStory.lon)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPosition, 5f))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}