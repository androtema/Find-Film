package com.temalu.findfilm.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.temalu.findfilm.R
import com.temalu.findfilm.databinding.FragmentHomeBinding
import com.temalu.findfilm.domain.Film
import com.temalu.findfilm.utils.AnimationHelper
import com.temalu.findfilm.view.MainActivity
import com.temalu.findfilm.view.rv_adapters.FilmListRecyclerAdapter
import com.temalu.findfilm.view.rv_adapters.TopSpacingItemDecoration
import com.temalu.findfilm.viewmodel.HomeFragmentViewModel
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var bindingHomeFragment: FragmentHomeBinding
    private lateinit var searchView: SearchView
    private lateinit var mainRecycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private val TOTAL_PAGES = 500
    private var currentPage = 1

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }


    var filmsDataBase = listOf<Film>()
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            //Обновляем RV адаптер
            filmsAdapter.addItems(field)
        }

    var firstStart = true   //для определения первого запуска приложения

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingHomeFragment = FragmentHomeBinding.inflate(layoutInflater)
        return bindingHomeFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadPage(currentPage)
        viewModel.filmsListLiveData.observe(viewLifecycleOwner, Observer<List<Film>> {
            filmsDataBase = it
        })

        AnimationHelper.performFragmentCircularRevealAnimation(
            bindingHomeFragment.homeFragmentRoot,
            requireActivity(),
            1
        )

        //добавление анимации появления home fragment
        //fragment_home - пустой экран, виден только Main Activity (Scene 0) -> merge_home... (Scene 1) - содержимое экрана появляется
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

        RecyclerViewSetScroollListener()
        initSearchView(scene)
    }

    // слушатель скролла для реализации подгрузки контента
    fun RecyclerViewSetScroollListener() {
        var isLoading = false //проверка не загружаем ли мы данные
        val scrollListener = object : RecyclerView.OnScrollListener() {
            @Override
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as RecyclerView.LayoutManager
                //смотрим сколько элементов на экране
                val visibleItemCount: Int = layoutManager.childCount
                //сколько всего элементов
                val totalItemCount: Int = layoutManager.itemCount
                //какая позиция первого элемента
                val firstVisibleItems = (recyclerView.layoutManager as LinearLayoutManager)
                    .findFirstVisibleItemPosition()
                //проверяем, грузим мы что-то или нет
                // определяем начало загрузки следующей страницы
                if (!isLoading && (visibleItemCount + firstVisibleItems >= totalItemCount - 7)) {
                    //ставим флаг, что мы запросили ещё элементы
                    isLoading = true
                    currentPage++
                    if (currentPage <= TOTAL_PAGES) {
                        // загрузка следующей страницы
                        downloadNextPage(currentPage)
                        // Оповещение RecyclerView об изменении данных с помощью DiffUtil
                        isLoading = false
                    }
                }
            }
        }
        mainRecycler.addOnScrollListener(scrollListener)
    }

    private fun initSearchView(scene: Scene) {
        //т.к. добавляем не ViewGroup, а layout, то наполняем View'хами по отдельности
        searchView = scene.sceneRoot.findViewById(R.id.search_view)

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

    private fun downloadNextPage(page: Int) {
        viewModel.loadPage(page) // Предполагается, что метод loadPage в ViewModel загружает данные
    }
}