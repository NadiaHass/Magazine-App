package com.nadiahassouni.magazine.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.model.Story
import com.smarteist.autoimageslider.SliderViewAdapter

class StoryAdapter(
    private val context: Context,
    private val storiesList: ArrayList<Story>
) : SliderViewAdapter<StoryAdapter.Holder>() {

    class Holder(view : View) : SliderViewAdapter.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tv_title)
        var tvDate: TextView = view.findViewById(R.id.tv_date)
        var imageView: ImageView = view.findViewById(R.id.iv_story)

    }

    override fun getCount(): Int {
        return storiesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): Holder {
        val adapterLayout = LayoutInflater.from(parent?.context)
            .inflate(R.layout.story_rv_item , parent, false)
        return StoryAdapter.Holder(adapterLayout)
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.tvTitle?.text = storiesList[position].title
        holder?.tvDate?.text = storiesList[position].date
        Glide.with(context)
            .load(Uri.parse(storiesList[position].imageUrl))
            .into(holder?.imageView!!)
    }
}