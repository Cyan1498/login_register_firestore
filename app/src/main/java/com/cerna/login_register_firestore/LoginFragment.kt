package com.cerna.login_register_firestore

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cerna.login_register_firestore.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.signupRedirectText.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.showRegisterFragment()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login()
    }


    private fun login() {
        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {

            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                //addOnCompleteListener: listener ejecutará cuando se complete una operación asíncrona,
                //como el intento de inicio de sesión en Firebase Authentication.
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        requireActivity().finish()
                        //startActivity(Intent(requireContext(), MainActivity::class.java))
                        val intent = Intent(context, StartActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            }
        }
    }

}