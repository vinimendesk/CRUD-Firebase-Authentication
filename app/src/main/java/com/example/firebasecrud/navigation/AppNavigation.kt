package com.example.firebasecrud.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebasecrud.auth.AuthViewModel
import com.example.firebasecrud.data.UsersViewModel
import com.example.firebasecrud.ui.LoginUI
import com.example.firebasecrud.ui.MainUI
import com.google.firebase.database.DatabaseReference

@Composable
fun AppNavigation(
    databaseReference: DatabaseReference,
    authViewModel: AuthViewModel,
    usersViewModel: UsersViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenType.LOGIN.toString()
    ) {

        composable(ScreenType.MAIN.toString()) {
            MainUI(navController ,databaseReference, authViewModel, usersViewModel)
        }

        composable(ScreenType.LOGIN.toString()) {
            LoginUI(navController, usersViewModel, authViewModel)
        }

    }

}

enum class ScreenType() {
    LOGIN, MAIN
}