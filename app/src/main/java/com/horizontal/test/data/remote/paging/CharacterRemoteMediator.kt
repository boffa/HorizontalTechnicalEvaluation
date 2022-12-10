package com.horizontal.test.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.horizontal.test.data.local.CharacterDatabase
import com.horizontal.test.data.remote.TestApi
import com.horizontal.test.data.remote.response.character.toCharacter
import com.horizontal.test.domain.models.CharacterModel
import com.horizontal.test.domain.models.CharacterRemoteKeys

import javax.inject.Inject

@ExperimentalPagingApi
class CharacterRemoteMediator @Inject constructor(
    private val characterApi: TestApi,
    private val characterDatabase: CharacterDatabase,
) : RemoteMediator<Int, CharacterModel>() {

    private val characterDao = characterDatabase.characterDao()
    private val characterRemoteKeys = characterDatabase.characterRemoteKeys()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterModel>,
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response =
                characterApi.getCharacterList(page = currentPage).results.map { it.toCharacter() }
            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            characterDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDao.deleteAllCharacters()
                    characterRemoteKeys.deleteAllRemoteKeys()
                }
                val keys = response.map { character ->
                    CharacterRemoteKeys(
                        id = character.id.toString(),
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                characterRemoteKeys.addAllRemoteKeys(remoteKeys = keys)
                characterDao.addCharacter(images = response)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharacterModel>,
    ): CharacterRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                characterRemoteKeys.getRemoteKeys(id = id.toString())
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, CharacterModel>,
    ): CharacterRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                characterRemoteKeys.getRemoteKeys(id = character.id.toString())
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, CharacterModel>,
    ): CharacterRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                characterRemoteKeys.getRemoteKeys(id = movie.id.toString())
            }
    }
}