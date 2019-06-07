package org.visapps.vkmemeskeyboard.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.visapps.vkmemeskeyboard.GlideRequests
import org.visapps.vkmemeskeyboard.data.models.Pack

class SavedPacksAdapter(private val glide: GlideRequests) : RecyclerView.Adapter<PackItemViewHolder>() {

    private val packs = mutableListOf<Pack>()

    override fun getItemCount() = packs.size

    override fun onBindViewHolder(holder: PackItemViewHolder, position: Int) {
        holder.bind(packs[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackItemViewHolder {
        return PackItemViewHolder.create(parent, glide)
    }

    fun updatePacks(packs : List<Pack>) {
        val diffCallback = PackDiffCallback(this.packs, packs)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.packs.clear()
        this.packs.addAll(packs)
        diffResult.dispatchUpdatesTo(this)
    }

}