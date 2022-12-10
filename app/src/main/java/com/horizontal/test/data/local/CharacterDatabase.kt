package com.horizontal.test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.horizontal.test.domain.models.CharacterModel
import com.horizontal.test.domain.models.CharacterRemoteKeys
import com.horizontal.test.domain.models.FavoriteCharacterModel

@Database(entities = [CharacterModel::class, CharacterRemoteKeys::class,FavoriteCharacterModel::class] , version = 2)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterRemoteKeys(): CharacterRemoteKeysDao
    abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}