package org.visapps.vkstickerskeyboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.visapps.vkstickerskeyboard.GlideRequests
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.data.models.PackStatus
import org.visapps.vkstickerskeyboard.util.toVisibility

class PackViewHolder(view: View, private val glide: GlideRequests) :
    RecyclerView.ViewHolder(view) {
    private val logo: ImageView = view.findViewById(R.id.logo)
    private val name: TextView = view.findViewById(R.id.name)
    private val status_progress: ProgressBar = view.findViewById(R.id.status_progress)
    private val status_button: Button = view.findViewById(R.id.status_button)
    private var pack: Pack? = null

    init {
        view.setOnClickListener {
            pack?.let{

            }
        }
        status_button.setOnClickListener {
            pack?.let{

            }
        }
    }

    fun bind(pack: Pack?) {
        this.pack = pack
        pack?.let{
            name.text = it.name
            status_button.visibility = toVisibility(it.status == PackStatus.NOTSAVED || it.status == PackStatus.SAVED)
            status_progress.visibility = toVisibility(it.status == PackStatus.INPROGRESS)
            glide.load(pack?.logo)
                .fitCenter()
                .into(logo)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests, clickListener: (Int) -> Unit): PackViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pack_item, parent, false)
            return PackViewHolder(view, glide)
        }
    }
}