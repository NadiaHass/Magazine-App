package com.nadiahassouni.magazine.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.databinding.FragmentSignUpBinding
import com.nadiahassouni.magazine.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment() {
    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater , container , false)

        binding.btnSignUp.setOnClickListener {
            signUpNewUser()
        }

        return binding.root
    }

    private fun signUpNewUser() {
        if(checkInputNotEmpty()){
            showProgressBar()
            addUserToFirebase(binding.etEmail.text.toString() , binding.etPassword.text.toString())
        }else{
            Toast.makeText(context , " Veillez complétez tous les champs !" , Toast.LENGTH_LONG).show()
        }
    }

    private fun addUserToFirebase(email : String, password : String) {
        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email , password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = getUser()
                        sendVerificationEmail()
                        storeUserProfileData(user)
                        hideProgressBar()
                    } else {
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        hideProgressBar()
                    }
                }
        }catch (e : Exception){
            hideProgressBar()
        }
    }

    private fun getUser(): User {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        return User(userId,
            binding.etName.text.toString(),
            binding.etSurname.text.toString(),
            binding.etEmail.text.toString(),
            " " , )
    }

    private fun storeUserProfileData(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val userCollectionRef = Firebase.firestore.collection("users").document(user.id)
            userCollectionRef.set(user).await()
            FirebaseAuth.getInstance().signOut()
            navigateToSignInFragment()
        }catch (e : Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context , e.message , Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendVerificationEmail() {
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
            ?.addOnCompleteListener(requireActivity()){
                if (it.isSuccessful){
                    Toast.makeText(context , "Email de verification est envoyee " , Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context , "Echec de l'envoi du l'email de verification" , Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkInputNotEmpty(): Boolean {
        return (binding.etName.text.toString() != "" &&
                binding.etSurname.text.toString() != "" &&
                binding.etEmail.text.toString() != "" &&
                binding.etPassword.text.toString() != "")
    }

    private fun navigateToSignInFragment() {
        Navigation.findNavController(binding.root).navigate(R.id.action_signUpFragment_to_signInFragment)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSignUp.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.btnSignUp.visibility = View.VISIBLE
    }
}