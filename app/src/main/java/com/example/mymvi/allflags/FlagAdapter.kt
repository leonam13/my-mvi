package com.example.mymvi.allflags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymvi.mvi.Flag
import com.example.mymvi.R
import kotlinx.android.synthetic.main.item_flag.view.*

class FlagAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = emptyList<Flag>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_flag, parent, false)
        return FlagViewHolder(inflate)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FlagViewHolder).bind(items[position])
    }

    class FlagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(flag: Flag) {
            itemView.run {
                Glide.with(context).load(flag.url).into(flagImage)
                flagName.text = flag.name
            }
        }
    }
}