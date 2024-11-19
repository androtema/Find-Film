package com.temalu.findfilm

import android.view.View
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.RecyclerView
import com.temalu.findfilm.databinding.ActivityMainBinding
import com.temalu.findfilm.databinding.FilmItemBinding

class FilmViewHolder(val filmItem: FilmItemBinding) : RecyclerView.ViewHolder(filmItem.root) {
    private val title = filmItem.title
    private val poster = filmItem.poster
    private val description = filmItem.description

    fun bind(film : Film){
        title.text = film.title
        poster.setImageResource(film.poster)
        description.text = film.description
    }
}