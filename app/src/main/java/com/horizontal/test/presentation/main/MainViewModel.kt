package com.horizontal.test.presentation.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.horizontal.test.domain.models.CharacterModel
import com.horizontal.test.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _characters: MutableStateFlow<PagingData<CharacterModel>?> = MutableStateFlow(null)
    val characters: StateFlow<PagingData<CharacterModel>?> = _characters

    init {
        getAllCharacters()
    }

    private fun getAllCharacters() {
        viewModelScope.launch {
            repository
                .getAllCharacters()
                .cachedIn(viewModelScope)
                .collect { _characters.value = it }
        }
    }
}