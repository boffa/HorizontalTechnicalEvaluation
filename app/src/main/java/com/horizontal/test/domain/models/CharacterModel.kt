package com.horizontal.test.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_table")
data class CharacterModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val gender: String?,
    val image: String?,
    val name: String?,
    val status: String?,
    val url: String?
)
