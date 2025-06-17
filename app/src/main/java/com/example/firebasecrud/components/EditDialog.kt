package com.example.firebasecrud.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.firebasecrud.data.Users

@Composable
fun EditDialog(
    userId: String,
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    editUser: (Users) -> Unit,
    closeDialog: () -> Unit,
    modifier: Modifier
) {
    Dialog(
        onDismissRequest = closeDialog,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Column() {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = 4.dp)
            ) {
                IconButton(
                    onClick = closeDialog,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Nome
            TextField(
                value = username,
                onValueChange = { username -> onUsernameChange(username) },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Senha
            TextField(
                value = password,
                onValueChange = { senha -> onPasswordChange(senha) },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Editar
            Button(
                onClick = { editUser(Users(id = userId, username = username, password = password))},
                content = { Text(text = "Editar") }
            )
        }
    }
}