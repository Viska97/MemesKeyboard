package org.visapps.vkmemeskeyboard.data.models

import androidx.room.ColumnInfo
import androidx.room.Relation

data class PackObject(
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "logo")
    var logo: String,
    @Relation(parentColumn = "id", entityColumn = "pack_id")
    var memes : List<Meme>
) {

    companion object {
        fun create(pack : Pack, memes : List<Meme>) : PackObject {
            val id = pack.id
            val name = pack.name
            val logo = pack.logo
            return PackObject(id, name, logo, memes)
        }
    }
}