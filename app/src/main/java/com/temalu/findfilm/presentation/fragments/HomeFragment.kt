package com.temalu.findfilm.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.temalu.findfilm.R
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.databinding.FragmentHomeBinding
import com.temalu.findfilm.presentation.MainActivity
import com.temalu.findfilm.presentation.rv_adapters.FilmListRecyclerAdapter
import com.temalu.findfilm.presentation.rv_adapters.TopSpacingItemDecoration
import com.temalu.findfilm.presentation.utils.AnimationHelper
import com.temalu.findfilm.presentation.viewmodel.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.internal.util.NotificationLite.disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var mainRecycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private val bindingHomeFragment: FragmentHomeBinding get() = _bindingHomeFragment!!
    private var _bindingHomeFragment: FragmentHomeBinding? = null

    private val TOTAL_PAGES = 500
    private var currentPage = 1
    private var isLoading = false
    private var firstStart = true
    private var scene: Scene? = null

    private val disposable = CompositeDisposable()

    private val viewModel: HomeFragmentViewModel by viewModels()

    var filmsDataBase = listOf<Film>()
        set(value) {
            if (field == value) return
            field = value
            filmsAdapter.addItems(field)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingHomeFragment = FragmentHomeBinding.inflate(layoutInflater)
        return bindingHomeFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scene = addScene()
        workWithViewModel()
        addAnimation(scene)
        addRecyclerAndDecorator(scene)
        recyclerViewSetScroollListener()
        initSearchView(scene)
    }

    private fun workWithViewModel() {
        viewModel.loadPage(currentPage)

        // Подписка на список фильмов
        viewModel.filmsListObservable
            .subscribe(
                { films ->
                    Log.d("filmsListObservable","Films in Fragment: $films")
                    filmsDataBase = films
                    filmsAdapter.addItems(films)
                },
                { error -> Log.e("HomeFragment", "Error: ${error.message}") }
            ).also { disposable.add(it) }

        // Подписка на состояние ProgressBar
        viewModel.showProgressBar
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { isLoading ->
                    bindingHomeFragment.homeFragmentRoot
                        .findViewById<ProgressBar>(R.id.progress_bar)
                        .isVisible = isLoading
                },
                { error ->
                    Log.e("HomeFragment", "Error with progress bar: ${error.message}")
                }
            ).also { disposable.add(it) }

        // Подписка на события тоста
        viewModel.toastEvent
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                },
                { error ->
                    Log.e("HomeFragment", "Error with toast: ${error.message}")
                }
            ).also { disposable.add(it) }
    }

    private fun addScene(): Scene {
        if (scene == null) {
            scene = Scene.getSceneForLayout(
                bindingHomeFragment.homeFragmentRoot,
                R.layout.merge_home_screen_content,
                requireContext()
            )
        }
        return scene!!
    }

    private fun addRecyclerAndDecorator(scene: Scene) {
        mainRecycler = scene.sceneRoot.findViewById(R.id.main_recycler)
        mainRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(5)
            addItemDecoration(decorator)
        }
    }

    private fun addAnimation(scene: Scene) {
        AnimationHelper.performFragmentCircularRevealAnimation(
            bindingHomeFragment.homeFragmentRoot,
            requireActivity(),
            1
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
    }

    // слушатель скролла для реализации подгрузки контента
    private fun recyclerViewSetScroollListener() {
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as RecyclerView.LayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItems = (recyclerView.layoutManager as LinearLayoutManager)
                    .findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItems >= totalItemCount - 15) {
                    if (!isLoading && currentPage <= TOTAL_PAGES) {
                        isLoading = true
                        currentPage++
                        downloadNextPage(currentPage)
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

            override fun onQueryTextChange(newText: String?): Boolean {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    val result = withContext(Dispatchers.Default) {
                        filmsDataBase.filter {
                            it.title
                                .lowercase(Locale.getDefault())
                                .contains(newText?.lowercase(Locale.getDefault()) ?: "")
                        }
                    }
                    filmsAdapter.addItems(result)
                }
                return true
            }
        })
    }

    private fun downloadNextPage(page: Int) {
        viewModel.loadPage(page).also {
            isLoading = false // После загрузки данных сбрасываем флаг
        }
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
        _bindingHomeFragment = null
    }
}