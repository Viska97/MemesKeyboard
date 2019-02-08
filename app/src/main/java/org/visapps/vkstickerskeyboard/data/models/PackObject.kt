package org.visapps.vkstickerskeyboard.data.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Relation

data class PackObject(
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "logo")
    var logo: String,
    @Relation(parentColumn = "id", entityColumn = "pack_id")
    var stickers : List<Sticker>
) {

    companion object {
        fun create(pack : Pack, stickers : List<Sticker>) : PackObject {
            val id = pack.id
            val name = pack.name
            val logo = pack.logo
            return PackObject(id, name, logo, stickers)
        }
    }
}