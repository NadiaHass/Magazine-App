package com.nadiahassouni.magazine.ui.reader

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.databinding.ActivityReaderBinding

class ReaderActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReaderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.logo)
        supportActionBar?.setTitle("")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))


        val navHost = findNavController(R.id.readerFragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navHost)
    }
}