package com.horizontal.test.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_character")
data class FavoriteCharacterModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val gender: String?,
    val image: String?,
    val name: String?,
    val status: String?,
    val url: String?
)
