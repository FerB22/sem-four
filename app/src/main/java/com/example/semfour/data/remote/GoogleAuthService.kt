package com.example.semfour.data.remote

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Servicio robusto de autenticación con Google e integración a Google Drive.
 * Soporta Google Sign-In con scope Drive y Credential Manager.
 */
@Singleton
class GoogleAuthService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val WEB_CLIENT_ID = "978045615455-94mmtn93s6qu2mjmm02q2bfvb71pvf28.apps.googleusercontent.com"
        val DRIVE_APPDATA_SCOPE = Scope("https://www.googleapis.com/auth/drive.appdata")
    }

    private val credentialManager = CredentialManager.create(context)

    private val _authState = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val isSignedIn: Boolean get() = _authState.value is AuthState.SignedIn
    val currentToken: String? get() = (_authState.value as? AuthState.SignedIn)?.idToken
    val currentEmail: String? get() = (_authState.value as? AuthState.SignedIn)?.email

    /**
     * Obtiene el Access Token de OAuth 2.0 con scope drive.appdata para llamadas a Google Drive API.
     */
    suspend fun getDriveAccessToken(forceRefresh: Boolean = false): String? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        try {
            val lastAccount = GoogleSignIn.getLastSignedInAccount(context)
            val androidAccount = lastAccount?.account ?: return@withContext null
            val scope = "oauth2:https://www.googleapis.com/auth/drive.appdata"
            val token = GoogleAuthUtil.getToken(context, androidAccount, scope)
            if (forceRefresh && token != null) {
                GoogleAuthUtil.clearToken(context, token)
                GoogleAuthUtil.getToken(context, androidAccount, scope)
            } else {
                token
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun invalidateCachedDriveToken(token: String) {
        try {
            GoogleAuthUtil.clearToken(context, token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        // Verificar si ya hay una cuenta iniciada previamente
        checkExistingUser()
    }

    private fun checkExistingUser() {
        val lastAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (lastAccount != null && !lastAccount.email.isNullOrBlank()) {
            _authState.value = AuthState.SignedIn(
                idToken = lastAccount.idToken ?: "",
                email = lastAccount.email ?: "",
                displayName = lastAccount.displayName ?: ""
            )
        }
    }

    /**
     * Devuelve el cliente de GoogleSignIn configurado con scopes de Drive.
     */
    fun getGoogleSignInClient(activityContext: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(WEB_CLIENT_ID)
            .requestScopes(DRIVE_APPDATA_SCOPE)
            .build()
        return GoogleSignIn.getClient(activityContext, gso)
    }

    /**
     * Obtiene el Intent para lanzar el selector de cuentas nativo de Google.
     */
    fun getSignInIntent(activityContext: Context): Intent {
        return getGoogleSignInClient(activityContext).signInIntent
    }

    /**
     * Procesa el resultado de la Activity tras seleccionar la cuenta de Google.
     */
    fun handleSignInResult(data: Intent?): AuthResult {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            
            val email = account.email ?: ""
            val idToken = account.idToken ?: ""
            val displayName = account.displayName ?: ""

            _authState.value = AuthState.SignedIn(
                idToken = idToken,
                email = email,
                displayName = displayName
            )
            AuthResult.Success(email)
        } catch (e: ApiException) {
            val message = when (e.statusCode) {
                12501 -> "Inicio de sesión cancelado por el usuario"
                12500 -> "Error en Google Play Services (${e.statusCode})"
                10 -> "Error de configuración de credenciales (código 10: verifica SHA-1)"
                7 -> "Error de conexión a internet"
                else -> "Error de Google Sign-In: ${e.statusCode} ${e.message}"
            }
            AuthResult.Error(message)
        } catch (e: Exception) {
            AuthResult.Error("Error inesperado: ${e.message}")
        }
    }

    /**
     * Inicia el flujo usando Credential Manager moderno como método alternativo.
     */
    suspend fun signInWithCredentialManager(activityContext: Context): AuthResult {
        val activity = activityContext.findActivity()
        if (activity == null) {
            return AuthResult.Error("No se pudo obtener la Activity para Credential Manager")
        }

        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(activity, request)
            val credential = result.credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                _authState.value = AuthState.SignedIn(
                    idToken = googleCredential.idToken,
                    email = googleCredential.id,
                    displayName = googleCredential.displayName ?: ""
                )
                AuthResult.Success(googleCredential.id)
            } else {
                AuthResult.Error("Tipo de credencial no soportado")
            }
        } catch (e: NoCredentialException) {
            AuthResult.Error("No hay cuentas disponibles en el dispositivo: ${e.message}")
        } catch (e: GetCredentialCancellationException) {
            AuthResult.Cancelled
        } catch (e: GetCredentialException) {
            AuthResult.Error("Error al obtener credencial: ${e.message}")
        } catch (e: Exception) {
            AuthResult.Error("Error de autenticación: ${e.message}")
        }
    }

    /**
     * Cierra la sesión en Google SignIn y Credential Manager.
     */
    suspend fun signOut(activityContext: Context? = null) {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (_: Exception) { }

        try {
            if (activityContext != null) {
                getGoogleSignInClient(activityContext).signOut()
            } else {
                getGoogleSignInClient(context).signOut()
            }
        } catch (_: Exception) { }

        _authState.value = AuthState.SignedOut
    }
}

// ── Helper para extraer Activity ─────────────────────────────────────────────

fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

// ── Estados de autenticación ──────────────────────────────────────────────────

sealed class AuthState {
    data object SignedOut : AuthState()
    data class SignedIn(
        val idToken: String,
        val email: String,
        val displayName: String
    ) : AuthState()
}

sealed class AuthResult {
    data class Success(val email: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
    data object Cancelled : AuthResult()
}
