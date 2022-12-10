package com.horizontal.test.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_remote_keys_table")
data class CharacterRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?,
) : java.io.Serializable