package com.temalu.findfilm

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.temalu.findfilm.databinding.FilmItemBinding

class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                //Вызываем метод bind(), который мы создали, и передаем туда объект
                //из нашей базы данных с указанием позиции
                holder.bind(items[position])
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //например, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                holder.itemView.setOnClickListener {
                    clickListener.click(items[position])
                }
                //добавление анимации появления рейтинга
                holder.filmItem.ratingDonut.startAnimation(AnimationUtils.loadAnimation(holder.filmItem.ratingDonut.context,R.anim.for_visible_raiting_film_item))
            }
        }
    }

    fun addItems(list: List<Film>) {
        val diff = FilmDiff(items , list)           //сравниваем список который был в FilmListRecyclerAdapter с тем что пришёл из ???базы???
        val diffResult = DiffUtil.calculateDiff(diff)
        items = list                                //обновляем "наш" список
        diffResult.dispatchUpdatesTo(this@FilmListRecyclerAdapter)
    }

    interface OnItemClickListener {
        fun click(film: Film)
    }
}