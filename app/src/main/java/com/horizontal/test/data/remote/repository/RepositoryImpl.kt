package com.horizontal.test.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.horizontal.test.core.Resource
import com.horizontal.test.data.local.CharacterDatabase
import com.horizontal.test.data.remote.TestApi
import com.horizontal.test.data.remote.paging.CharacterRemoteMediator
import com.horizontal.test.data.remote.response.character.toCharacter
import com.horizontal.test.data.remote.response.details.toCharacterDetails
import com.horizontal.test.domain.models.CharacterModel
import com.horizontal.test.domain.models.FavoriteCharacterModel
import com.horizontal.test.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class RepositoryImpl @Inject constructor(
    private val api: TestApi,
    private val characterDatabase: CharacterDatabase
) : Repository {
    override fun getAllCharacters(): Flow<PagingData<CharacterModel>> {
        val pagingSourceFactory = { characterDatabase.characterDao().getAllCharacters() }
            return Pager(
                config = PagingConfig(pageSize = 10),
                remoteMediator = CharacterRemoteMediator(
                 characterApi = api,
                 characterDatabase = characterDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    override fun getCharacterDetails(characterId: Int): Flow<Resource<CharacterModel>> = flow {
        try {
            emit(Resource.loading<CharacterModel>(null))
            val response = api.getCharacterDetails(characterId).toCharacterDetails()
            emit(Resource.success<CharacterModel>(response))
        } catch (e: Exception) {
            emit(
                Resource.error<CharacterModel>(
                    e.localizedMessage ?: "Check Network Connection!",
                    null
                )
            )
        }
    }


    override suspend fun addToFavorite(favoriteCharacter: FavoriteCharacterModel) = characterDatabase.favoriteCharacterDao().addToFavorite(favoriteCharacter)
    override fun getFavorite() = characterDatabase.favoriteCharacterDao().getFavoriteCharacter()
    override suspend fun checkCharacter(id: String) = characterDatabase.favoriteCharacterDao().checkCharacter(id)
    override suspend fun removeFromFavorite(id: String) {
        characterDatabase.favoriteCharacterDao().removeFromFavorite(id)
    }
}