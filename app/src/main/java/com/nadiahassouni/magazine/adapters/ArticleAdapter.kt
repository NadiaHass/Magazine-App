package com.nadiahassouni.magazine.adapters

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.model.Article

class ArticleAdapter (
    private val context: Context,
    private val articlesList: ArrayList<Article> ,
    private val fragment : String)
    : RecyclerView.Adapter<ArticleAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tv_title)
        var tvDate: TextView = view.findViewById(R.id.tv_date)
        var tvCategory: TextView = view.findViewById(R.id.tv_category)
        var imageView: ImageView = view.findViewById(R.id.iv_article)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_rv_item , parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.tvTitle.text = articlesList[position].title
        holder.tvCategory.text = articlesList[position].category
        holder.tvDate.text = articlesList[position].date
        Glide.with(context)
            .load(Uri.parse(articlesList[position].imageUrl))
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("article" , articlesList.get(position))
            try {
                when (fragment){
                    "articles" ->{
                        Navigation.findNavController(holder.itemView).navigate(R.id.action_articlesFragment_to_articleFragment , bundle)
                    }
                    "categories"->{
                    Navigation.findNavController(holder.itemView).navigate(R.id.action_categoryArticlesFragment_to_articleFragment , bundle)

                    }
                }
            }catch (e : Exception){

            }
        }
    }

    override fun getItemCount()= articlesList.size

}