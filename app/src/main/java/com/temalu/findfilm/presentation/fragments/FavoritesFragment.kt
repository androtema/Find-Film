package com.temalu.findfilm.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.temalu.findfilm.presentation.rv_adapters.FilmListRecyclerAdapter
import com.temalu.findfilm.presentation.rv_adapters.TopSpacingItemDecoration
import com.temalu.findfilm.databinding.FragmentFavoritesBinding
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.presentation.utils.AnimationHelper
import com.temalu.findfilm.presentation.MainActivity

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(binding.favoriteRoot, requireActivity(), 2)

        //ВЫЗОВ ФРАГМЕНТА ДЕТАЛЕЙ ФИЛЬМА
        binding.favoritesRecycler.apply {
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(5)
            addItemDecoration(decorator)
        }
        //Кладем нашу БД в RV
        filmsAdapter.addItems(MainActivity.favoritesList)
    }
}