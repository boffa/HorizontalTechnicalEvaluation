package com.horizontal.test.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.horizontal.test.domain.models.FavoriteCharacterModel

@Dao
interface FavoriteCharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(favoriteMovie: FavoriteCharacterModel)

    @Query("SELECT * FROM favorite_character")
    fun getFavoriteCharacter(): LiveData<List<FavoriteCharacterModel>>

    @Query("SELECT count(*) FROM favorite_character WHERE favorite_character.id = :id")
    suspend fun checkCharacter(id: String): Int

    @Query("DELETE FROM favorite_character WHERE favorite_character.id = :id" )
    suspend fun removeFromFavorite(id: String) : Int
}