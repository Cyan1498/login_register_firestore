package com.cerna.login_register_firestore.view.fragments.user.adapter

import androidx.recyclerview.widget.RecyclerView
import com.cerna.login_register_firestore.databinding.ItemUserBinding
import com.cerna.login_register_firestore.model.User

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val userList: List<User>,
    private val itemClickListener: OnUserItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val user = userList[position]
                itemClickListener.onUserItemClick(user)
            }
        }
    }

    fun render(user: User) {
        binding.tvUserName.text = user.name
        binding.tvUserEmail.text = user.email
    }
}

