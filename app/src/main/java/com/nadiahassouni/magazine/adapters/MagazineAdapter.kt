package com.nadiahassouni.magazine.adapters

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.model.Article

class MagazineAdapter(
    private val context: Context,
    private val articlesList: ArrayList<Article>)
    : RecyclerView.Adapter<MagazineAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDate: TextView = view.findViewById(R.id.tv_date)
        var tvTitle: TextView = view.findViewById(R.id.tv_title)
        var imageView: ImageView = view.findViewById(R.id.iv_article)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.magazine_rv_item , parent, false)
        return MagazineAdapter.ItemViewHolder(adapterLayout)
    }

    override fun getItemCount()= articlesList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.tvDate.text = articlesList[position].date
        holder.tvTitle.text = articlesList[position].title
        Glide.with(context)
            .load(Uri.parse(articlesList[position].imageUrl))
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("article" , articlesList.get(position))
            Navigation.findNavController(holder.itemView).navigate(R.id.action_magazineFragment_to_articleFragment , bundle)

        }

    }

}