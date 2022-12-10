package com.horizontal.test.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.horizontal.test.domain.models.CharacterModel

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character_table")
    fun getAllCharacters(): PagingSource<Int, CharacterModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacter(images: List<CharacterModel>)

    @Query("DELETE FROM character_table")
    suspend fun deleteAllCharacters()
}