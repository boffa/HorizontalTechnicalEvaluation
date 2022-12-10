package com.horizontal.test.data.remote.response.character

import com.google.gson.annotations.SerializedName
import com.horizontal.test.domain.models.CharacterModel

data class CharacterInfo(
    val created: String?,
    @SerializedName("episode") val episodes: List<String>?,
    @SerializedName( "gender") val gender: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("image") val image: String?,
    @SerializedName( "name") val name: String?,
    @SerializedName( "origin") val origin: Origin?,
    @SerializedName( "species") val species: String?,
    @SerializedName( "status") val status: String?,
    @SerializedName( "type") val type: String?,
    @SerializedName( "url") val url: String?


)
fun CharacterInfo.toCharacter(): CharacterModel {
    return CharacterModel(
        id  = id,
        gender = gender,
        image = image,
        name = name,
        status = status,
        url = url
    )
}