package com.example.to_dolist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.to_dolist.R
import com.example.to_dolist.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
     private lateinit var navController: NavController
    private var welcomeText : TextView? = null
    private var binding: ActivityMainBinding? = null
    private var toolbar: androidx.appcompat.widget.Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)


        toolbar = findViewById(R.id.toolbar_main_activity)
        setUpActionBar()

        binding?.navView?.setNavigationItemSelectedListener(this)

        welcomeText = findViewById(R.id.name)
        if(intent.hasExtra("username")){
            welcomeText?.text = "Welcome, "+intent.getStringExtra("username")
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(R.drawable.ic_menu)
        toolbar?.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (binding?.drawerMain!!.isDrawerOpen(GravityCompat.START)) {
            binding?.drawerMain?.closeDrawer(GravityCompat.START)
        } else {
            binding?.drawerMain!!.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (binding?.drawerMain!!.isDrawerOpen(GravityCompat.START)) {
            binding?.drawerMain?.closeDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sign_out -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this,IntroActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
            }

        }
        binding?.drawerMain!!.closeDrawer(GravityCompat.START)
        return true
    }
}