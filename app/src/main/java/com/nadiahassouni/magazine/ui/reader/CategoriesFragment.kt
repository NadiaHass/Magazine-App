package com.nadiahassouni.magazine.ui.reader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.adapters.CategoryAdapter
import com.nadiahassouni.magazine.databinding.FragmentArticlesBinding
import com.nadiahassouni.magazine.databinding.FragmentCategoriesBinding
import com.nadiahassouni.magazine.model.Article
import com.nadiahassouni.magazine.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.ArrayList

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriesList: ArrayList<Category>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        getCategories()

        binding.searchView.setOnClickListener{
            navigateToSearchFragment()
        }

        return binding.root
    }

    private fun navigateToSearchFragment() {
        Navigation.findNavController(binding.root).navigate(R.id.action_categoriesFragment_to_searchFragment)
    }

    override fun onStart() {
        super.onStart()
        showBottomNav()
    }

    private fun getCategories() = CoroutineScope(Dispatchers.IO).launch {
        var category : Category?

        try {
            val categoryCollectionRef = Firebase.firestore.collection("categories")
            val querySnapshot = categoryCollectionRef.get().await()
            var list = ArrayList<Category>()
            for(doc in querySnapshot.documents){
                category = doc.toObject<Category>()
                if (category != null) {
                    list.add(category)
                }
            }
            withContext(Dispatchers.Main){
                categoriesList = list

                val adapter = CategoryAdapter(requireContext() , categoriesList)
                binding.gvCategories.adapter = adapter
            }

        }catch (e : java.lang.Exception){

        }

    }

    private fun showBottomNav() {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav!!.visibility = View.VISIBLE
    }
}