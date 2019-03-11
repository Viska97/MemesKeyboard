package org.visapps.vkmemeskeyboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.util.NetworkState
import org.visapps.vkmemeskeyboard.util.toVisibility

class NetworkStateItemViewHolder(
    view: View,
    private val reloadCallback: () -> Unit
) : RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val reload = view.findViewById<Button>(R.id.reload_button)
    private val errorMsg = view.findViewById<TextView>(R.id.error_msg)

    init {
        reload.setOnClickListener {
            reloadCallback()
        }
    }

    fun bindTo(networkState: Int?) {
        progressBar.visibility =
            toVisibility(networkState == NetworkState.RUNNING)
        reload.visibility =
            toVisibility(networkState == NetworkState.FAILED)
        errorMsg.visibility =
            toVisibility(networkState == NetworkState.FAILED)
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view, retryCallback)
        }
    }
}