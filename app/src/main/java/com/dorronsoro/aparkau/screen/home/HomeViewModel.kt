package com.dorronsoro.aparkau.screen.home

import androidx.compose.runtime.mutableStateOf
import com.dorronsoro.aparkau.AparkauRoutes
import com.dorronsoro.aparkau.MakeItSoViewModel
import com.dorronsoro.aparkau.model.service.AccountService
import com.dorronsoro.aparkau.model.service.LogService
import com.dorronsoro.aparkau.model.service.ReservaService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val accountService: AccountService,
    private val reservaService: ReservaService,
    logService: LogService
) : MakeItSoViewModel(logService) {

    var uiState = mutableStateOf(HomeUiState())
        private set

    init {
        uiState.value = uiState.value.copy(userId = accountService.currentUserId)
        cargarReservas()
    }

    private fun cargarReservas() {
        uiState.value = uiState.value.copy(isLoading = true)
        launchCatching {
            try {
                val reservas = reservaService
                    .getReservasActivas(accountService.currentUserId)
                    .sortedBy { it.horaInicio }
                uiState.value = uiState.value.copy(reservas = reservas)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onReserveClick(openScreen: (String) -> Unit) {
        openScreen(AparkauRoutes.RESERVA_SCREEN)
    }

    fun onSignOutClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.signOut()
            openAndPopUp(AparkauRoutes.LOGIN_SCREEN, AparkauRoutes.HOME_SCREEN)
        }
    }
}
