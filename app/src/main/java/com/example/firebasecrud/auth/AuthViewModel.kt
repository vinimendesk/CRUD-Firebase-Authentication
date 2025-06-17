package com.example.firebasecrud.auth
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class AuthViewModel: ViewModel() {

    // Armazena uma instância do firebase.
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    // Função para verificar se o usuário está logado.
    fun checkAuthState() {
        // Se não existe usuário atual defina como não-autenticado, caso contrário defina como autenticado.
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    // Função para logar no sistema.
    fun login(email: String, password: String) {

        // Se email ou password forem vazios, exiba um erro e finalize a função.
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email ou senha não podem estarem vazios.")
            return
        }

        // Defina como loading enquanto o sistema não realiza o login.
        _authState.value = AuthState.Loading
        // Realiza login com email e senha.
        auth.signInWithEmailAndPassword(email, password)
            // Quando a chamada for completa, verifica se o usuário foi logado com sucesso.
            .addOnCompleteListener { task ->
                // Se logado, atualize o estado de autenticação para autenticado.
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                // Caso contrário, use 
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Algo deu errado.")
                }
            }

    }

}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}