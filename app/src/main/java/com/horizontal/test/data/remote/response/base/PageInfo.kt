package com.horizontal.test.data.remote.response.base

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class PageInfo(
    @SerializedName( "count") val count: Int?,
    @SerializedName("next") val next: String?,
    @SerializedName("pages") val pages: Int?,
    @SerializedName("prev") val prev: String?
)