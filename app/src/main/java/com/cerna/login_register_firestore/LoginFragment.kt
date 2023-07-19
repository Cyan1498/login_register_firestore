package com.cerna.login_register_firestore

import LoginViewModel
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cerna.login_register_firestore.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupLoginButton()
        redirectToSignup()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                requireActivity().finish()
                val intent = Intent(context, StartActivity::class.java)
                startActivity(intent)
            } else {
                val errorMessage = getString(R.string.message_login_failed)
                Helper.showToast(this, errorMessage)
            }
        })
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            viewModel.loginUser(email, password)
        }
    }

    private fun redirectToSignup() {
        binding.signupRedirectText.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.showRegisterFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
