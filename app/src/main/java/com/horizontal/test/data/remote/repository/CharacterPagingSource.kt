package com.horizontal.test.data.remote.repository

import androidx.datastore.preferences.protobuf.Api
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.PagingSource.LoadResult.Page
import com.horizontal.test.data.remote.TestApi
import com.horizontal.test.data.remote.response.character.toCharacter
import com.horizontal.test.domain.models.CharacterModel


import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharacterPagingSource @Inject
constructor(
    private val api: TestApi
    ) : PagingSource<Int, CharacterModel>(){

    override fun getRefreshKey(state: PagingState<Int, CharacterModel>): Int? {
        return state.anchorPosition?.let {
            //state.closestPageToPosition(it)?.prevKey
                anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {
        return try {
            //val page = if (params is Append) params.key else null
            val page = params.key ?: 1

            val response = api.getCharacterList(page).results.map { it.toCharacter() }
            Page (
                data = response,
                prevKey = null,
                nextKey = page + 1
            )
        } catch (e : IOException) {
            LoadResult.Error(e)
        } catch (e : HttpException) {
            LoadResult.Error(e)
        }
    }


}