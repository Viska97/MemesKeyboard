package org.visapps.vkmemeskeyboard.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_item.view.*
import org.visapps.vkmemeskeyboard.GlideApp
import org.visapps.vkmemeskeyboard.GlideRequests
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Dialog
import org.visapps.vkmemeskeyboard.util.NetworkState

class DialogsAdapter(private val glide: GlideRequests, private val reloadCallback: () -> Unit)
    : PagedListAdapter<Dialog, RecyclerView.ViewHolder>(DIALOG_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.dialog_item -> (holder as DialogViewHolder).bind(getItem(position))
            R.layout.dialog_network_state-> (holder as DialogNetworkStateViewHolder).bindTo(
                networkState)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>) {
        onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.dialog_item -> PackItemViewHolder.create(
                parent,
                glide
            )
            R.layout.dialog_network_state -> DialogNetworkStateViewHolder.create(
                parent,
                reloadCallback
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.SUCCESS

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.dialog_network_state
        } else {
            R.layout.dialog_item
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
        val DIALOG_COMPARATOR = object : DiffUtil.ItemCallback<Dialog>() {
            override fun areContentsTheSame(oldItem: Dialog, newItem: Dialog): Boolean =
                oldItem.peerId == newItem.peerId

            override fun areItemsTheSame(oldItem: Dialog, newItem: Dialog): Boolean {
                return oldItem == newItem
            }
        }
    }
}