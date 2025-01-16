package com.temalu.findfilm.view.rv_viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.temalu.findfilm.databinding.FilmItemBinding
import com.temalu.findfilm.domain.Film

class FilmViewHolder(val filmItem: FilmItemBinding) : RecyclerView.ViewHolder(filmItem.root) {
    private val title = filmItem.title
    private val poster = filmItem.poster
    private val description = filmItem.description
    private val ratingDonut = filmItem.ratingDonut

    fun bind(film : Film){
        title.text = film.title

        Glide.with(itemView)
            .load(film.poster)      //загружаем картинку из film в poster(вьюхолдера)
            .centerCrop()
            .into(poster)

        description.text = film.description

        ratingDonut.setProgress((film.rating * 10).toInt())
    }
}