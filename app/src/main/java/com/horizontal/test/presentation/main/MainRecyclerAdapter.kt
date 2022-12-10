package com.horizontal.test.presentation.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.horizontal.test.core.MainDiffCallback
import com.horizontal.test.databinding.MainItemBinding
import com.horizontal.test.domain.models.CharacterModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MainRecyclerAdapter
@Inject
constructor(
    @ApplicationContext val context: Context,
    private val clicked: (Int) -> Unit
) : PagingDataAdapter<CharacterModel, MainRecyclerAdapter.MainViewHolder>(
    MainDiffCallback
) {

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            MainItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class MainViewHolder(
        private val binding: MainItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CharacterModel?) {
            binding.characterName.text = data?.name.toString()
            binding.characterImg.load(data?.image)
            binding.root.setOnClickListener {
                data?.id?.let { id -> clicked.invoke(id) }
            }
        }

        private fun circularProgressBar(): CircularProgressDrawable {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 10f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.start()
            return circularProgressDrawable
        }
    }
}