package com.example.lms.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    login: () -> Unit
) {
    val username = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            UsernameTextField(username = username.value, onChange = {username.value = it})

            PasswordTextField(password = password.value, onChange = {password.value = it})

            LoginButton {
                login()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameTextField(username: String, onChange: (String)->Unit){
    TextField(
        value = username,
        onValueChange = {onChange(it)},
        singleLine = true,
        placeholder = {
            Text("Username")
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(password: String, onChange: (String) -> Unit){
    TextField(
        value = password,
        onValueChange = {onChange(it)},
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        placeholder = {
            Text("Password")
        }
    )
}

@Composable
fun LoginButton(onClick: ()->Unit){
    Button(onClick = {
        onClick()
    }) {
        Text(
            text = "Login"
        )
    }
}