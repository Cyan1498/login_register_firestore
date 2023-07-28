package com.cerna.login_register_firestore.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cerna.login_register_firestore.utils.Helper
import com.cerna.login_register_firestore.MainActivity
import com.cerna.login_register_firestore.view.activities.StartActivity
import com.cerna.login_register_firestore.databinding.FragmentLoginBinding
import com.cerna.login_register_firestore.utils.LoginFormValidator
import com.cerna.login_register_firestore.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginFormValidator: LoginFormValidator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupValidationListeners()
        setupLoginButton()
        //redirectToSignup()
        redirectToUserListFragment()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //El fragmento LoginFragment observa la LiveData loginResult del ViewModel com.cerna.login_register_firestore.viewmodel.LoginViewModel
        //utilizando viewModel.loginResult.observe().

        //Cuando se produce un cambio en el valor de loginResult, el fragmento recibe la notificación
        // y toma una acción basada en el resultado del inicio de sesión:
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { success ->
            //viewLifecycleOwner: ciclo de vida asociado con la vista del fragmento
            //observa LiveData y evita posibles fugas de memoria cuando se registra un observador.
            if (success) {
                requireActivity().finish()
                val intent = Intent(context, StartActivity::class.java)
                startActivity(intent)
            }
        })

        viewModel.userNotFoundEvent.observe(viewLifecycleOwner, Observer {
            Helper.showToast(this, "Usuario no existe")
        })

        viewModel.incorrectPasswordEvent.observe(viewLifecycleOwner, Observer {
            Helper.showToast(this, "Contraseña incorrecta")
        })

        viewModel.unknownErrorEvent.observe(viewLifecycleOwner, Observer {
            //Helper.showToast(this, "Error desconocido")
            Helper.showToast(this, "Revise su conexion a internet")
        })
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            loginFormValidator = LoginFormValidator(binding)
            if (loginFormValidator.validateForm()) {
                val email = binding.edEmail.text.toString().trim()
                val password = binding.edPassword.text.toString().trim()
                viewModel.loginUser(email, password)
            }else {
                Helper.showToast(this, "Ingrese correctamente sus credenciales")
            }
        }
    }

//    private fun redirectToSignup() {
//        binding.signupRedirectText.setOnClickListener {
//            val mainActivity = activity as? MainActivity
//            mainActivity?.showRegisterFragment()
//        }
//    }

    private fun redirectToUserListFragment() {
        binding.signupRedirectText.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.showUserListFragment()
        }
    }

    private fun setupValidationListeners() {
        //En el código anterior, se utiliza un TextWatcher para escuchar los cambios en el EditText llamado edName.
        //Esta función permite escuchar los cambios en el texto del TextInputEditText
        //y ejecutar el código que se proporciona como lambda después de que el texto ha cambiado.
        loginFormValidator = LoginFormValidator(binding)

        binding.edEmail.doAfterTextChanged {
            loginFormValidator.validateEmail()
        }

        binding.edPassword.doAfterTextChanged {
            loginFormValidator.validatePassword()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
