package com.nadiahassouni.magazine.ui.reader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.adapters.ArticleAdapter
import com.nadiahassouni.magazine.adapters.StoryAdapter
import com.nadiahassouni.magazine.databinding.FragmentArticlesBinding
import com.nadiahassouni.magazine.model.Article
import com.nadiahassouni.magazine.model.Story
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ArticlesFragment : Fragment() {
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private lateinit var articlesList: ArrayList<Article>
    private lateinit var storiesList: ArrayList<Story>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)

//        showBottomNav()
        showProgressBar()
        getArticles()
        getStories()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showBottomNav()
    }

    private fun getArticles() = CoroutineScope(Dispatchers.IO).launch {
        var article: Article?

        try {
            val articleCollectionRef = Firebase.firestore.collection("articles")
            val querySnapshot = articleCollectionRef
                .whereEqualTo("state" , "valide")
                .whereEqualTo("type" , "article")
                .get().await()
            var list = ArrayList<Article>()
            for (doc in querySnapshot.documents) {
                article = doc.toObject<Article>()
                list.add(article!!)
            }
            withContext(Dispatchers.Main) {
                articlesList = list
                val adapter = ArticleAdapter(requireContext(), articlesList , "articles")
                adapter.notifyDataSetChanged()
                binding.rvArticles.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvArticles.adapter = adapter
                hideProgressBar()
            }

        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun getStories() = CoroutineScope(Dispatchers.IO).launch {
        var story: Story?

        try {
            val storiesCollectionRef = Firebase.firestore.collection("stories")
            val querySnapshot = storiesCollectionRef
                .whereEqualTo("state" , "valide")
                .get().await()
            var list = ArrayList<Story>()
            for (doc in querySnapshot.documents) {
                story = doc.toObject<Story>()
                list.add(story!!)
            }
            withContext(Dispatchers.Main) {
                storiesList = list
                val adapter = StoryAdapter(requireContext(), storiesList)
                adapter.notifyDataSetChanged()
                binding.sliderStories.setSliderAdapter(adapter)
                binding.sliderStories.startAutoCycle()
                hideProgressBar()
            }

        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showBottomNav() {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav!!.visibility = View.VISIBLE
    }
}