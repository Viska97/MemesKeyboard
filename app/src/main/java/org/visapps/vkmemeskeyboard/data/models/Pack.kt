package org.visapps.vkmemeskeyboard.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "packs")
data class Pack(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    var id: Int,
    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    var name: String,
    @SerializedName("logo")
    @Expose
    @ColumnInfo(name = "logo")
    var logo: String
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