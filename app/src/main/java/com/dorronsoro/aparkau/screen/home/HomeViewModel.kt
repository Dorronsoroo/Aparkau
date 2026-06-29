package com.dorronsoro.aparkau.screen.home

import androidx.compose.runtime.mutableStateOf
import com.dorronsoro.aparkau.AparkauRoutes
import com.dorronsoro.aparkau.MakeItSoViewModel
import com.dorronsoro.aparkau.model.service.AccountService
import com.dorronsoro.aparkau.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : MakeItSoViewModel(logService) {

    var uiState = mutableStateOf(HomeUiState())
        private set

    init {
        uiState.value = uiState.value.copy(userId = accountService.currentUserId)
    }

    fun onSignOutClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.signOut()
            openAndPopUp(AparkauRoutes.LOGIN_SCREEN, AparkauRoutes.HOME_SCREEN)
        }
    }
}

