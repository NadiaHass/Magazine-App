package com.nadiahassouni.magazine.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.databinding.FragmentSignInBinding
import com.nadiahassouni.magazine.ui.reader.ReaderActivity

class SignInFragment : Fragment() {
    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater , container , false)

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }
        binding.tvCreateAccount.setOnClickListener {
            navigateToSignUpFragment()
        }
        return binding.root
    }

    private fun navigateToSignUpFragment() {
        Navigation.findNavController(binding.root).navigate(R.id.action_signInFragment_to_signUpFragment)

    }

    private fun signInUser() {
        if(checkInputNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.etEmail.text.toString() ,binding.etPassword.text.toString() )
                .addOnCompleteListener(requireActivity()){ task->
                    if(task.isSuccessful){
                        if(emailIsVerified()){
                            openReaderActivity()
                        }else{
                            sendVerificationEmail()
                            signOut()
                            Toast.makeText(context , "Verifiez votre compte pour pouvoir y acceder . Un email de verification est envoye" , Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .addOnFailureListener(requireActivity()){
                    Toast.makeText(context , "$it",Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    private fun sendVerificationEmail() {
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
    }

    private fun emailIsVerified(): Boolean {
        return FirebaseAuth.getInstance().currentUser?.isEmailVerified == true
    }

    private fun checkInputNotEmpty(): Boolean {
        return (binding.etEmail.text.toString() != "" &&
                binding.etPassword.text.toString() != "")
    }

    private fun openReaderActivity() {
        val intent = Intent(requireActivity() , ReaderActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}