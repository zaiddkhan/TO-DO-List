package com.example.to_dolist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.to_dolist.R
import com.example.to_dolist.firestore.FirestoreClass
import com.example.to_dolist.models.User

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            var currentUserID = FirestoreClass().getCurrentUserId()
            if(currentUserID.isNotEmpty()){
                FirestoreClass().getCurrentUserData(this)
            }else {
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }
        },2500)
    }
    fun sendUserNameToMainActivityFromSplash(user: User?){
        val name:String = user!!.name
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("username",name)
        startActivity(intent)
        finish()
    }
}