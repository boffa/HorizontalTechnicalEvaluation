package com.horizontal.test.core

import androidx.recyclerview.widget.DiffUtil
import com.horizontal.test.domain.models.CharacterModel

object MainDiffCallback : DiffUtil.ItemCallback<CharacterModel>() {

    override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
        return oldItem == newItem
    }
}