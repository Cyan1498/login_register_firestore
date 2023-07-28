package com.cerna.login_register_firestore.view.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.cerna.login_register_firestore.R
import com.cerna.login_register_firestore.databinding.FragmentUserListBinding
import com.cerna.login_register_firestore.model.User
import com.cerna.login_register_firestore.utils.Helper
import com.cerna.login_register_firestore.viewmodel.UserViewModel

class UserListFragment : Fragment(), UserListAdapter.OnUserItemClickListener {

    private val viewModel: UserViewModel by viewModels()
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserListAdapter
    private lateinit var loadingAnimationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        // Inicializar la referencia al LottieAnimationView
        loadingAnimationView = binding.lottieLoading
        setupRecyclerView()
        // Mostrar la animación de carga
        loadingAnimationView.visibility = View.VISIBLE

        observeUserData()

        binding.fabAddUser.setOnClickListener {
            showRegisterFragment()

        }
        return binding.root
    }

    private fun showRegisterFragment() {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, SignupFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showEditFragment(selectedUser: User) {
        val signupFragment = SignupFragment()

        // Puedes pasar datos al fragmento utilizando un Bundle
        val bundle = Bundle()
        bundle.putParcelable("selectedUser", selectedUser)
        bundle.putBoolean("isEditMode", true) // Indicador de modo edición
        signupFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, signupFragment)
            .addToBackStack(null)
            .commit()
    }


        private fun setupRecyclerView() {
        userAdapter = UserListAdapter(emptyList(), this) // Empty list initially
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    override fun onUserItemClick(user: User) {
        // Mostramos un Toast con los datos del usuario al hacer clic en el elemento
        val toastMessage = "Nomb re: ${user.name}\nEmail: ${user.email}"
        //Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
        Helper.showToast(this, toastMessage)
    }

    private fun observeUserData() {
        viewModel.getAllUsers().observe(viewLifecycleOwner) { userList ->
            // Aquí actualizarías el RecyclerView con los datos cargados
            userAdapter.updateData(userList)
            // y luego ocultarías la animación de carga
            loadingAnimationView.visibility = View.GONE
        }

        userAdapter.itemClickListener = object : UserListAdapter.OnUserItemClickListener {
            override fun onUserItemClick(user: User) {
                // Aquí puedes navegar al SignupFragment y pasar los datos del usuario seleccionado
                showEditFragment(user)
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
