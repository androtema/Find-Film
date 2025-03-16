package com.temalu.findfilm.presentation.rv_viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.temalu.findfilm.data.tmdb.API_TMDB
import com.temalu.findfilm.databinding.FilmItemBinding
import com.temalu.findfilm.data.entity.Film

class FilmViewHolder(val filmItem: FilmItemBinding) : RecyclerView.ViewHolder(filmItem.root) {
    private val title = filmItem.title
    private val poster = filmItem.poster
    private val description = filmItem.description
    private val ratingDonut = filmItem.ratingDonut

    fun bind(film : Film){
        title.text = film.title

        Glide.with(itemView)
            .load(API_TMDB.IMAGES_URL + "w200" + film.poster)
            .centerCrop()
            .into(poster)

        description.text = film.description

        ratingDonut.setProgress((film.rating * 10).toInt())
    }
}