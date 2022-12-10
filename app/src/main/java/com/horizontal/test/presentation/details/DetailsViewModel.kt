package com.horizontal.test.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizontal.test.core.Resource
import com.horizontal.test.domain.models.CharacterModel
import com.horizontal.test.domain.models.FavoriteCharacterModel
import com.horizontal.test.domain.repository.Repository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    private val _characterDetails: MutableStateFlow<Resource<CharacterModel>?> = MutableStateFlow(null)
    val characterDetails: MutableStateFlow<Resource<CharacterModel>?> = _characterDetails

    fun setCharacterId(characterId: Int) {
        getCharacterDetails(characterId)
    }

    private fun getCharacterDetails(characterId: Int) {
        viewModelScope.launch {
            repository
                .getCharacterDetails(characterId)
                .collect {
                    _characterDetails.value = it
                }
        }
    }


    fun addToFavorite(character: CharacterModel){
        CoroutineScope(Dispatchers.IO).launch {
            repository.addToFavorite(
                FavoriteCharacterModel(
                    character.id,
                    character.gender,
                    character.image,
                    character.name,
                    character.status,
                    character.url,
                )
            )

        }
    }

    suspend fun checkCharacter(id: String) = repository.checkCharacter(id)

    fun removeFromFavorite(id: String){
        CoroutineScope(Dispatchers.IO).launch {
            repository.removeFromFavorite(id)
        }
    }

}
