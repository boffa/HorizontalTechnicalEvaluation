package com.horizontal.test.data.remote

import com.horizontal.test.data.remote.response.character.CharacterInfo
import com.horizontal.test.data.remote.response.character.CharacterResponse
import com.horizontal.test.data.remote.response.details.DetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TestApi {

    @GET("character")
    suspend fun getCharacterList(
        @Query("page") page: Int
    ): CharacterResponse


    @GET("character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") characterId: Int,
    ): DetailsResponse
}