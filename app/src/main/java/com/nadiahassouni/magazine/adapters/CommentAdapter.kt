package com.nadiahassouni.magazine.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.model.Comment
import com.nadiahassouni.magazine.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CommentAdapter (
    private val context: Context,
    private val commentsList: ArrayList<Comment>)
    : RecyclerView.Adapter<CommentAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var comment : TextView = view.findViewById(R.id.tv_comment)
        var imageView: ImageView = view.findViewById(R.id.iv_user)
        var name : TextView = view.findViewById(R.id.tv_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_rv_item , parent, false)
        return CommentAdapter.ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.comment.text = commentsList[position].comment

        displayData(commentsList[position].userId , holder)
    }

    override fun getItemCount(): Int {
       return commentsList.size
    }

    private fun displayData(id: String, holder: ItemViewHolder) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val userCollectionRef = Firebase.firestore.collection("users").document(id)
            val querySnapshot = userCollectionRef.get().await()
            val user : User? = querySnapshot.toObject<User>()
            withContext(Dispatchers.Main){
                if(user?.imageUrl != "")
                    Glide.with(context)
                        .load(Uri.parse(user?.imageUrl))
                        .into(holder.imageView)

                holder.name.text = user?.name + " " + user?.surname
            }
        }catch (e : Exception){

        }
    }
}