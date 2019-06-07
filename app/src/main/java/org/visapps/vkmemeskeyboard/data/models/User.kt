package org.visapps.vkmemeskeyboard.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey
    var id : Int,
    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    var firstName : String,
    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    var lastName : String,
    @SerializedName("photo_max")
    @ColumnInfo(name = "photo_max")
    var photoMax : String
)