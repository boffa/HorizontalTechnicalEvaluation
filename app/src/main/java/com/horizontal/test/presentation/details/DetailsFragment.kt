package com.horizontal.test.presentation.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.horizontal.test.core.Status
import com.horizontal.test.databinding.FragmentDetailsBinding
import com.horizontal.test.domain.models.CharacterModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailsFragment : Fragment() {

private lateinit var binding: FragmentDetailsBinding
private val viewModel: DetailsViewModel by viewModels()
private val args: DetailsFragmentArgs by navArgs()
private  lateinit var  characterModel: CharacterModel
private var _isChecked = false

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    binding = FragmentDetailsBinding.inflate(layoutInflater)
    return binding.root
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // initToolbar()
    subscribeObservers()
    retry()



   binding.toggleFavorite.setOnClickListener {
        _isChecked = !_isChecked
        if (_isChecked){
            viewModel.addToFavorite(characterModel)
        } else{
            viewModel.removeFromFavorite(characterModel.id.toString())
        }
        binding.toggleFavorite.isChecked = _isChecked
    }

}


private fun subscribeObservers() {
    viewModel.setCharacterId(args.characterId)
    lifecycleScope.launch { viewModel.characterDetails.collectLatest {
            result ->
        when (result?.status) {
            Status.LOADING -> {
                binding.progressBar.isVisible = true
            }
            Status.SUCCESS -> {
                binding.progressBar.isVisible = false
                binding.coordinatorLayout.isVisible = true
                binding.txtError.isVisible = false
                binding.btnRetry.isVisible = false
                result.data?.let { characterModel ->

                    this@DetailsFragment.characterModel = characterModel
                    CoroutineScope(Dispatchers.IO).launch{
                        val count = viewModel.checkCharacter(characterModel.id.toString())
                        withContext(Dispatchers.Main){
                            if (count > 0){
                                binding.toggleFavorite.isChecked = true
                                _isChecked = true
                            }else{
                                binding.toggleFavorite.isChecked = false
                                _isChecked = false
                            }

                        }
                    }

                    setMovieProperties(
                        characterModel.name,
                        characterModel.status,
                        characterModel.gender,
                        characterModel.image
                    )
                }
            }
            Status.ERROR -> {
                binding.coordinatorLayout.isVisible = false
                binding.progressBar.isVisible = false
                binding.txtError.isVisible = true
                binding.btnRetry.isVisible = true
                binding.txtError.text = "Check Network Connection!"
            }
            else -> {}
        }
    }
    }
}

private fun setMovieProperties(
    title: String?, status: String?, gender: String?, posterPath: String?
) {
    binding.name.text = "Title : $title"
    binding.status.text = "Status : $status"
    binding.gender.text = "Gender : $gender"
    binding.image.load(posterPath)


}

private fun retry() {
    binding.btnRetry.also {
        it.setOnClickListener { subscribeObservers() }
    }
}
}
