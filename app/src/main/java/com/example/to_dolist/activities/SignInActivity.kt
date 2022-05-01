package com.example.to_dolist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.to_dolist.R
import com.example.to_dolist.firestore.FirestoreClass
import com.example.to_dolist.models.User
import com.example.to_dolist.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private var binding : ActivitySignInBinding? = null
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = FirebaseAuth.getInstance()
        setUpActionBar()
        binding?.btnSignIn?.setOnClickListener {
            signInUser()
        }

    }
    private fun setUpActionBar(){
        setSupportActionBar(binding?.toolbarSinginActivity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        binding?.toolbarSinginActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    private fun signInUser(){
        val email = binding?.etSigninEmail?.text.toString()
        val password = binding?.etSigninPassword?.text.toString()

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this,"SIGN IN SUCCESSFUL", Toast.LENGTH_SHORT).show()
                    FirestoreClass().getCurrentUserData(this)
                }else{
                    Toast.makeText(this,"SIGN IN Not SUCCESSFUL", Toast.LENGTH_SHORT).show()

                }
            }
    }
    fun sendUserNameToMainActivityFromSignIn(user: User?){
        val name:String = user!!.name
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("username",name)
        startActivity(intent)
        finish()
    }

}