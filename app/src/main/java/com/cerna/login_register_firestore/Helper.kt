package com.cerna.login_register_firestore

import android.widget.Toast
import androidx.fragment.app.Fragment

object Helper {
    fun showToast(fragment: Fragment, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(fragment.requireContext(), message, duration).show()
    }

    fun validateUserFields(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }
}
