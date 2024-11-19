package com.temalu.findfilm

import android.os.Bundle
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
    }

    private fun setFilmsDetails() {
        //Получаем наш фильм из переданного бандла
        val film = arguments?.get("film") as Film

        //Устанавливаем заголовок
        detailsBinding.detailsToolbar.title = film.title
        //Устанавливаем картинку
        detailsBinding.detailsPoster.setImageResource(film.poster)
        //Устанавливаем описание
        detailsBinding.detailsDescription.text = film.description
    }

    private fun initSnackbar(){
        val snackbarLike = Snackbar.make(detailsBinding.main, "Нравится!", Snackbar.LENGTH_SHORT)
        detailsBinding.detailsFabLike.setOnClickListener { snackbarLike.show() }

        val snackbarShare = Snackbar.make(detailsBinding.main, "Поделиться", Snackbar.LENGTH_SHORT)
        detailsBinding.detailsFabShare.setOnClickListener { snackbarShare.show() }

        val snackbarLater = Snackbar.make(detailsBinding.main, "Посмотреть позже", Snackbar.LENGTH_SHORT)
        detailsBinding.detailsFabLater.setOnClickListener { snackbarLater.show() }
    }
}