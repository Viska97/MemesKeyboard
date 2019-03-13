package org.visapps.vkmemeskeyboard.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import org.visapps.vkmemeskeyboard.GlideRequests
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.data.models.PackStatus
import org.visapps.vkmemeskeyboard.util.toVisibility
import org.visapps.vkmemeskeyboard.workers.SavePackWorker

class PackItemViewHolder(private val view: View, private val glide: GlideRequests) :
    RecyclerView.ViewHolder(view) {
    private val logo = view.findViewById<ImageView>(R.id.logo)
    private val name = view.findViewById<TextView>(R.id.name)
    private val statusProgress = view.findViewById<ProgressBar>(R.id.status_progress)
    private val statusButton = view.findViewById<ImageButton>(R.id.status_button)
    private var pack: Pack? = null

    init {
        view.setOnClickListener {
            pack?.let {
                val bundle = Bundle()
                bundle.putInt("packId", it.id)
                Navigation.findNavController(view).navigate(R.id.action_allpacks_to_pack, bundle)
            }
        }
        statusButton.setOnClickListener {
            pack?.let {
                //it.status = PackStatus.IN_PROGRESS
                //bind(it)
                val data = Data.Builder().putInt("packId", it.id).build()
                val workRequest = OneTimeWorkRequest.Builder(SavePackWorker::class.java).setInputData(data).build()
                WorkManager.getInstance().enqueue(workRequest)
            }
        }
    }

    fun bind(pack: Pack?) {
        this.pack = pack
        pack?.let {
            name.text = it.name
            if (it.status == PackStatus.SAVED) {
                statusButton.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_check_black_24dp))
            } else {
                statusButton.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_add_black_24dp))
            }
            statusButton.visibility =
                toVisibility(it.status == PackStatus.NOTSAVED || it.status == PackStatus.SAVED)
            statusProgress.visibility =
                toVisibility(it.status == PackStatus.IN_PROGRESS)
            glide.load(it.logo)
                .fitCenter()
                .into(logo)
        }
    }

    fun updateStatus(newStatus: Int) {
        pack?.let {
            it.status = newStatus
            if (it.status == PackStatus.SAVED) {
                statusButton.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_check_black_24dp))
            } else {
                statusButton.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_add_black_24dp))
            }
            statusButton.visibility =
                toVisibility(it.status == PackStatus.NOTSAVED || it.status == PackStatus.SAVED)
            statusProgress.visibility =
                toVisibility(it.status == PackStatus.IN_PROGRESS)
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