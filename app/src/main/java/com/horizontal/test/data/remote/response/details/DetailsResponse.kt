package com.horizontal.test.data.remote.response.details

import com.horizontal.test.data.remote.response.character.CharacterInfo
import com.horizontal.test.domain.models.CharacterModel

data class DetailsResponse(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String

)

fun DetailsResponse.toCharacterDetails(): CharacterModel {
    return CharacterModel(
        id  = id,
        gender = gender,
        image = image,
        name = name,
        status = status,
        url = url
    )
}