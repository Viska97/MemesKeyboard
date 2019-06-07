package org.visapps.vkmemeskeyboard.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import org.visapps.vkmemeskeyboard.data.models.Pack

class PackDiffCallback(private val oldList: List<Pack>,
                       private val newList: List<Pack>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return if (PacksAdapter.sameExceptStatus(
                oldList[oldItemPosition],
                newList[newItemPosition]
            )
        ) {
            mutableListOf(newList[newItemPosition].status)
        } else {
            null
        }
    }
}