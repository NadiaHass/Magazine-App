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
import com.nadiahassouni.magazine.databinding.FragmentCategoryArticlesBinding
import com.nadiahassouni.magazine.databinding.FragmentSignUpBinding
import com.nadiahassouni.magazine.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CategoryArticlesFragment : Fragment() {
    private var _binding : FragmentCategoryArticlesBinding? = null
    private val binding get() = _binding!!
    private lateinit var articlesList: ArrayList<Article>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        _binding = FragmentCategoryArticlesBinding.inflate(inflater , container , false)

        val category = arguments?.getString("category") as String

        binding.textView.text = category

        hideBottomNav()

        showProgressBar()

        getArticles(category)

        return binding.root
    }

    private fun getArticles(category: String) = CoroutineScope(
        Dispatchers.IO).launch {
        var article: Article?

        try {
            val articleCollectionRef = Firebase.firestore.collection("articles")
            val querySnapshot = articleCollectionRef
                .whereEqualTo("category" , category)
                .get().await()
            var list = ArrayList<Article>()
            for (doc in querySnapshot.documents) {
                article = doc.toObject<Article>()
                list.add(article!!)
            }
            withContext(Dispatchers.Main) {
                articlesList = list
                val adapter = ArticleAdapter(requireContext(), articlesList , "magazines")
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

    private fun hideBottomNav() {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav!!.visibility = View.GONE
    }

}