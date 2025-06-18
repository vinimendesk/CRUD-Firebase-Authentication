package com.example.firebasecrud.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebasecrud.auth.AuthState
import com.example.firebasecrud.auth.AuthViewModel
import com.example.firebasecrud.data.UsersViewModel

@Composable
fun LoginUI(
    usersViewModel: UsersViewModel = viewModel(),
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    val userUiState = usersViewModel.uiState.collectAsState()

    val authState = authViewModel.authState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(authState) {
        when(authState.value) {
            is AuthState.Authenticated -> {/*
                navController.navigate("MainUI")
                popUpto("LoginUI") { inclusive = true } */
            }
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
                ).show()
            else -> Unit
        }
    }

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TextField para adicionar o nome(email).
        TextField(
            value = userUiState.value.username,
            onValueChange = { username -> usersViewModel.onUsernameChange(username) },
            label = { Text(text = "Nome") },
            placeholder = { Text(text = "Insira seu nome de usuário") },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // TextField para adicionar a senha.
        TextField(
            value = userUiState.value.password,
            onValueChange = { password -> usersViewModel.onPasswordChange(password) },
            label = { Text(text = "Senha") },
            placeholder = { Text(text = "Insira a sua senha") },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Botão para realizar o login
        Button(
            enabled = authState.value != AuthState.Loading,
            onClick = {
                // Realiza o login.
                authViewModel.login(userUiState.value.username, userUiState.value.password)
                // Atualiza os dados no UiState.
                usersViewModel.onUsernameChange("")
                usersViewModel.onPasswordChange("")
            },

        ) {
            Text(text = "Fazer Login")
        }
    }

}