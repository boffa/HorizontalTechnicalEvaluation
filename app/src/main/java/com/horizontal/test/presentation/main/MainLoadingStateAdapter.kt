package com.horizontal.test.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.horizontal.test.R
import com.horizontal.test.databinding.MainLoadingStateItemBinding

class MainLoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<MainLoadingStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ) = LoadStateViewHolder(parent,retry)


    override fun onBindViewHolder(
        holder: LoadStateViewHolder, loadState: LoadState
    ) = holder.bind(loadState)

    inner class LoadStateViewHolder(
        parent: ViewGroup,
        retry: () -> Unit
        ): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.main_loading_state_item,parent,false)
        ) {

        private val binding = MainLoadingStateItemBinding.bind(itemView)
        private val retry: Button = binding.btnRetry
            .also {
                it.setOnClickListener { retry() }
            }

            fun bind(loadState: LoadState) {

                if (loadState is LoadState.Error) {
                    binding.txtError.text = "Check Network Connection!"
                }
                binding.progressBar.isVisible = loadState is LoadState.Loading
                binding.btnRetry.isVisible = loadState is LoadState.Error
                binding.txtError.isVisible = loadState is LoadState.Error
            }
        }
}