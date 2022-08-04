package com.nadiahassouni.magazine.ui.reader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.adapters.ArticleAdapter
import com.nadiahassouni.magazine.adapters.MagazineAdapter
import com.nadiahassouni.magazine.databinding.FragmentArticlesBinding
import com.nadiahassouni.magazine.databinding.FragmentMagazineBinding
import com.nadiahassouni.magazine.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MagazineFragment : Fragment() {
    private var _binding: FragmentMagazineBinding? = null
    private val binding get() = _binding!!
    private lateinit var articlesList: ArrayList<Article>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMagazineBinding.inflate(inflater, container, false)

        showProgressBar()
        getArticles()

        return binding.root
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
    private fun getArticles() = CoroutineScope(Dispatchers.IO).launch {
        var article: Article?

        try {
            val articleCollectionRef = Firebase.firestore.collection("articles")
            val querySnapshot = articleCollectionRef
                .whereEqualTo("state" , "valide")
                .whereEqualTo("type" , "magazine")
                .get().await()
            var list = ArrayList<Article>()
            for (doc in querySnapshot.documents) {
                article = doc.toObject<Article>()
                list.add(article!!)
            }
            withContext(Dispatchers.Main) {
                articlesList = list
                val adapter = MagazineAdapter(requireContext(), articlesList)
                adapter.notifyDataSetChanged()
                binding.rvMagazines.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.rvMagazines.adapter = adapter
                hideProgressBar()
            }

        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}