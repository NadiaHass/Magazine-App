package com.nadiahassouni.magazine.ui.visitor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.databinding.FragmentArticlesBinding
import com.nadiahassouni.magazine.databinding.FragmentProfileVisitorBinding
import com.nadiahassouni.magazine.ui.auth.AuthActivity

class ProfileVisitorFragment : Fragment() {
    private var _binding: FragmentProfileVisitorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileVisitorBinding.inflate(inflater, container, false)

        binding.tvSignIn.setOnClickListener {
            openAuthActivity()
        }

        binding.ivFacebook.setOnClickListener {
            openUrl("https://www.facebook.com/profile.php?id=100084289170331")
        }

        binding.ivInstagram.setOnClickListener {
            openUrl("https://instagram.com/eve_magazine1?igshid=YmMyMTA2M2Y")
        }

        return binding.root
    }

    private fun openAuthActivity() {
        startActivity(Intent(requireContext()  , AuthActivity::class.java))
    }

    fun openUrl(url : String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}