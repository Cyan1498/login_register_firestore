package com.cerna.login_register_firestore.view.fragments.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.cerna.login_register_firestore.MainActivity
import com.cerna.login_register_firestore.R
import com.cerna.login_register_firestore.view.activities.StartActivity
import com.cerna.login_register_firestore.databinding.FragmentSignupBinding
import com.cerna.login_register_firestore.model.User
import com.cerna.login_register_firestore.utils.Helper
import com.cerna.login_register_firestore.utils.SignupFormValidator
import com.cerna.login_register_firestore.viewmodel.UserViewModel

class SignupFragment : Fragment(){

    private val viewModel: UserViewModel by viewModels()
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var signupFormValidator: SignupFormValidator
    private lateinit var loadingAnimationView: LottieAnimationView
    //private var selectedUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        // Inicializar loadingAnimationView después de inflar el diseño del fragmento
        loadingAnimationView = binding.lottieLoading

       // editarUser()

        setupValidationListeners()
//        binding.loginRedirectText.setOnClickListener {
//            val loginIntent = Intent(requireContext(), MainActivity::class.java)
//            startActivity(loginIntent)
//        }

        // Obtener el usuario seleccionado del UserListFragment, si es null no se mostrará nada
        val selectedUser = arguments?.getParcelable<User>("selectedUser")
        if (selectedUser != null) {
            showUserData(selectedUser)
            Helper.showToast(this,"Edicion de Usuario")
            setButtonText(true) // Configurar el texto del botón como "Update"
        } else {
            setButtonText(false) // Configurar el texto del botón como "Register"
        }

        binding.signupButton.setOnClickListener {
            val nameUser = binding.edName.text.toString().trim()
            val emailUser = binding.edEmail.text.toString().trim()
            val passUser = binding.edPassword.text.toString().trim()

            //loadingAnimationView.playAnimation()
            val formValidator = SignupFormValidator(binding)
            if (formValidator.validateForm()) {
                loadingAnimationView.visibility = View.VISIBLE
                loadingAnimationView.playAnimation()
                // Ocultar el formulario y mostrar la animación de carga
                binding.formLayout.visibility = View.GONE
                val userId = selectedUser?.id // Almacenar el ID en una variable local
                if (userId != null) {
                    // Si el ID del usuario no es nulo, significa que es un usuario existente y queremos actualizar sus datos
                    viewModel.updateUser(userId, nameUser, emailUser, passUser)
                } else {
                    // Si el ID del usuario es nulo, significa que es un nuevo usuario y queremos registrarlo
                    viewModel.registerUser(nameUser, emailUser, passUser)
                }
                setButtonText(userId != null) // Cambiar el texto del botón después de la acción
            } else {
                Helper.showToast(this, "Complete correctamente sus datos")
            }
        }

        viewModel.signupSuccess.observe(viewLifecycleOwner) {success->
            //it representa el valor emitido por el LiveData observado.
            if (success) {
                Helper.showToast(this, "Usuario registrado con éxito")
                //viewModel.resetEvents()
                val intent = Intent(requireContext(), StartActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        viewModel.userAlreadyRegisteredEvent.observe(viewLifecycleOwner) {
            Helper.showToast(this, "El usuario ya existe")
        }

        viewModel.unknownErrorEvent.observe(viewLifecycleOwner) {
            Helper.showToast(this, "Error desconocido")
        }

        //con animacion
        viewModel.updateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Detener la animación de carga
                //loadingAnimationView.cancelAnimation()
                loadingAnimationView.visibility = View.GONE

                // Mostrar un mensaje de éxito
                Helper.showToast(this@SignupFragment, "Registro actualizado con éxito")

                // Cerrar el fragmento después de que la animación se haya detenido
                requireActivity().supportFragmentManager.popBackStack()
                // Actualizar la lista de usuarios (asumiendo que el fragmento UserListFragment está en el back stack)
               // requireActivity().supportFragmentManager.popBackStack("UserListFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        return binding.root
    }

    private fun setupValidationListeners() {
        signupFormValidator = SignupFormValidator(binding)

        binding.edName.doAfterTextChanged {
            signupFormValidator.validateName()
        }
        binding.edEmail.doAfterTextChanged {
            signupFormValidator.validateEmail()
        }

        binding.edPassword.doAfterTextChanged {
            signupFormValidator.validatePassword()
        }
    }
    //Editar usuario
    // Método para mostrar los datos del usuario seleccionado en el SignupFragment
    private fun showUserData(user: User) {
        binding.edName.setText(user.name)
        binding.edEmail.setText(user.email)
        binding.edPassword.setText(user.password)
    }

    // Implementación del método de la interfaz OnUserItemClickListener para recibir el usuario seleccionado
//    override fun onUserItemClick(user: User) {
//        showUserData(user)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setButtonText(isUpdateAction: Boolean) {
        if (isUpdateAction) {
            binding.signupButton.text = getString(R.string.update)
        } else {
            binding.signupButton.text = getString(R.string.register)
        }
    }

}
