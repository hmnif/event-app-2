package com.example.eventapps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventapps.databinding.ItemEventVerticalBinding
import com.example.eventapps.remote.response.ListEventsItem
import com.example.eventapps.ui.detail.DetailActivity

class VerticalEventAdapter : ListAdapter<ListEventsItem, VerticalEventAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
    class MyViewHolder(private val binding: ItemEventVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ListEventsItem){
            binding.tvName.text = "${review.name}"
            Glide.with(binding.imgMediaCover.context)
                .load(review.mediaCover)
                .into(binding.imgMediaCover)

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("EVENT_ID", review.id)  // Send the event id to DetailActivity
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}