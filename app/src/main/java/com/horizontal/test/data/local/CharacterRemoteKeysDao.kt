package com.horizontal.test.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.horizontal.test.domain.models.CharacterRemoteKeys

@Dao
interface CharacterRemoteKeysDao {

    @Query("SELECT * FROM character_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: String) : CharacterRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<CharacterRemoteKeys>)

    @Query("DELETE FROM character_remote_keys_table")
    suspend fun deleteAllRemoteKeys()
}