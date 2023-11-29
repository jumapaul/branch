package com.example.branch.presentation.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.branch.domain.model.LoginCredential
import com.example.branch.presentation.login.viewmodel.LoginViewModel
import com.example.branch.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }


    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var isEmailError by remember {
        mutableStateOf(false)
    }

    var isPasswordError by remember {
        mutableStateOf(false)
    }

    val errorMessage = "Invalid email"
    val errorPasswordMessage = "Invalid password"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailError = Patterns.EMAIL_ADDRESS.matcher(it).matches().not()
            },
            label = { TextLabels(label = "Email", modifier) }, modifier = Modifier.fillMaxWidth(),
            placeholder = { TextLabels(label = "Input email", modifier) },
            colors = OutlinedTextFieldDefaults.colors(
                errorTextColor = if (isEmailError) Color.Red else MaterialTheme.colorScheme.primary,
            ),
            supportingText = {
                if (isEmailError) Text(
                    text = errorMessage, modifier = modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            },
            isError = isEmailError,
        )

        Spacer(modifier = modifier.size(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordError = password != email.reversed()
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { TextLabels(label = "Input password", modifier) },
            label = { TextLabels(label = "Password", modifier) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            supportingText = {
                if (isPasswordError) Text(
                    text = errorPasswordMessage, modifier = modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }

        )

        Spacer(modifier = modifier.size(10.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.login(
                        LoginCredential(
                            email, password
                        )
                    )
                }
            },
            modifier = modifier.fillMaxWidth()
        ) {
            TextLabels(label = "Submit", modifier = modifier)
        }
    }

    if (!viewModel.loginState.value.isLoading && viewModel.loginState.value.error != null) {
        Toast.makeText(
            context,
            "An error occurred",
            Toast.LENGTH_SHORT
        ).show()
    } else if (!viewModel.loginState.value.isLoading && viewModel.loginState.value.authKey?.auth_token != null) {
        navController.navigate(NavigationRoutes.HomeScreen.route)
    }
}

@Composable
fun TextLabels(
    label: String,
    modifier: Modifier
) {
    Text(
        text = label,
        modifier = modifier,
        fontSize = 15.sp
    )
}

