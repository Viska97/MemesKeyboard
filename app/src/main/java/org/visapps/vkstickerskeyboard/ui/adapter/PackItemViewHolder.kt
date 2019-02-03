package org.visapps.vkstickerskeyboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.visapps.vkstickerskeyboard.GlideRequests
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.data.models.PackStatus
import org.visapps.vkstickerskeyboard.util.toVisibility

class PackItemViewHolder(private val view: View, private val glide: GlideRequests) :
    RecyclerView.ViewHolder(view) {
    private val logo = view.findViewById<ImageView>(R.id.logo)
    private val name = view.findViewById<TextView>(R.id.name)
    private val statusProgress = view.findViewById<ProgressBar>(R.id.status_progress)
    private val statusButton  = view.findViewById<ImageButton>(R.id.status_button)
    private var pack: Pack? = null

    init {
        view.setOnClickListener {
            pack?.let{

            }
        }
        statusButton.setOnClickListener {
            pack?.let{

            }
        }
    }

    fun bind(pack: Pack?) {
        this.pack = pack
        pack?.let{
            name.text = it.name
            if(it.status == PackStatus.SAVED){
                statusButton.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_check_black_24dp))
            }
            else{
                statusButton.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_add_black_24dp))
            }
            statusButton.visibility = toVisibility(it.status == PackStatus.NOTSAVED || it.status == PackStatus.SAVED)
            statusProgress.visibility = toVisibility(it.status == PackStatus.INPROGRESS)
            glide.load(it.logo)
                .fitCenter()
                .into(logo)
        }
    }

    fun updateStatus(newStatus: Int) {
        pack?.let{
            it.status = newStatus
            if(it.status == PackStatus.SAVED){
                statusButton.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_check_black_24dp))
            }
            else{
                statusButton.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_add_black_24dp))
            }
            statusButton.visibility = toVisibility(it.status == PackStatus.NOTSAVED || it.status == PackStatus.SAVED)
            statusProgress.visibility = toVisibility(it.status == PackStatus.INPROGRESS)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): PackItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pack_item, parent, false)
            return PackItemViewHolder(view, glide)
        }
    }
}