package id.overlogic.devent.ui.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.overlogic.devent.data.response.ListEventsItem
import id.overlogic.devent.databinding.ItemEventBinding

class UpcomingAdapter: ListAdapter<ListEventsItem, UpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder (val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem){
            binding.tvTitle.text = event.name
            binding.tvSubtitle.text = event.summary
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.imgImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
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