package com.example.firebasecrud.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsersViewModel: ViewModel() {
    // cria uma versão mútavel e observável do UsersUiState.
    private val _uiState = MutableStateFlow(UsersUiState())
    // cria uma versão imutável e exposta do UsersUistate.
    val uiState = _uiState.asStateFlow()

    // Função para carregar todos os users no banco de dados.
    fun loadUsers(databaseReference: DatabaseReference, context: Context) {
        databaseReference.addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onUsersListEmpty()
                    snapshot.children.forEach {
                        val users = it.getValue(Users::class.java)
                        users?.let { onUsersListAdd(users) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context,
                        "Erro ao carregar o usuário",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        )
    }

    // Função que armazena umnovo User no banco de dados.
    fun addUser(databaseReference: DatabaseReference, context: Context, user: Users) {
        // É criado um novo child caso não exista o id. Se criado, será usado o mesmo child.
        databaseReference.child(user.id).setValue(user)
        // Se child criado.
            .addOnSuccessListener {
                // Texto aviso no inferior da tela.
                Toast.makeText(
                    context,
                    "Usuário cadastrado com sucesso.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Erro ao cadastrar usuário",
                    Toast.LENGTH_SHORT
                ).show()
            }
        // OnUsernameChange
        // OnPasswordChange
    }

    // Função para remover um usuário.
    fun deleteUser(databaseReference: DatabaseReference, context: Context, userId: String) {
        databaseReference.child(userId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Usuário deletado com sucesso.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Erro ao deletar o usuário.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun editUser(databaseReference: DatabaseReference, context: Context, user: Users) {
        val newUserDataMap = mapOf(
            "username" to user.username,
            "password" to user.password
        )
        databaseReference.child(user.id).updateChildren(newUserDataMap)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "User editado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Erro ao editar o user",
                    Toast.LENGTH_SHORT
                ).show()
            }
        _uiState.value = _uiState.value.copy(
            username = "",
            password = "",
            isEditOpen = false
        )
    }

    fun onUsersListEmpty() {
        _uiState.value = _uiState.value.copy(
            usersList = emptyList()
        )
    }

    fun onUsersListAdd(users: Users) {
        val usersList = _uiState.value.usersList
        _uiState.value = _uiState.value.copy(
            // adiciona o elemento na lista
            usersList = (usersList + users)
        )
    }

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password
        )
    }

    fun editDialogOpen(user: Users) {
        _uiState.value = _uiState.value.copy(
            isEditOpen = true,
            user = user,
            username = user.username,
            password = user.password
        )
    }

    fun editDialogClose() {
        _uiState.value = _uiState.value.copy(
            isEditOpen = false
        )
    }
}