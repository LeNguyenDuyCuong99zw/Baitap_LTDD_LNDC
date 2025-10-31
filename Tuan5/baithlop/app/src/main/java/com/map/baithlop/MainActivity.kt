package com.map.baithlop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.map.baithlop.ui.theme.BaithlopTheme

sealed class LoginUiState {
    object Idle : LoginUiState()
    data class Error(val title: String, val message: String) : LoginUiState()
    data class Success(val displayName: String?, val email: String?) : LoginUiState()
}

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        val webClientResId = resources.getIdentifier("default_web_client_id", "string", packageName)
        val webClientId = if (webClientResId != 0) getString(webClientResId) else null

        if (webClientId == null) {
            Log.w("Auth", "default_web_client_id not found. Firebase Auth with Google will require adding a Web OAuth client in Firebase console and updating google-services.json")
        }

        val gsoBuilder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
        if (webClientId != null) {
            gsoBuilder.requestIdToken(webClientId)
        }
        val gso = gsoBuilder.build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val uiState = mutableStateOf<LoginUiState>(
            if (auth.currentUser != null) LoginUiState.Success(auth.currentUser?.displayName, auth.currentUser?.email) else LoginUiState.Idle
        )

        val signInLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            val data: Intent? = result.data
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken) { successMsg, errorMsg ->
                        if (successMsg != null) {
                            val user = auth.currentUser
                            uiState.value = LoginUiState.Success(user?.displayName, user?.email)
                        } else if (errorMsg != null) {
                            uiState.value = LoginUiState.Error("Google Sign-In Failed", errorMsg)
                        }
                        if (successMsg != null) Log.d("Auth", "Success: $successMsg")
                        if (errorMsg != null) Log.w("Auth", "Error: $errorMsg")
                    }
                } else {
                    uiState.value = LoginUiState.Error("Google Sign-In Failed", "No id token returned. Check google-services.json Web OAuth client.")
                }
            } catch (e: ApiException) {
                val message = when (e.statusCode) {
                    -1 -> "User canceled the Google sign-in process."
                    else -> e.localizedMessage ?: "Google sign-in failed"
                }
                uiState.value = LoginUiState.Error("Google Sign-In Failed", message)
                Log.w("Auth", "Google sign in failed", e)
            }
        }

        setContent {
            BaithlopTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Pass current uiState and interaction lambdas
                    LoginScreen(
                        uiState = uiState.value,
                        onLoginClick = {
                            val signInIntent = googleSignInClient.signInIntent
                            signInLauncher.launch(signInIntent)
                        },
                        onReset = {
                            // reset to idle
                            uiState.value = LoginUiState.Idle
                        }
                    )
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, callback: (success: String?, error: String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    callback("Success: ${user?.email}", null)
                } else {
                    callback(null, "Authentication Failed: ${task.exception?.localizedMessage}")
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(uiState: LoginUiState, onLoginClick: () -> Unit, onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginButton(onClick = onLoginClick)

        when (uiState) {
            is LoginUiState.Error -> ErrorCard(uiState, onReset)
            is LoginUiState.Success -> SuccessCard(uiState)
            else -> Unit // Idle state
        }
    }
}

@Composable
private fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1EA1FF)),
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(48.dp)
    ) {
        Text(text = "Login by Gmail", color = Color.White)
    }
}

@Composable
private fun ErrorCard(uiState: LoginUiState.Error, onReset: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF82EA00)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = uiState.title, style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Text(text = uiState.message, modifier = Modifier.padding(top = 8.dp), color = Color.Black)
            TextButton(onClick = onReset, modifier = Modifier.align(Alignment.End)) {
                Text("Try again", color = Color(0xFF1EA1FF))
            }
        }
    }
}

@Composable
private fun SuccessCard(uiState: LoginUiState.Success) {
    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF12577E)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Success!", style = MaterialTheme.typography.titleMedium)
            Text(text = "Hi ${uiState.displayName ?: ""}\n${uiState.email ?: ""}", modifier = Modifier.padding(top = 8.dp))
            Text(text = "Welcome to UTHSmartTasks", modifier = Modifier.padding(top = 8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    BaithlopTheme {
        LoginScreen(uiState = LoginUiState.Idle, onLoginClick = {}, onReset = {})
    }
}
