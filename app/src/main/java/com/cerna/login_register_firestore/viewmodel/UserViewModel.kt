package com.cerna.login_register_firestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cerna.login_register_firestore.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {

    private val _signupSuccess = MutableLiveData<Boolean>()
    val signupSuccess: LiveData<Boolean> get() = _signupSuccess

    private val _userAlreadyRegisteredEvent = MutableLiveData<Boolean>()
    val userAlreadyRegisteredEvent: LiveData<Boolean> get() = _userAlreadyRegisteredEvent

    private val _unknownErrorEvent = MutableLiveData<Boolean>()
    val unknownErrorEvent: LiveData<Boolean> get() = _unknownErrorEvent

    // Agregar nueva variable LiveData para almacenar el usuario seleccionado para editar
    private val _selectedUser = MutableLiveData<User>()
    val selectedUser: LiveData<User> get() = _selectedUser

    //Listar
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList

    //Actualizar
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess


    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun registerUser(name: String, email: String, password: String) {
        // Verificamos si el usuario ya está registrado
        firebaseAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (!signInMethods.isNullOrEmpty()) {
                        // El usuario ya está registrado, muestra el evento correspondiente.
                        _userAlreadyRegisteredEvent.value = true
                    } else {
                        // El usuario no está registrado, procede con el registro.
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val currentUser = firebaseAuth.currentUser
                                    currentUser?.let {
                                        val user = User(
                                            id = it.uid,
                                            name = name,
                                            email = email,
                                            password = password
                                        )
                                        addUserToFirestore(user)
                                    }
                                } else {
                                    // Error desconocido durante el registro
                                    _unknownErrorEvent.value = true
                                }
                            }
                    }
                } else {
                    // Error al verificar la existencia del usuario
                    _unknownErrorEvent.value = true
                }
            }
    }

    private fun addUserToFirestore(user: User) {
        firestore.collection("users").document(user.id!!)
            .set(user)
            .addOnSuccessListener {
                _signupSuccess.value = true
            }
            .addOnFailureListener {
                _unknownErrorEvent.value = true
            }
    }

    fun getAllUsers(): LiveData<List<User>> {
        // Query Firestore to get all users
        usersCollection
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = mutableListOf<User>()
                for (document in querySnapshot) {
                    val user = document.toObject(User::class.java)
                    users.add(user)
                }
                _userList.value = users
            }
            .addOnFailureListener { exception ->
                // Handle failure
                // You can set an error message or handle it as per your app's requirement
            }
        return userList
    }

    fun resetEvents() {
        _signupSuccess.value = false
        _userAlreadyRegisteredEvent.value = false
        _unknownErrorEvent.value = false
    }

    // Nuevo método para configurar el usuario seleccionado para editar
    fun setSelectedUser(user: User) {
        _selectedUser.value = user
    }

    fun getSelectedUser(): User {
        return selectedUser.value ?: User() // Retorna un objeto User vacío si selectedUser es nulo
    }

    fun updateUser(userId: String, name: String, email: String, password: String) {
        val user = User(userId, name, email, password)
        firestore.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                // La actualización fue exitosa
                _updateSuccess.value = true
            }
            .addOnFailureListener {
                // Error durante la actualización
                _unknownErrorEvent.value = true
            }
    }

    fun deleteUser(user: User) {
        // Eliminar el usuario del conjunto de datos (lista de usuarios)
        val currentList = userList.value.orEmpty().toMutableList()
        currentList.remove(user)
        _userList.value = currentList

        // Aquí implementamos la lógica para eliminar el usuario de la base de datos de Firestore
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection("users")

        // Realizamos la consulta para obtener el documento del usuario en Firestore según su ID
        usersCollection.document(user.id!!).delete()
            .addOnSuccessListener {
                // El usuario se eliminó con éxito de la base de datos de Firestore
                // Aquí puedes realizar cualquier acción adicional después de la eliminación
            }
            .addOnFailureListener { exception ->
                // Ocurrió un error al eliminar el usuario de la base de datos de Firestore
                // Aquí puedes manejar el error de acuerdo a las necesidades de tu app
            }
    }


}
