package com.cerna.login_register_firestore.view.fragments.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cerna.login_register_firestore.databinding.ItemUserBinding
import com.cerna.login_register_firestore.model.User

class UserListAdapter(
    private var userList: List<User>,
    var itemClickListener: OnUserItemClickListener
) : RecyclerView.Adapter<UserViewHolder>() {

//    interface OnUserItemClickListener {
//        //Define un método para manejar eventos de clic en los elementos de usuario.
//        fun onUserItemClick(user: User)
//        //Esto permite que la acción de clic se maneje fuera del adaptador, en el fragmento o actividad que usa el RecyclerView.
//        //fun onDeleteUserClick(user: User)
//    }
    //Crea y devuelve una nueva vista para un elemento de usuario.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding,userList,itemClickListener )
    }

    //Rellena una vista con los datos de un usuario específico y configura el evento de clic.
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = userList[position]
        holder.render(item)
    }
    //Devuelve la cantidad total de usuarios en la lista.
    override fun getItemCount(): Int = userList.size

    //Actualiza la lista de usuarios con nuevos datos y notifica al adaptador para que se reflejen los cambios.
    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }
}


