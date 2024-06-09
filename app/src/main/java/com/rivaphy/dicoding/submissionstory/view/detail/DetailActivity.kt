package com.rivaphy.dicoding.submissionstory.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rivaphy.dicoding.submissionstory.databinding.ActivityDetailBinding
import com.rivaphy.dicoding.submissionstory.view.main.MainAdapter
import com.rivaphy.dicoding.submissionstory.withDateFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(MainAdapter.USERNAME)
        val created_at = intent.getStringExtra(MainAdapter.CREATED_AT)
        val desc = intent.getStringExtra(MainAdapter.DESC)
        val photoUrl = intent.getStringExtra(MainAdapter.PHOTO_URL)

        binding.tvDetailName.text = username
        binding.tvDetailDate.text = created_at?.withDateFormat()
        binding.tvDetailDesc.text = desc
        Glide.with(binding.root.context)
            .load(photoUrl)
            .into(binding.imgDetail)
    }
}