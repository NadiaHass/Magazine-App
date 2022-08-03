package com.nadiahassouni.magazine.adapters

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.model.Category

class CategoryAdapter(
    private val context : Context ,
    private val categoriesList : ArrayList<Category>
) : BaseAdapter(){
    override fun getCount(): Int {
        return categoriesList.size
    }

    override fun getItem(p0: Int): Any {
        return categoriesList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view : View = View.inflate(context , R.layout.category_gv_item , null)
        val image : ImageView = view.findViewById(R.id.iv_category)
        val title : TextView = view.findViewById(R.id.tv_title)

        title.bringToFront()

        Glide.with(context)
            .load(categoriesList[p0].imageUrl)
            .into(image)
        title.text = categoriesList[p0].title.toString()

        view.setOnClickListener {
            navigateToCategoryArticleFragment(view , categoriesList[p0].title)
        }

        return view
    }

    private fun navigateToCategoryArticleFragment(view: View, title: String) {
        val bundle = Bundle()
        bundle.putString("category" , title)
        Navigation.findNavController(view).navigate(R.id.action_categoriesFragment_to_categoryArticlesFragment , bundle)
    }
}