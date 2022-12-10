package com.horizontal.test.data.remote.response.character

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Origin(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?
)