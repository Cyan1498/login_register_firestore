package com.cerna.login_register_firestore.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String?,
    val name: String?,
    val email: String?,
    val password: String?
) : Parcelable {
    // Constructor sin argumentos requerido por Firebase Firestore
    constructor() : this(null, null, null, null)
}
