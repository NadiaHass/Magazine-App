package com.nadiahassouni.magazine.ui.visitor

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.databinding.ActivityReaderBinding
import com.nadiahassouni.magazine.databinding.ActivityVisitorBinding

class VisitorActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVisitorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.logo)
        supportActionBar?.setTitle("")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))

        val navHost = findNavController(R.id.visitorFragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navHost)

    }
}