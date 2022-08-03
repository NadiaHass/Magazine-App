package com.nadiahassouni.magazine.ui.reader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nadiahassouni.magazine.databinding.FragmentProfileBinding
import com.nadiahassouni.magazine.model.User
import com.nadiahassouni.magazine.ui.visitor.VisitorActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var storageReference : StorageReference
    private lateinit var userId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        _binding = FragmentProfileBinding.inflate(inflater , container , false)

        storageReference = FirebaseStorage.getInstance().getReference("Pictures")
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        binding.progressBar.visibility = View.VISIBLE
        displayData()

        binding.tvUpdatePicture.setOnClickListener {
            openFileChooser()
        }

        binding.tvSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), VisitorActivity::class.java))
            activity?.finish()
        }

        binding.ivFacebook.setOnClickListener {
            openUrl("https://www.facebook.com/profile.php?id=100084289170331")
        }

        binding.ivInstagram.setOnClickListener {
            openUrl("https://instagram.com/eve_magazine1?igshid=YmMyMTA2M2Y")
        }

        return binding.root
    }

    private fun displayData() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val userCollectionRef = Firebase.firestore.collection("users").document(userId)
            val querySnapshot = userCollectionRef.get().await()
            val user : User? = querySnapshot.toObject<User>()
            withContext(Dispatchers.Main){
                if(user?.imageUrl != "")
                    Glide.with(requireContext())
                        .load(Uri.parse(user?.imageUrl))
                        .into(binding.imgProfile)

                binding.tvEmail.text = user?.email
                binding.tvName.text = user?.name
                binding.tvSurname.text = user?.surname

                binding.progressBar.visibility = View.GONE
            }
        }catch (e : Exception){

        }
    }
    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK ){
            val data : Intent? = result.data
            if (data?.data != null) {
                val uriImage = data.data
                binding.imgProfile.setImageURI(uriImage)
                uploadPictureToStorage(uriImage)
            }
        }
    }

    private fun uploadPictureToStorage(uriImage: Uri?) = CoroutineScope(Dispatchers.IO).launch {
        if(uriImage != null){
            val fileReference = FirebaseAuth.getInstance().currentUser?.let {
                storageReference.child(it.uid + getFileExtension(uriImage))
            }
            fileReference?.putFile(uriImage)?.addOnSuccessListener { l ->
                fileReference.downloadUrl.addOnSuccessListener {
                    val userCollectionRef = Firebase.firestore.collection("users").document(userId)
                    userCollectionRef.update(
                        mapOf( "imageUrl" to it
                        )
                    )
                }
            }
        }
    }

    private fun getFileExtension(uriImage: Uri): String {
        val contentResolver = activity?.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver?.getType(uriImage))!!
    }

    fun openUrl(url : String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}