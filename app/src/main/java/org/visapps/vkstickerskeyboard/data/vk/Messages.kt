package org.visapps.vkstickerskeyboard.data.vk

data class ConversationsResponse(val count : Int, val items : List<Item>, val profiles : List<Profile>)

data class Item(val conversation : Conversation, val last_message : LastMessage)

data class Conversation(val peer : Peer, val in_read : Int, val out_read : Int, val last_message_id : Int, val can_write : CanWrite)

data class LastMessage(val from_id : Int)

data class Peer(val id : Int, val type : String, val local_id : Int)

data class CanWrite(val allowed : Boolean, val reason : Int)

data class Profile(val id : Int, val photo_100 : String)