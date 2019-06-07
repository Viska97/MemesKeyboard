package org.visapps.vkmemeskeyboard.data.vk

import com.google.gson.annotations.SerializedName
import org.visapps.vkmemeskeyboard.data.models.User

class UsersResponse : VKResponse<UsersPage>()

data class UsersPage(
    @SerializedName("response")
    var users : List<User>
)
