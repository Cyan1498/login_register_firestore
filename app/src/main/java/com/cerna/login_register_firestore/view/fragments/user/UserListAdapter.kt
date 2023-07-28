package com.cerna.login_register_firestore.view.fragments.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cerna.login_register_firestore.R
import com.cerna.login_register_firestore.databinding.ItemUserBinding
import com.cerna.login_register_firestore.model.User

class UserListAdapter(
    private var userList: List<User>,
    var itemClickListener: OnUserItemClickListener
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    interface OnUserItemClickListener {
        fun onUserItemClick(user: User)
        //fun onDeleteUserClick(user: User)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Para pasar los datos seleccionados
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = userList[position]
                    itemClickListener.onUserItemClick(user)
                }
            }
        }
        val tvUserName: TextView = binding.tvUserName
        val tvUserEmail: TextView = binding.tvUserEmail

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.tvUserName.text = currentUser.name
        holder.tvUserEmail.text = currentUser.email

        holder.itemView.setOnClickListener {
            itemClickListener.onUserItemClick(currentUser)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }
}


