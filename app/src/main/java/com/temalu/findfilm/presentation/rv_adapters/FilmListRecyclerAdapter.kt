package com.temalu.findfilm.presentation.rv_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.temalu.findfilm.R
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.databinding.FilmItemBinding
import com.temalu.findfilm.presentation.rv_viewholders.FilmViewHolder

class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<ViewHolder>() {

    private var items = listOf<Film>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FilmItemBinding.inflate(
            LayoutInflater
                .from(parent.context),
            parent,
            false
        )
        return FilmViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                holder.bind(items[position])
                holder.itemView.setOnClickListener {
                    clickListener.click(items[position])
                }
                //добавление анимации появления рейтинга
                holder.filmItem.ratingDonut.startAnimation(
                    AnimationUtils.loadAnimation(
                        holder.filmItem.ratingDonut.context,
                        R.anim.for_visible_raiting_film_item
                    )
                )
            }
        }
    }

    fun addItems(newList: List<Film>, isSearch: Boolean = false) {
        val mergedList = if (isSearch) newList else (items + newList)
        val diff = DiffUtilFilms(items, mergedList)
        val diffResult = DiffUtil.calculateDiff(diff)

        items = mergedList
        diffResult.dispatchUpdatesTo(this@FilmListRecyclerAdapter)
    }

    interface OnItemClickListener {
        fun click(film: Film)
    }
}