package com.horizontal.test.data.remote.response.character

import com.google.gson.annotations.SerializedName
import com.horizontal.test.data.remote.response.base.PageInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class CharacterResponse(
    @SerializedName("info") val pageInfo: PageInfo?,
    @SerializedName("results") val results: List<CharacterInfo>
)
