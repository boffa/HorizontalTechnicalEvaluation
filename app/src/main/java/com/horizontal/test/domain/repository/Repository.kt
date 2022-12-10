package com.horizontal.test.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.horizontal.test.core.Resource
import com.horizontal.test.domain.models.CharacterModel
import com.horizontal.test.domain.models.FavoriteCharacterModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllCharacters(): Flow<PagingData<CharacterModel>>
    fun getCharacterDetails(characterId: Int): Flow<Resource<CharacterModel>>
    suspend fun addToFavorite(favoriteMovie: FavoriteCharacterModel)
    fun getFavorite(): LiveData<List<FavoriteCharacterModel>>
    suspend fun checkCharacter(id: String): Int
    suspend fun removeFromFavorite(id: String)
}