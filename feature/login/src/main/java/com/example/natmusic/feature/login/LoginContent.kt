package com.example.natmusic.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.natmusic.core.common_ui.component.NatButton
import com.example.natmusic.core.common_ui.component.NatTextAction
import com.example.natmusic.core.common_ui.component.NatTextField

/**
 * Pure stateless UI for the Login feature. No ViewModel or Contract dependencies.
 */
@Composable
fun LoginContent(
    state: LoginContract.State,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleMode: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (state.isRegister) "Create Account" else "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            NatTextField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = "Email",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            NatTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = "Password",
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (state.isRegister) ImeAction.Next else ImeAction.Done
                )
            )

            if (state.isRegister) {
                NatTextField(
                    value = state.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    placeholder = "Confirm Password",
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            NatButton(
                text = if (state.isRegister) "Sign Up" else "Login",
                onClick = onSubmit
            )

            state.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            NatTextAction(
                text = if (state.isRegister) "Already have an account?" else "Don't have an account?",
                actionText = if (state.isRegister) "Login" else "Sign Up",
                onActionClick = onToggleMode
            )
        }
    }
}

@Preview
@Composable
private fun LoginContentPreview() {
    MaterialTheme {
        LoginContent(
            state = LoginContract.State(isRegister = false),
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}
