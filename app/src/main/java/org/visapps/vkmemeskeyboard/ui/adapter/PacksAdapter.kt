package org.visapps.vkmemeskeyboard.ui.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.visapps.vkmemeskeyboard.GlideRequests
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.util.NetworkState

class PacksAdapter(private val glide: GlideRequests, private val reloadCallback: () -> Unit) : PagedListAdapter<Pack, RecyclerView.ViewHolder>(
    PACK_COMPARATOR
){

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.pack_item-> (holder as PackItemViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (holder as PackItemViewHolder).updateStatus(payloads[0] as Int)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.pack_item -> PackItemViewHolder.create(
                parent,
                glide
            )
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(
                parent,
                reloadCallback
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.SUCCESS

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.pack_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val PACK_COMPARATOR = object : DiffUtil.ItemCallback<Pack>() {
            override fun areContentsTheSame(oldItem: Pack, newItem: Pack): Boolean =
                oldItem.id == newItem.id

            override fun areItemsTheSame(oldItem: Pack, newItem: Pack): Boolean {
                return oldItem.status == newItem.status && oldItem.updated == newItem.updated && oldItem.logo == newItem.logo && oldItem.name == newItem.name
            }

            override fun getChangePayload(oldItem: Pack, newItem: Pack): Any? {
                return if (sameExceptStatus(
                        oldItem,
                        newItem
                    )
                ) {
                    mutableListOf(newItem.status)
                } else {
                    null
                }
            }
        }

        fun sameExceptStatus(oldItem: Pack, newItem: Pack): Boolean {
            return oldItem.name == newItem.name && oldItem.logo == newItem.logo
        }
    }
}