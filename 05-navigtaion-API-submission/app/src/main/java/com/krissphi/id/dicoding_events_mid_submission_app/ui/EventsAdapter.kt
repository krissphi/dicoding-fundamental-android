package com.krissphi.id.dicoding_events_mid_submission_app.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krissphi.id.dicoding_events_mid_submission_app.R
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.ItemEventBinding
import com.krissphi.id.dicoding_events_mid_submission_app.ui.detail.DetailEventActivity



class EventsAdapter : ListAdapter<ListEventsItem, EventsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

    }

    inner class MyViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem){
            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.ivLogo)
            binding.tvTitle.text = "${event.name}"
            binding.tvCity.text = "${event.cityName}"
            val registrantText = binding.root.context.getString(R.string.event_quota, event.registrants, event.quota)
            binding.tvRegistrant.text = registrantText


            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailEventActivity::class.java)
                intent.putExtra("event_data", event)
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
