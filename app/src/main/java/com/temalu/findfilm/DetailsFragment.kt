package com.temalu.findfilm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.temalu.findfilm.databinding.FragmentDetailsBinding
import com.temalu.findfilm.databinding.FragmentHomeBinding

class DetailsFragment : Fragment() {

    private lateinit var detailsBinding: FragmentDetailsBinding

    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailsBinding = FragmentDetailsBinding.inflate(layoutInflater)
        return detailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFilmsDetails()//добавляем данные фильма в активити
        initSnackbar()      //задаём snackbar для fab

        detailsBinding.detailsFabLike.setOnClickListener {
            if (!film.isInFavorites) {
                detailsBinding.detailsFabLike.setImageResource(R.drawable.round_favorite_24)
                film.isInFavorites = true
                MainActivity.favoritesList.add(film)
            } else {
                detailsBinding.detailsFabLike.setImageResource(R.drawable.favorite_add)
                film.isInFavorites = false
                MainActivity.favoritesList.remove(film)
            }
            Log.d("AAAAAAAAAAAAAAA", "" + film.isInFavorites)
            Log.d("AAAAAAAAAAAAAAA", "" + MainActivity.favoritesList.size)
        }

        //задаём действие кнопки ПОДЕЛИТЬСЯ
        detailsBinding.detailsFabShare.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Укзываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            //УКазываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    private fun setFilmsDetails() {
        //Получаем наш фильм из переданного бандла
        film = arguments?.get("film") as Film

        //Устанавливаем заголовок
        detailsBinding.detailsToolbar.title = film.title
        //Устанавливаем картинку
        detailsBinding.detailsPoster.setImageResource(film.poster)
        //Устанавливаем описание
        detailsBinding.detailsDescription.text = film.description

        detailsBinding.detailsFabLike.setImageResource(
            if (film.isInFavorites) R.drawable.round_favorite_24
            else R.drawable.favorite_add
        )
    }

    private fun initSnackbar() {
        val snackbarLike = Snackbar.make(detailsBinding.main, "Нравится!", Snackbar.LENGTH_SHORT)
        detailsBinding.detailsFabLike.setOnClickListener { snackbarLike.show() }

        val snackbarShare = Snackbar.make(detailsBinding.main, "Поделиться", Snackbar.LENGTH_SHORT)
        detailsBinding.detailsFabShare.setOnClickListener { snackbarShare.show() }

        val snackbarLater =
            Snackbar.make(detailsBinding.main, "Посмотреть позже", Snackbar.LENGTH_SHORT)
        detailsBinding.detailsFabLater.setOnClickListener { snackbarLater.show() }
    }
}