package com.example.firebasecrud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebasecrud.auth.AuthViewModel
import com.example.firebasecrud.navigation.AppNavigation
import com.example.firebasecrud.theme.FirebaseCRUDTheme
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {

    //Armazena a referência ao firebase realtime database
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cria ou usa o nó/node(SCHEMA) Users.
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        enableEdgeToEdge()

        val authViewModel : AuthViewModel by viewModels()

        setContent {
            FirebaseCRUDTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        databaseReference,
                        authViewModel,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirebaseCRUDTheme {
        //Greeting("Android")
    }
}