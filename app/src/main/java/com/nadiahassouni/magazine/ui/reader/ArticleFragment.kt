package com.nadiahassouni.magazine.ui.reader

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.adapters.ArticleAdapter
import com.nadiahassouni.magazine.adapters.CommentAdapter
import com.nadiahassouni.magazine.databinding.FragmentArticleBinding
import com.nadiahassouni.magazine.model.Article
import com.nadiahassouni.magazine.model.Comment
import com.nadiahassouni.magazine.ui.auth.AuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private lateinit var commentsList: ArrayList<Comment>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)

        val article = arguments?.getSerializable("article") as Article

        hideBottomNav()
        putData(article)

        binding.ivSendComment.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null){
                showProgressBar()
                binding.ivArticle.isEnabled = false
                addComment(binding.etComment.text.toString() , article.id)
            }else{
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Vous devez etre connecte pour faire un commentaire")
                    .setMessage("Est ce que vous voulez se connecter ?")
                    .setPositiveButton("Oui") { p0, p1 ->
                        openAuthActivity()
                    }
                    .setNegativeButton("Non"){ p0, p1 ->
                        p0.dismiss()
                    }
                alertDialog.show()
            }

        }

        getComments(article.id)
        return binding.root
    }

    private fun openAuthActivity() {
        startActivity(Intent(requireContext() , AuthActivity::class.java))
    }

    private fun addComment(commentContent: String, id: String)= CoroutineScope(Dispatchers.IO).launch {
        try {
            val commentCollectionRef = Firebase.firestore.collection("comments")
            val commentId = commentCollectionRef.document().id
            val comment = Comment(commentId , id , FirebaseAuth.getInstance().currentUser?.uid.toString() , commentContent )
            commentCollectionRef.add(comment).await()
            withContext(Dispatchers.Main){
                Toast.makeText(context ,"Le commentaireete ajoute" , Toast.LENGTH_LONG).show()
                hideProgressBar()
                binding.etComment.text.clear()
                binding.ivArticle.isEnabled = true
                getComments(id)
            }
        }catch (e : Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context , e.message , Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun putData(article: Article) {
        Glide.with(requireContext())
            .load(article.imageUrl)
            .into(binding.ivArticle)
        binding.tvTitle.text = article.title
        binding.tvDate.text = article.date
        binding.tvCategory.text = article.category
        binding.tvContent.text = article.text
    }

    private fun hideBottomNav() {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav!!.visibility = View.GONE
    }

    private fun getComments(id: String) = CoroutineScope(Dispatchers.IO).launch {
        var comment: Comment?

        try {
            val commentCollectionRef = Firebase.firestore.collection("comments")
            val querySnapshot = commentCollectionRef
                .whereEqualTo("articleId" , id)
                .get().await()
            var list = ArrayList<Comment>()
            for (doc in querySnapshot.documents) {
                comment = doc.toObject<Comment>()
                list.add(comment!!)
            }
            withContext(Dispatchers.Main) {
                commentsList = list
                val adapter = CommentAdapter(requireContext(), commentsList)
                adapter.notifyDataSetChanged()
                binding.rvComments.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvComments.adapter = adapter
                hideProgressBar()
            }

        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}