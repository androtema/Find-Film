package com.temalu.findfilm

import android.os.Bundle
import androidx.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.TransitionSet
import com.temalu.findfilm.databinding.FragmentHomeBinding
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var bindingHomeFragment: FragmentHomeBinding
    private lateinit var searchView: SearchView
    private lateinit var mainRecycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    val filmsDataBase: MutableList<Film> = mutableListOf(
        Film(
            "Матрица",
            R.drawable.matrix,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.",
            4.5f
        ),
        Film(
            "Рокки",
            R.drawable.rocky,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.",
            7.5f
        ),
        Film(
            "Индиана Джонс",
            R.drawable.raiders,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            9.5f
        ),
        Film(
            "Матрица2",
            R.drawable.matrix,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            0.5f),
        Film(
            "Ла Ла Лэнд",
            R.drawable.lalaland,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            5.5f),
        Film(
            "Мононоке",
            R.drawable.mononoke,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            3.5f),
        Film(
            "Бэтмен",
            R.drawable.darknight,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            8.5f),
        Film(
            "Один дома",
            R.drawable.home_alone,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            3.5f),
        Film(
            "Матрица3",
            R.drawable.matrix,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            7.5f),
        Film(
            "Интерстеллар",
            R.drawable.interstellar,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            6.5f),
        Film(
            "Властелин колец",
            R.drawable.lord_rings,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            2.5f),
        Film(
            "Матрица4",
            R.drawable.matrix,
            "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker."
            ,
            1.5f),
    )
    var firstStart = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingHomeFragment = FragmentHomeBinding.inflate(layoutInflater)
        return bindingHomeFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(
            bindingHomeFragment.homeFragmentRoot,
            requireActivity(),
            1
        )

        //добавление анимации появления home fragment
        //fragment_home - пустой экран (Scene 0) -> merge_home... (Scene 1) - содержимое экрана появляется
        val scene = Scene.getSceneForLayout(
            bindingHomeFragment.homeFragmentRoot,
            R.layout.merge_home_screen_content,
            requireContext()
        )

        if (firstStart) {
            val searchSlide = Slide(Gravity.TOP).addTarget(R.id.search_view)
            val recyclerSlide = Slide(Gravity.BOTTOM).addTarget(R.id.main_recycler)
            val customTransition = TransitionSet().apply {
                duration = 500
                addTransition(recyclerSlide)
                addTransition(searchSlide)
            }
            TransitionManager.go(scene, customTransition)
            firstStart = false
        } else {
            TransitionManager.go(scene)
        }

        searchView =
            scene.sceneRoot.findViewById(R.id.search_view)         //т.к. добавляем не ViewGroup, а layout, то наполняем View'хами по отдельности
        mainRecycler = scene.sceneRoot.findViewById(R.id.main_recycler)

        //находим наш RV
        mainRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(5)
            addItemDecoration(decorator)
        }
        //Кладем нашу БД в RV
        filmsAdapter.addItems(filmsDataBase)


        //чтобы нажимать на всю площадь вью поиска
        searchView.setOnClickListener {
            searchView.isIconified = false
        }


        //Подключаем слушателя изменений введенного текста в поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String?): Boolean {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText != null) {
                    if (newText.isEmpty()) {
                        filmsAdapter.addItems(filmsDataBase)
                        return true
                    }
                }
                //Фильтруем список на поискк подходящих сочетаний
                val result = filmsDataBase.filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.lowercase(Locale.getDefault())
                        .contains(newText!!.lowercase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                filmsAdapter.addItems(result)
                return true
            }
        })

    }
}