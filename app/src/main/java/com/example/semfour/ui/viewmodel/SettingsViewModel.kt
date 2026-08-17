package com.example.semfour.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semfour.data.remote.AuthResult
import com.example.semfour.data.remote.AuthState
import com.example.semfour.data.remote.GoogleAuthService
import com.example.semfour.data.repository.StudyRepository
import com.example.semfour.data.repository.SyncRepository
import com.example.semfour.data.repository.SyncState
import com.example.semfour.worker.SyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Configuración y Sincronización con Google Drive.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authService: GoogleAuthService,
    private val syncRepository: SyncRepository,
    private val studyRepository: StudyRepository
) : ViewModel() {

    val authState: StateFlow<AuthState> = authService.authState
    val syncState: StateFlow<SyncState> = syncRepository.syncState

    private val _exp2Enabled = MutableStateFlow(false)
    val exp2Enabled: StateFlow<Boolean> = _exp2Enabled.asStateFlow()

    private val _exp3Enabled = MutableStateFlow(false)
    val exp3Enabled: StateFlow<Boolean> = _exp3Enabled.asStateFlow()

    private val _englishEnabled = MutableStateFlow(false)
    val englishEnabled: StateFlow<Boolean> = _englishEnabled.asStateFlow()

    private val _uiEvents = MutableSharedFlow<String>()
    val uiEvents: SharedFlow<String> = _uiEvents.asSharedFlow()

    init {
        loadExperienceStates()
    }

    fun loadExperienceStates() {
        viewModelScope.launch {
            _exp2Enabled.value = studyRepository.isExperienceEnabled(2)
            _exp3Enabled.value = studyRepository.isExperienceEnabled(3)
            _englishEnabled.value = studyRepository.isEnglishEnabled()
        }
    }

    fun toggleEnglish(enabled: Boolean) {
        viewModelScope.launch {
            studyRepository.setEnglishEnabled(enabled)
            _englishEnabled.value = enabled
            val accion = if (enabled) "añadida con éxito" else "removida"
            _uiEvents.emit("Asignatura Inglés Intermedio 1 $accion")
        }
    }

    fun toggleExperience(expNumber: Int, enabled: Boolean) {
        viewModelScope.launch {
            studyRepository.setExperienceEnabled(expNumber, enabled)
            if (expNumber == 2) {
                _exp2Enabled.value = enabled
                if (!enabled && _exp3Enabled.value) {
                    // Si desactiva la 2, también desactivamos la 3 por orden lógico
                    studyRepository.setExperienceEnabled(3, false)
                    _exp3Enabled.value = false
                }
            } else if (expNumber == 3) {
                _exp3Enabled.value = enabled
                if (enabled && !_exp2Enabled.value) {
                    // Si activa la 3, habilitamos la 2 automáticamente
                    studyRepository.setExperienceEnabled(2, true)
                    _exp2Enabled.value = true
                }
            }
            val accion = if (enabled) "añadidos" else "removidos"
            _uiEvents.emit("Temas y evaluaciones de la Experiencia $expNumber $accion")
        }
    }

    /**
     * Obtiene el Intent para el ActivityResultLauncher del Google Sign-In nativo.
     */
    fun getSignInIntent(context: Context): Intent {
        return authService.getSignInIntent(context)
    }

    /**
     * Procesa el resultado de la Activity tras el selector de Google.
     */
    fun onSignInResult(data: Intent?) {
        viewModelScope.launch {
            when (val result = authService.handleSignInResult(data)) {
                is AuthResult.Success -> {
                    _uiEvents.emit("¡Conectado exitosamente como ${result.email}!")
                    // Sincronizar automáticamente tras conectar
                    syncNow()
                }
                is AuthResult.Error -> {
                    _uiEvents.emit(result.message)
                }
                is AuthResult.Cancelled -> {
                    _uiEvents.emit("Inicio de sesión cancelado")
                }
            }
        }
    }

    /**
     * Intenta inicio de sesión con Credential Manager si es necesario.
     */
    fun signInWithCredentialManager(activityContext: Context) {
        viewModelScope.launch {
            when (val result = authService.signInWithCredentialManager(activityContext)) {
                is AuthResult.Success -> {
                    _uiEvents.emit("¡Conectado exitosamente como ${result.email}!")
                    syncNow()
                }
                is AuthResult.Error -> {
                    _uiEvents.emit(result.message)
                }
                is AuthResult.Cancelled -> {
                    _uiEvents.emit("Inicio de sesión cancelado")
                }
            }
        }
    }

    /**
     * Cierra la sesión de Google.
     */
    fun signOut(context: Context? = null) {
        viewModelScope.launch {
            authService.signOut(context)
            _uiEvents.emit("Sesión de Google cerrada")
        }
    }

    /**
     * Ejecuta la sincronización manual inmediata con Google Drive.
     */
    fun syncNow(context: Context? = null) {
        viewModelScope.launch {
            if (authService.authState.value is AuthState.SignedOut) {
                _uiEvents.emit("Conecta tu cuenta de Google primero para sincronizar")
                return@launch
            }

            _uiEvents.emit("Obteniendo autorización de Google Drive...")
            val token = authService.getDriveAccessToken()
            if (token.isNullOrBlank()) {
                val errorMsg = "No se pudo obtener el token OAuth de Drive. Vuelve a iniciar sesión."
                _uiEvents.emit(errorMsg)
                syncRepository.reportError(errorMsg)
                return@launch
            }

            _uiEvents.emit("Sincronizando con Google Drive...")
            syncRepository.sync(token)

            if (context != null) {
                SyncWorker.enqueueImmediate(context)
            }
        }
    }
}
