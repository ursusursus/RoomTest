package eu.activstar.ursus.myapplication

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.bottomnavigation.LabelVisibilityMode
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()
    private val pageRelay = BehaviorRelay.create<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myDatabase = Room.databaseBuilder(this, MyDatabase::class.java, "mydb").build()
        val articleDao = myDatabase.articleDao();

        val paginationManager = PaginationManager()

        findViewById<View>(R.id.button).setOnClickListener {
            Completable
                    .fromAction { articleDao.insert(Article(0, "title1", "desc1", "imageUrl1", "url1")) }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        }

        findViewById<View>(R.id.button3).setOnClickListener {
            //            Completable
//                    .fromAction { articleDao.updateTimestampsRandom() }
//                    .subscribeOn(Schedulers.io())
//                    .subscribe()
            pageRelay.accept(Unit)
        }

        val adapter = ArticlesAdapter(this, FooDiffUtilCallback(), object : ArticlesAdapter.OnArticleClickListener {
            override fun onArticleClicked(article: Article) {

            }

            override fun onArticleRemove(article: Article) {
                Completable
                        .fromAction { articleDao.delete(article) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
            }
        })


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val listEndScrolledListener = object : ListEndScrollListener() {
            override fun onScrolledToEnd() {
                paginationManager.nextPage(adapter.itemCount)
            }
        }
        listEndScrolledListener.attachToRecyclerView(recyclerView, null)


//        disposables += paginationManager.currentPageObservable()
//                .toFlowable(BackpressureStrategy.LATEST)
//                .switchMap { pageIndex ->
//                    Log.d("Default", "nextPage=$pageIndex");
//                    articleDao.findAll(PaginationManager.PAGE_SIZE * (pageIndex + 1))
//                }
//                .subscribeOn(Schedulers.io())
//                .diffWithLast { old, new -> FooDiffUtilCallback(old, new) }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { diffedList ->
//                    Log.d("Default", "size=${diffedList.list.size}");
//                    for (article in it) {
//                        Log.d("Default", "${article.id} - t=${article.addedAt}");
//                    }
//                    val recyclerViewState = recyclerView.layoutManager.onSaveInstanceState()

//                    adapter.setArticles(diffedList.list)
//                    diffedList.diff.dispatchUpdatesTo(adapter)

//                    recyclerView.layoutManager.onRestoreInstanceState(recyclerViewState)
//                }

//        val pagedList = PagedList.Builder<Int, Article>(dataSourceFactory.create(), PaginationManager.PAGE_SIZE)
//                .build()

//        val dataSourceFactory = articleDao.findAll3()
//        val what = LivePagedListBuilder(dataSourceFactory, PaginationManager.PAGE_SIZE)
//                .build();
//        what.observe(this, Observer {
//            it?.run {
//                Log.d("Default", "${it.size}")
//                adapter.submitList(it)
//            }
//        })

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_favorites -> {
                }
                R.id.action_schedules -> {
                }
                R.id.action_music -> {
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        pageRelay
                .concatMap {
                    Observable.fromCallable {
                        val nextInt = Random().nextInt(2000)
                        Log.d("Default", "waiting for=$nextInt");
                        Thread.sleep(nextInt.toLong())
                        return@fromCallable nextInt
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("Default", "what=$it");
                })

//        val dataSourceFactory = articleDao.findAll3()
//        val what = LivePagedListBuilder(
//                dataSourceFactory,
//                PagedList.Config.Builder()
//                        .setEnablePlaceholders(true)
//                        .setPageSize(5)
//                        .build())
//                .build()
//
//        what.observe(this, Observer {
//            it?.run {
//                Log.d("Default", "${it.size}")
//                adapter.submitList(it)
//            }
//        })


    }

}

fun <T> Observable<T>.throwingSubscribe(function: (T) -> Unit): Disposable {
    return subscribe(function, { throw RuntimeException(it) })
}

fun <T> Flowable<T>.throwingSubscribe(function: (T) -> Unit): Disposable {
    return subscribe(function, { throw RuntimeException(it) })
}
//
//fun wrapAndThrow(t: Throwable) {
//    throw RuntimeException(t)
//}

operator fun CompositeDisposable.plusAssign(d: Disposable) {
    add(d)
}

//class FooDiffUtilCallback(
//        private val oldList: List<Article>,
//        private val newList: List<Article>
//) : DiffUtil.Callback() {
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id == newList[newItemPosition].id
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
//
//    override fun getOldListSize() = oldList.size
//
//    override fun getNewListSize() = newList.size
//
//}

class FooDiffUtilCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}

fun <T> Flowable<List<T>>.diffWithLast(differ: (old: List<T>, new: List<T>) -> DiffUtil.Callback): Flowable<DiffedList<T>> {
    data class Tuple(var old: List<T>, var new: List<T>, var results: DiffUtil.DiffResult?)
    return scan<Tuple>(
            Tuple(emptyList(), emptyList(), null),
            { tuple, newList ->
                tuple.old = tuple.new
                tuple.new = newList
                tuple.results = DiffUtil.calculateDiff(differ(tuple.old, tuple.new))
                tuple
            })
            .skip(1)
            .map {
                DiffedList(it.new, it.results!!)
            }
}

data class DiffedList<T>(val list: List<T>, val diff: DiffUtil.DiffResult)

