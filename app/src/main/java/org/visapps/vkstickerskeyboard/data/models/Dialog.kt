package org.visapps.vkstickerskeyboard.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dialogs")
data class Dialog(
    @PrimaryKey
    @ColumnInfo(name = "peerId")
    var peerId: Int,
    @ColumnInfo(name = "last_message_id")
    var last_message_id: Int,
    @ColumnInfo(name = "name")
    var name : String,
    @ColumnInfo(name = "allowed")
    var allowed : Boolean,
    @ColumnInfo(name = "photo")
    var photo : String
){

}