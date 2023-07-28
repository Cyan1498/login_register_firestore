package com.cerna.login_register_firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.cerna.login_register_firestore.databinding.ActivityMainBinding
import com.cerna.login_register_firestore.view.fragments.LoginFragment
import com.cerna.login_register_firestore.view.fragments.user.SignupFragment
import com.cerna.login_register_firestore.view.fragments.user.UserListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentContainer: FrameLayout
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentContainer = binding.fragmentContainer
        showLoginFragment()
    }

    private fun showLoginFragment() {
        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, loginFragment)
            .commit()
    }

//    fun showRegisterFragment() {
//        val signupFragment = SignupFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, signupFragment)
//            .commit()
//    }

    fun showUserListFragment() {
        val userListFragment = UserListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, userListFragment)
            .commit()
    }
}