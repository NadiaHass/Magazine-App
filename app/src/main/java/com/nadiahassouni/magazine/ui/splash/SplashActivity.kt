package com.nadiahassouni.magazine.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.nadiahassouni.magazine.R
import com.nadiahassouni.magazine.ui.auth.AuthActivity
import com.nadiahassouni.magazine.ui.reader.ReaderActivity
import com.nadiahassouni.magazine.ui.visitor.VisitorActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Handler().postDelayed({ checkAuthState() }, 2000)
    }

    private fun checkAuthState() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            openVisitorActivity()
        } else {
            openReaderActivity()
        }
    }

    private fun openReaderActivity() {
        startActivity(Intent(this , ReaderActivity::class.java))
        startActivity(intent)
        finish()
    }

    private fun openVisitorActivity() {
        startActivity(Intent(this , VisitorActivity::class.java))
        startActivity(intent)
        finish()
    }

}