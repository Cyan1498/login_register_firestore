package com.cerna.login_register_firestore.utils

import com.cerna.login_register_firestore.databinding.FragmentSignupBinding

class SignupFormValidator(private val binding: FragmentSignupBinding) {

    fun validateForm(): Boolean {
        return validateName() && validateEmail() && validatePassword()
    }

    fun validateName(): Boolean {
        val name = binding.edName.text.toString().trim()
        return if (name.isEmpty()) {
            binding.signupNameL.error = "Required"
            false
        } else {
            binding.signupNameL.error = null
            true
        }
    }

    fun validateEmail(): Boolean {
        val emailPattern = Regex("[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+")
        val email = binding.edEmail.text.toString().trim()
        return if (email.isEmpty()) {
            binding.signupEmailL.error = "Required"
            false
        } else if (!email.matches(emailPattern)) {
            binding.signupEmailL.error = "Valid E-mail"
            false
        } else {
            binding.signupEmailL.error = null
            true
        }
    }

    fun validatePassword(): Boolean {
        val password = binding.edPassword.text.toString().trim()
        return if (password.isEmpty()) {
            binding.signupPasswordL.error = "Required"
            false
        } else if (password.length !in 6..10) {
            binding.signupPasswordL.error = "¡La contraseña debe tener entre 6 y 10 caracteres!"
            false
        } else {
            binding.signupPasswordL.error = null
            true
        }
    }
}
