package com.packt.kisumu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.packt.kisumu.ui.theme.KisumuTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KisumuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val loginViewModel: LoginViewModel = viewModel()
    val uiState: UIState by loginViewModel.uiState.collectAsState(initial = UIState())
    Scaffold(
        topBar = {
                 TopAppBar(title = { Text(text = "GDG Kisumu") })

        },
        content = {
            LoginScreenContent(
                uiState = uiState,
                modifier = Modifier.padding(it),
                onEmailChange = {email ->
                    loginViewModel.handleLoginActions(LoginActions.EmailChanged(email))
                },
                onPasswordChange = {
                    loginViewModel.handleLoginActions(LoginActions.PasswordChanged(it))
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    modifier: Modifier,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    uiState: UIState
) {
    Card(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email ?: "" , onValueChange = {
                    onEmailChange(it)
            } )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = uiState.password ?: "" , onValueChange = {

                    onPasswordChange(it)
            } )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = uiState.isValid(),
                onClick = { /*TODO*/ }) {
                Text(text = "Login")
            }
        }
    }

}

class LoginViewModel: ViewModel() {
    val uiState  = MutableStateFlow(UIState())

    fun handleLoginActions(actions: LoginActions) {
        when(actions) {
            is LoginActions.EmailChanged -> {
                uiState.update {
                    it.copy(email = actions.email)
                }
            }
            is LoginActions.PasswordChanged -> {
                uiState.update {
                    it.copy(password = actions.password)
                }
            }

            is LoginActions.Subt -> {
                // api
                uiState.value.email
                uiState.value.password
            }
        }
    }

}

data class UIState(
    val email: String? = null,
    val password: String? = null
) {
    fun isValid() : Boolean {
        return !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }
}

sealed class LoginActions {
    data class EmailChanged(val email: String): LoginActions()
    data class PasswordChanged(val password: String): LoginActions()
    data class Subt(val password: String): LoginActions()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KisumuTheme {
        LoginScreen()
    }
}