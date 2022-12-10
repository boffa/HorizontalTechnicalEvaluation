package com.horizontal.test.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.horizontal.test.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    lateinit var recyclerAdapter: MainRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeObservers()
    }


    private fun initRecyclerView() {

        binding.movieRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            recyclerAdapter =
                MainRecyclerAdapter(requireContext()) { characterId: Int ->
                    findNavController().navigate(
                        MainFragmentDirections
                            .actionMainFragmentToDetailsFragment(characterId = characterId)
                    )
                }

            adapter = recyclerAdapter
        }

        binding.movieRecyclerView.adapter = recyclerAdapter.withLoadStateFooter(
            footer = MainLoadingStateAdapter(recyclerAdapter::retry)
        )

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            recyclerAdapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.Loading) {

                    if (recyclerAdapter.snapshot().isEmpty()) {
                        binding.progressBar.isVisible = true
                    }
                    binding.txtError.isVisible = false
                    binding.btnRetry.isVisible = false
                } else {
                    binding.progressBar.isVisible = false

                    val error = when {
                        loadState.source.prepend is LoadState.Error -> loadState.source.prepend as LoadState.Error
                        loadState.source.append is LoadState.Error -> loadState.source.append as LoadState.Error
                        loadState.source.refresh is LoadState.Error -> loadState.source.refresh as LoadState.Error
                        else -> null
                    }
                    error?.let {
                        if (recyclerAdapter.snapshot().isEmpty()) {
                            binding.txtError.isVisible = true
                            binding.btnRetry.isVisible = true
                            binding.txtError.text = "Check Network Connection!"
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            recyclerAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.characters.collectLatest {
                if (it != null)
                    recyclerAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun retry() {
        binding.btnRetry.setOnClickListener { recyclerAdapter.retry() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}