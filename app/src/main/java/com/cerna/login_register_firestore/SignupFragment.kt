package com.cerna.login_register_firestore

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cerna.login_register_firestore.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {

    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(loginIntent)
        }


        mFirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        //val btn_signup = binding.signupButton

        binding.signupButton.setOnClickListener {
            val nameUser = binding.signupName.text.toString().trim()
            val emailUser = binding.signupEmail.text.toString().trim()
            val passUser = binding.signupPassword.text.toString().trim()

            if (nameUser.isEmpty() || emailUser.isEmpty() || passUser.isEmpty()) {
                //showToast("Complete los datos")
                Helper.showToast(this,"Complete los datos")
                //Toast.makeText(requireContext(), "Complete los datos", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(nameUser, emailUser, passUser)
            }
        }

        return binding.root
    }

    private fun registerUser(nameUser: String, emailUser: String, passUser: String) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val id = mAuth.currentUser?.uid
                    val map = hashMapOf(
                        "id" to id,
                        "name" to nameUser,
                        "email" to emailUser,
                        "password" to passUser
                    )

                    mFirestore.collection("user").document(id!!)
                        .set(map)
                        .addOnSuccessListener {
                            //Toast.makeText(requireContext(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                            //showToast("Usuario registrado con éxito")
                            Helper.showToast(this, "Usuario registrado con éxito")
                            val intent = Intent(context, StartActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        .addOnFailureListener {
                            //showToast("Error al guardar")
                            Helper.showToast(this, "Error al guardar")
                            //Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()

                        }
                }
                else {
                    //showToast("Error al registrar")
                    Helper.showToast(this, "Error al guardar")
                    //Toast.makeText(requireContext(), "Error al registrar", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //para limpiar la referencia al binding.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Función de extensión showToast()
    /*private fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }*/


}

