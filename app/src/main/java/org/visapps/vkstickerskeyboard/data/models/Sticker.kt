package org.visapps.vkstickerskeyboard.data.models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "stickers",
    foreignKeys = [ForeignKey(entity = Pack::class, parentColumns = ["id"], childColumns = ["pack_id"], onDelete = CASCADE)],
    indices = [Index("pack_id")]
)
data class Sticker(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Int,
    @SerializedName("pack_id")
    @ColumnInfo(name = "pack_id")
    var pack_id: Int,
    @SerializedName("attachment")
    @ColumnInfo(name = "attachment")
    var attachment: String,
    @SerializedName("image")
    @ColumnInfo(name = "image")
    var image: String
) {

}