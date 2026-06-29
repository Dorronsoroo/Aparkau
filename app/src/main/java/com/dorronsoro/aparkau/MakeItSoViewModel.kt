package com.dorronsoro.aparkau

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorronsoro.aparkau.common.snackbar.SnackbarManager
import com.dorronsoro.aparkau.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.dorronsoro.aparkau.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class MakeItSoViewModel(private val logService: LogService) : ViewModel() {

    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                SnackbarManager.showMessage(throwable.toSnackbarMessage())
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}

