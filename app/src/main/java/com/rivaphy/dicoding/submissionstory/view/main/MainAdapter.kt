package com.rivaphy.dicoding.submissionstory.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem
import com.rivaphy.dicoding.submissionstory.databinding.ItemMainBinding
import com.rivaphy.dicoding.submissionstory.view.detail.DetailActivity
import com.rivaphy.dicoding.submissionstory.withDateFormat

class MainAdapter: PagingDataAdapter<ListStoryItem, MainAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataUser = getItem(position)
        dataUser?.let { holder.bind(it) }
    }

    class ViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataStory: ListStoryItem) {
            Glide.with(binding.root.context)
                .load(dataStory.photoUrl)
                .into(binding.imgItemMain)

            binding.tvItemName.text = dataStory.name
            binding.tvItemDate.text = dataStory.createdAt.withDateFormat()
            binding.tvItemDesc.text = dataStory.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(USERNAME, dataStory.name)
                intent.putExtra(CREATED_AT, dataStory.createdAt)
                intent.putExtra(DESC, dataStory.description)
                intent.putExtra(PHOTO_URL, dataStory.photoUrl)

                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        const val USERNAME = "name"
        const val CREATED_AT = "createdAt"
        const val DESC = "description"
        const val PHOTO_URL = "photoUrl"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}