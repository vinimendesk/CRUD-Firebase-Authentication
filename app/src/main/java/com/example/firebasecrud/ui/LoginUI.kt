package com.example.firebasecrud.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebasecrud.data.Users
import com.example.firebasecrud.data.UsersViewModel

@Composable
fun LoginUI(
    usersViewModel: UsersViewModel = viewModel(),
    modifier: Modifier
) {

    val userUiState = usersViewModel.uiState.collectAsState()

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
        Button(
            onClick = {

                usersViewModel.onUsernameChange("")
                usersViewModel.onPasswordChange("")
            },

        ) {
            Text(text = "Fazer Login")
        }
    }

}