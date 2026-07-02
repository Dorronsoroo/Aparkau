package com.dorronsoro.aparkau.screen.mi_cuenta

import androidx.compose.runtime.mutableStateOf
import com.dorronsoro.aparkau.AparkauRoutes
import com.dorronsoro.aparkau.MakeItSoViewModel
import com.dorronsoro.aparkau.common.AppText
import com.dorronsoro.aparkau.common.snackbar.SnackbarManager
import com.dorronsoro.aparkau.model.service.AccountService
import com.dorronsoro.aparkau.model.service.LogService
import com.dorronsoro.aparkau.model.service.UsuarioService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MiCuentaViewModel @Inject constructor(
    private val accountService: AccountService,
    private val usuarioService: UsuarioService,
    logService: LogService
) : MakeItSoViewModel(logService) {

    var uiState = mutableStateOf(MiCuentaUiState())
        private set

    private val email get() = uiState.value.email
    private val perfil get() = uiState.value.perfil

    init {
        cargarUsuario()
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }


    fun onGuardarClick() {
        if (email.isBlank()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        launchCatching {
            val resultado = usuarioService.actualizarUsuario(
                uid = accountService.currentUserId,
                email = email.trim(),
                perfil = perfil
            )
            resultado.getOrElse { error -> throw error }

            SnackbarManager.showMessage(AppText.datos_guardados)
        }
    }

    fun onMisCochesClick(openScreen: (String) -> Unit) {
        openScreen(AparkauRoutes.MIS_COCHES_SCREEN)
    }

    fun onVolverClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(AparkauRoutes.HOME_SCREEN, AparkauRoutes.MI_CUENTA_SCREEN)
    }

    fun onSignOutClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.signOut()
            openAndPopUp(AparkauRoutes.LOGIN_SCREEN, AparkauRoutes.HOME_SCREEN)
        }
    }

    private fun cargarUsuario() {
        uiState.value = uiState.value.copy(isLoading = true)
        launchCatching {
            try {
                val usuario = usuarioService.getUsuario(accountService.currentUserId)
                if (usuario != null) {
                    uiState.value = uiState.value.copy(
                        email = usuario.email,
                        perfil = usuario.perfil
                    )
                }
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }
}

