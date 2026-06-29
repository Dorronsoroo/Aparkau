package com.dorronsoro.aparkau.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dorronsoro.aparkau.common.AppText
import com.dorronsoro.aparkau.common.composable.BasicButton
import com.dorronsoro.aparkau.common.ext.basicButton
import com.dorronsoro.aparkau.common.snackbar.SnackbarManager
import com.dorronsoro.aparkau.common.snackbar.SnackbarMessage
import com.dorronsoro.aparkau.theme.AparkauTheme
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun HomeScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        SnackbarManager.snackbarMessages.filterNotNull().collect { message ->
            val text = when (message) {
                is SnackbarMessage.ResourceSnackbar -> context.getString(message.message)
                is SnackbarMessage.StringSnackbar -> message.message
            }
            snackbarHostState.showSnackbar(text)
            SnackbarManager.clearSnackbarState()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        }
    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onSignOutClick = { viewModel.onSignOutClick(openAndPopUp) }
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onSignOutClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(AppText.home_welcome),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        BasicButton(
            text = AppText.sign_out,
            modifier = Modifier.basicButton(),
            action = onSignOutClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AparkauTheme {
        HomeScreenContent(
            uiState = HomeUiState(userId = "preview-user"),
            onSignOutClick = {}
        )
    }
}
