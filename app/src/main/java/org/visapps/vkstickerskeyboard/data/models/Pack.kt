package org.visapps.vkstickerskeyboard.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "packs")
data class Pack(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String,
    @SerializedName("logo")
    @ColumnInfo(name = "logo")
    val logo: String
) {

    @ColumnInfo(name = "status")
    var status: Int = PackStatus.NOTSAVED
    @ColumnInfo(name = "updated")
    var updated: Boolean = true
}

object PackStatus {
    const val NOTSAVED = 0
    const val INPROGRESS = 1
    const val SAVED = 2
}