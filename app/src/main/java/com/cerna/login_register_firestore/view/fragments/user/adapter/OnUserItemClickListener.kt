package com.cerna.login_register_firestore.view.fragments.user.adapter

import com.cerna.login_register_firestore.model.User

interface OnUserItemClickListener {
    //Esto permite que la acción de clic se maneje fuera del adaptador,
    //en el fragmento o actividad que usa el RecyclerView.
    fun onUserItemClick(user: User)
}
