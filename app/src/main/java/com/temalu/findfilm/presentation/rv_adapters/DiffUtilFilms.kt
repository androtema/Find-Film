package com.temalu.findfilm.presentation.rv_adapters

import androidx.recyclerview.widget.DiffUtil
import com.androtema.local.data.entity.Film

class DiffUtilFilms(private val oldList: List<Film>, private val newList: List<Film>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  oldList[oldItemPosition] == newList[newItemPosition]
    }
}