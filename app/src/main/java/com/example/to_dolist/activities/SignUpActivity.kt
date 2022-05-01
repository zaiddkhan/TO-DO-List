package com.example.to_dolist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.to_dolist.R
import com.example.to_dolist.firestore.FirestoreClass
import com.example.to_dolist.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {


    private var binding : ActivitySignUpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpActionBar()

        binding?.btnSignUp?.setOnClickListener {
            registerUser()
        }
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding?.toolbarSingupActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        binding?.toolbarSingupActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    private fun registerUser(){
        val name:String = binding?.etName?.text.toString()
        val email :String = binding?.etEmail?.text.toString()
        val password : String = binding?.etPassword?.text.toString()
        if(validateForm(name, email, password)) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = com.example.to_dolist.models.User(
                            firebaseUser.uid,
                            name,
                            registeredEmail,
                            password
                        )
                        FirestoreClass().storeSignedUpUserDataInFirestore(this,user)
                    }else{
                        Toast.makeText(this,task.exception!!.message,Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun validateForm(name :String,email:String,password:String) : Boolean{
        return when{
            TextUtils.isEmpty(name) -> {
                Toast.makeText(this, "please enter a name", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "please enter a email", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "please enter a password", Toast.LENGTH_SHORT).show()
                false
            }else -> {
                true
            }
        }
    }
     fun userSuccessfullySignedUp(){
        Toast.makeText(this@SignUpActivity,"Sign Up Successfull",Toast.LENGTH_LONG).show()
        val intent = Intent(this@SignUpActivity,SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}