package com.example.firebasecrud.data

data class UsersUiState(
    val user: Users? = null,
    val username: String = "",
    val password: String = "",
    val usersList: List<Users> = emptyList(),
    val isEditOpen: Boolean = false
)
