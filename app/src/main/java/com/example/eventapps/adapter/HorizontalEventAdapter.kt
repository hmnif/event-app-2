package com.example.eventapps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventapps.databinding.ItemEventHorizontalBinding
import com.example.eventapps.remote.response.ListEventsItem
import com.example.eventapps.ui.detail.DetailActivity

class HorizontalEventAdapter : ListAdapter<ListEventsItem, HorizontalEventAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(private val binding: ItemEventHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvName.text = event.name
            Glide.with(binding.imgMediaCover.context)
                .load(event.imageLogo)
                .into(binding.imgMediaCover)

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("EVENT_ID", event.id)
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) = oldItem == newItem
        }
    }
}