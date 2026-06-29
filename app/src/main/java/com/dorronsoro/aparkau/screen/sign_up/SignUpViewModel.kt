package com.dorronsoro.aparkau.screen.sign_up

import androidx.compose.runtime.mutableStateOf
import com.dorronsoro.aparkau.AparkauRoutes
import com.dorronsoro.aparkau.MakeItSoViewModel
import com.dorronsoro.aparkau.common.AppText
import com.dorronsoro.aparkau.common.ext.isValidEmail
import com.dorronsoro.aparkau.common.ext.isValidPassword
import com.dorronsoro.aparkau.common.ext.passwordMatches
import com.dorronsoro.aparkau.common.snackbar.SnackbarManager
import com.dorronsoro.aparkau.model.PerfilUsuario
import com.dorronsoro.aparkau.model.service.AccountService
import com.dorronsoro.aparkau.model.service.LogService
import com.dorronsoro.aparkau.model.service.UsuarioService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    private val usuarioService: UsuarioService,
    logService: LogService
) : MakeItSoViewModel(logService) {

    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password
    private val repeatPassword get() = uiState.value.repeatPassword
    private val perfil get() = uiState.value.perfil

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onPerfilChange(newValue: PerfilUsuario) {
        uiState.value = uiState.value.copy(perfil = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }
        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }
        if (!password.passwordMatches(repeatPassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }

        launchCatching {
            // 1. Crear cuenta en Firebase Auth
            accountService.createAccount(email, password)

            // 2. Guardar el perfil en Firestore usando el UID recién generado
            val uid = accountService.currentUserId
            val resultado = usuarioService.guardarUsuario(
                uid = uid,
                email = email,
                perfil = perfil
            )

            // 3. Comprobar si Firestore devolvió error
            resultado.getOrElse { error ->
                // El bloque launchCatching capturará el lanzamiento
                // y mostrará el mensaje en el Snackbar automáticamente
                throw error
            }

            // 4. Navegar a Home solo si todo fue exitoso
            openAndPopUp(AparkauRoutes.HOME_SCREEN, AparkauRoutes.SIGN_UP_SCREEN)
        }
    }

    fun onLoginClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(AparkauRoutes.LOGIN_SCREEN, AparkauRoutes.SIGN_UP_SCREEN)
    }
}