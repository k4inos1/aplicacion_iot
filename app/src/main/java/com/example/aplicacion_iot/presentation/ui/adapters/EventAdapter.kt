package com.example.aplicacion_iot.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacion_iot.R
import com.example.aplicacion_iot.domain.model.Event
import com.example.aplicacion_iot.domain.model.EventType

class EventAdapter : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.ivEventIcon)
        private val messageTextView: TextView = itemView.findViewById(R.id.tvEventMessage)
        private val timestampTextView: TextView = itemView.findViewById(R.id.tvEventTimestamp)

        fun bind(event: Event) {
            messageTextView.text = event.message
            timestampTextView.text = event.timestamp

            val iconRes = when (event.type) {
                EventType.INFO -> R.drawable.ic_info
                EventType.UPLOAD -> R.drawable.ic_upload
                EventType.DOWNLOAD -> R.drawable.ic_download
                EventType.ERROR -> R.drawable.ic_error
            }
            iconImageView.setImageResource(iconRes)
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem // In a real app with IDs, you'd compare oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}
