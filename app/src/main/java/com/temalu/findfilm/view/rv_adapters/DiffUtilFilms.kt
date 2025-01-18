package com.temalu.findfilm.view.rv_adapters

import androidx.recyclerview.widget.DiffUtil
import com.temalu.findfilm.domain.Film

class DiffUtilFilms(private val oldList: List<Film>, private val newList: List<Film>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: Film = oldList[oldItemPosition]
        val new: Film = newList[newItemPosition]
        return old.description == new.description &&
                old.poster == new.poster
    }
}