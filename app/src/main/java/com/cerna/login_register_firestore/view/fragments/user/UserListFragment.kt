package com.cerna.login_register_firestore.view.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.cerna.login_register_firestore.R
import com.cerna.login_register_firestore.databinding.FragmentUserListBinding
import com.cerna.login_register_firestore.model.User
import com.cerna.login_register_firestore.utils.anim.AnimationHelper
import com.cerna.login_register_firestore.view.fragments.user.adapter.OnUserItemClickListener
import com.cerna.login_register_firestore.view.fragments.user.adapter.UserListAdapter
import com.cerna.login_register_firestore.viewmodel.UserViewModel

class UserListFragment : Fragment(), OnUserItemClickListener {

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

        loadingAnimationView = binding.lottieLoading
        //Cargar la data al recyclerview
        setupRecyclerView()
        loadingAnimationView.visibility = View.VISIBLE
        //Actualizar el recycler view despues de alguna accion
        observeUserData()

        // Ir al formulario de Registro con Animation FAB Explosion
        binding.fabAddUser.setOnClickListener {
            binding.fabAddUser.isVisible = false
            AnimationHelper.startCircleAnimationWithDelay(
                binding.circle,
                this,
                R.anim.circle_explosion_anim,
                1500,
                ::showRegisterFragment
            )
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

    //Pasar datos la vista SignupFragment usando Parcelize
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

    //vincular el UserListAdapter con el RecyclerView, estableciendo la conexión entre los datos y la interfaz de usuario.
    private fun setupRecyclerView() {
        userAdapter = UserListAdapter(emptyList(), this) // Empty list initially
        //recyclerView.adapter (apply optimiza, para no hacer con cada uno)
        binding.rcView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    override fun onUserItemClick(user: User) {
        //val toastMessage = "Nomb re: ${user.name}\nEmail: ${user.email}"
        //Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
        showEditFragment(user)
    }

    private fun observeUserData() {
        viewModel.getAllUsers().observe(viewLifecycleOwner) { userList ->
            // Aquí actualizarías el RecyclerView con los datos cargados
            userAdapter.updateData(userList)
            // y luego ocultarías la animación de carga
            loadingAnimationView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
