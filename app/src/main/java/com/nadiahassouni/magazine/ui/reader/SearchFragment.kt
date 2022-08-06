package com.nadiahassouni.magazine.ui.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.adapters.ArticleAdapter
import com.nadiahassouni.magazine.databinding.FragmentSearchBinding
import com.nadiahassouni.magazine.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var articlesList: ArrayList<Article>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        hideBottomNav()

        binding.serchView.setOnQueryTextListener(object :  SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                getArticles(p0!!)
                return true
            }

        })
        return binding.root
    }

    private fun hideBottomNav() {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav!!.visibility = View.GONE
    }

    private fun getArticles(input : String) = CoroutineScope(Dispatchers.IO).launch {
        var article: Article?

        try {
            if(input.length > 0){
                var recherche = input.substring(0,1).uppercase() + input.substring(1).lowercase()
                val articleCollectionRef = Firebase.firestore.collection("articles")
                val querySnapshot = articleCollectionRef
                    .whereEqualTo("state" , "valide")
                    .get().await()
                var list = ArrayList<Article>()
                for (doc in querySnapshot.documents) {
                    article = doc.toObject<Article>()
                    if(article?.title?.contains(recherche)!!)
                       list.add(article)
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
            }else{
                withContext(Dispatchers.Main) {
                    articlesList.clear()
                    val adapter = ArticleAdapter(requireContext(), articlesList , "articles")
                    adapter.notifyDataSetChanged()
                    binding.rvArticles.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.rvArticles.adapter = adapter
                    hideProgressBar()
                }
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
}