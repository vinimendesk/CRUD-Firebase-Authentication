package com.example.firebasecrud.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.firebasecrud.auth.AuthState
import com.example.firebasecrud.auth.AuthViewModel
import com.example.firebasecrud.components.EditDialog
import com.example.firebasecrud.data.Users
import com.example.firebasecrud.data.UsersViewModel
import com.example.firebasecrud.navigation.ScreenType
import com.google.firebase.database.DatabaseReference

@Composable
fun MainUI(
    navController: NavController,
    databaseReference: DatabaseReference,
    authViewModel: AuthViewModel,
    usersViewModel: UsersViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    val userUiState = usersViewModel.uiState.collectAsState()

    val authState = authViewModel.authState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) { usersViewModel.loadUsers(databaseReference, context) }

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate(ScreenType.LOGIN.toString()) {
                    popUpTo(ScreenType.MAIN.toString()) { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = userUiState.value.username,
            onValueChange = { username -> usersViewModel.onUsernameChange(username) },
            label = { Text(text = "Nome") },
            placeholder = { Text(text = "Insira seu nome de usuário") },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = userUiState.value.password,
            onValueChange = { password -> usersViewModel.onPasswordChange(password) },
            label = { Text(text = "Senha") },
            placeholder = { Text(text = "Insira a sua senha") },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Botão para criar usuário.
            Button(
                enabled = authState.value != AuthState.Loading,
                onClick = {
                    authViewModel.signUp(
                        userUiState.value.username,
                        userUiState.value.password,
                        usersViewModel
                    )
                    val userId = databaseReference.push().key
                    if (userId != null) {
                        usersViewModel.addUser(
                            databaseReference,
                            context,
                            Users(userId, userUiState.value.username, userUiState.value.password)
                        )
                    }
                },
                modifier = Modifier
            ) {
                Text(text = "Adicionar Usuário")
            }
            // Botão para signOut.
            Button(
                enabled = authState.value != AuthState.Loading,
                onClick = {
                    authViewModel.signOut()
                },
                modifier = Modifier
            ) {
                Text(text = "Sair")
            }
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        )
        LazyColumn {
            items(userUiState.value.usersList.size) { user ->
                val user = userUiState.value.usersList[user]
                Column {
                    Text(
                        text = "Nome: ${user.username} | Senha: ${user.password}",
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                    Row {
                        // Edit
                        IconButton(
                            onClick = { usersViewModel.editDialogOpen(user) },
                            modifier = Modifier
                                .size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "",
                            )
                        }
                        // Delete
                        IconButton(
                            onClick = { usersViewModel.deleteUser(
                                databaseReference,
                                context,
                                user.id
                            ) },
                            modifier = Modifier
                                .size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "",
                            )
                        }
                    }
                }
            }
        }
    }
    if (userUiState.value.isEditOpen == true) {
        EditDialog(
            userId = userUiState.value.user?.id ?: "",
            username = userUiState.value.username,
            password = userUiState.value.password,
            onUsernameChange = { username -> usersViewModel.onUsernameChange(username) },
            onPasswordChange = { password -> usersViewModel.onPasswordChange(password) },
            editUser = { user -> usersViewModel.editUser(databaseReference, context, user) },
            closeDialog = { usersViewModel.editDialogClose() },
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun MainUIPreview() {
    // MainUI()
}