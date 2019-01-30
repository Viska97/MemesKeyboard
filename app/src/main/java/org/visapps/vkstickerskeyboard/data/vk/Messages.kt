package org.visapps.vkstickerskeyboard.data.vk

import android.provider.MediaStore.Video
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConversationsResponse {

    @SerializedName("response")
    @Expose
    var response: Response? = null

}

class Attachment {

    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("sticker")
    @Expose
    var sticker: Sticker? = null
    @SerializedName("video")
    @Expose
    var video: Video? = null

}

class CanWrite {

    @SerializedName("allowed")
    @Expose
    var allowed: Boolean? = null
    @SerializedName("reason")
    @Expose
    var reason: Int? = null

}

class Conversation {

    @SerializedName("peer")
    @Expose
    var peer: Peer? = null
    @SerializedName("in_read")
    @Expose
    var inRead: Int? = null
    @SerializedName("out_read")
    @Expose
    var outRead: Int? = null
    @SerializedName("last_message_id")
    @Expose
    var lastMessageId: Int? = null
    @SerializedName("can_write")
    @Expose
    var canWrite: CanWrite? = null

}

class Image {

    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("width")
    @Expose
    var width: Int? = null
    @SerializedName("height")
    @Expose
    var height: Int? = null

}

class ImagesWithBackground {

    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("width")
    @Expose
    var width: Int? = null
    @SerializedName("height")
    @Expose
    var height: Int? = null

}

class Item {

    @SerializedName("conversation")
    @Expose
    var conversation: Conversation? = null
    @SerializedName("last_message")
    @Expose
    var lastMessage: LastMessage? = null

}

class LastMessage {

    @SerializedName("date")
    @Expose
    var date: Int? = null
    @SerializedName("from_id")
    @Expose
    var fromId: Int? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("out")
    @Expose
    var out: Int? = null
    @SerializedName("peer_id")
    @Expose
    var peerId: Int? = null
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("conversation_message_id")
    @Expose
    var conversationMessageId: Int? = null
    @SerializedName("fwd_messages")
    @Expose
    var fwdMessages: List<Any>? = null
    @SerializedName("important")
    @Expose
    var important: Boolean? = null
    @SerializedName("random_id")
    @Expose
    var randomId: Int? = null
    @SerializedName("attachments")
    @Expose
    var attachments: List<Attachment>? = null
    @SerializedName("is_hidden")
    @Expose
    var isHidden: Boolean? = null

}

class Peer {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("local_id")
    @Expose
    var localId: Int? = null

}

class Profile {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("is_closed")
    @Expose
    var isClosed: Boolean? = null
    @SerializedName("can_access_closed")
    @Expose
    var canAccessClosed: Boolean? = null
    @SerializedName("photo_max")
    @Expose
    var photomax: String? = null

}

class Response {

    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("items")
    @Expose
    var items: List<Item>? = null
    @SerializedName("profiles")
    @Expose
    var profiles: List<Profile>? = null

}

class Sticker {

    @SerializedName("product_id")
    @Expose
    var productId: Int? = null
    @SerializedName("sticker_id")
    @Expose
    var stickerId: Int? = null
    @SerializedName("images")
    @Expose
    var images: List<Image>? = null
    @SerializedName("images_with_background")
    @Expose
    var imagesWithBackground: List<ImagesWithBackground>? = null

}

class Video {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("owner_id")
    @Expose
    var ownerId: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("duration")
    @Expose
    var duration: Int? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("date")
    @Expose
    var date: Int? = null
    @SerializedName("comments")
    @Expose
    var comments: Int? = null
    @SerializedName("views")
    @Expose
    var views: Int? = null
    @SerializedName("photo_130")
    @Expose
    var photo130: String? = null
    @SerializedName("photo_320")
    @Expose
    var photo320: String? = null
    @SerializedName("photo_800")
    @Expose
    var photo800: String? = null
    @SerializedName("is_favorite")
    @Expose
    var isFavorite: Boolean? = null
    @SerializedName("access_key")
    @Expose
    var accessKey: String? = null
    @SerializedName("platform")
    @Expose
    var platform: String? = null
    @SerializedName("can_edit")
    @Expose
    var canEdit: Int? = null
    @SerializedName("can_add")
    @Expose
    var canAdd: Int? = null

}