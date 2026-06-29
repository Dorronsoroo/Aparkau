package com.dorronsoro.aparkau.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.dorronsoro.aparkau.R
import com.dorronsoro.aparkau.common.AppText
import com.dorronsoro.aparkau.common.composable.BasicButton
import com.dorronsoro.aparkau.common.ext.basicButton
import com.dorronsoro.aparkau.common.snackbar.SnackbarManager
import com.dorronsoro.aparkau.common.snackbar.SnackbarMessage
import com.dorronsoro.aparkau.model.Reserva
import com.dorronsoro.aparkau.theme.AparkauTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.filterNotNull
import java.text.SimpleDateFormat
import java.util.Locale

private val dayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
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
            onReserveClick = { viewModel.onReserveClick(openScreen) },
            onSignOutClick = { viewModel.onSignOutClick(openAndPopUp) }
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onReserveClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(AppText.home_welcome),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.my_reservations),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.reservas.isEmpty()) {
            Text(
                text = stringResource(R.string.no_reservations),
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 4.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.reservas, key = { it.id }) { reserva ->
                    ReservaItem(reserva)
                }
            }
        }

        BasicButton(
            text = AppText.reserve_spot,
            modifier = Modifier.basicButton(),
            action = onReserveClick
        )

        BasicButton(
            text = AppText.sign_out,
            modifier = Modifier.basicButton(),
            action = onSignOutClick
        )
    }
}

@Composable
private fun ReservaItem(reserva: Reserva) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.plaza_numero, reserva.plazaId),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(
                    R.string.reserva_resumen,
                    formatDay(reserva.fechaReserva ?: reserva.horaInicio),
                    formatTime(reserva.horaInicio),
                    formatTime(reserva.horaFin)
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            if (reserva.matriculaVehiculo.isNotBlank()) {
                Text(
                    text = reserva.matriculaVehiculo,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun formatDay(timestamp: Timestamp?): String =
    timestamp?.toDate()?.let { dayFormat.format(it) } ?: "-"

private fun formatTime(timestamp: Timestamp?): String =
    timestamp?.toDate()?.let { timeFormat.format(it) } ?: "--:--"

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AparkauTheme {
        HomeScreenContent(
            uiState = HomeUiState(userId = "preview-user"),
            onReserveClick = {},
            onSignOutClick = {}
        )
    }
}
