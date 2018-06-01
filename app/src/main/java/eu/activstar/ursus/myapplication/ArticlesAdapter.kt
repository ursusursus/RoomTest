package eu.activstar.ursus.myapplication

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 01-May-18.
 */
internal class ArticlesAdapter(
        context: Context,
        diffUtilCallback: DiffUtil.ItemCallback<Article>,
        private val listener: OnArticleClickListener)

    : PagedListAdapter<Article, ArticlesAdapter.ViewHolder>(diffUtilCallback) {

    internal interface OnArticleClickListener {
        fun onArticleClicked(article: Article)
        fun onArticleRemove(article: Article)
    }

    private val articles = mutableListOf<Article>()
    private val inflater = LayoutInflater.from(context);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_article, parent, false)
        return ViewHolder(view, object : ViewHolder.ClickListener() {
            override fun onClick(pos: Int) {
                val currentList = currentList
                if (currentList != null) {
                    val article = currentList[pos]
                    if (article != null) {
                        listener.onArticleClicked(article)
                    }
                }
            }

            override fun onLongClick(pos: Int) {
                val currentList = currentList
                if (currentList != null) {
                    val article = currentList[pos]
                    if (article != null) {
                        listener.onArticleRemove(article)
                    }
                }
            }
        })
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        article?.let { holder.bindTo(it) }
    }

    fun setArticles(article: List<Article>) {
        this.articles.clear()
        this.articles.addAll(article)
    }

//    override fun getItemCount() = articles.size

    class ViewHolder(view: View, private val listener: ClickListener) : RecyclerView.ViewHolder(view) {
        internal abstract class ClickListener {
            fun click(pos: Int) {
                if (pos != RecyclerView.NO_POSITION) {
                    onClick(pos)
                }
            }

            fun longClick(pos: Int) {
                if (pos != RecyclerView.NO_POSITION) {
                    onLongClick(pos)
                }
            }

            abstract fun onClick(pos: Int)

            abstract fun onLongClick(pos: Int)
        }

        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)

        init {
            itemView.setOnClickListener { listener.click(adapterPosition) }
            itemView.setOnLongClickListener {
                listener.longClick(adapterPosition)
                true
            }
        }

        fun bindTo(article: Article) {
            titleTextView.text = "${article.id} /// ${article.title}"
        }


    }
}