package com.dorronsoro.aparkau.screen.reserva

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dorronsoro.aparkau.R
import com.dorronsoro.aparkau.common.AppText
import com.dorronsoro.aparkau.common.composable.BasicToolbar
import com.dorronsoro.aparkau.common.ext.fieldModifier
import com.dorronsoro.aparkau.common.snackbar.SnackbarManager
import com.dorronsoro.aparkau.common.snackbar.SnackbarMessage
import com.dorronsoro.aparkau.model.EstadoPlaza
import com.dorronsoro.aparkau.model.Plaza
import com.dorronsoro.aparkau.model.TipoPlaza
import com.dorronsoro.aparkau.theme.AparkauTheme
import kotlinx.coroutines.flow.filterNotNull
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun ReservaScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: ReservaViewModel = hiltViewModel()
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
        ReservaScreenContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onMatriculaChange = viewModel::onMatriculaChange,
            onFechaChange = viewModel::onFechaChange,
            onHoraInicioChange = viewModel::onHoraInicioChange,
            onHoraFinChange = viewModel::onHoraFinChange,
            onReservarClick = viewModel::onReservarClick,
            onVolverClick = { viewModel.onVolverClick(openAndPopUp) }
        )
    }
}

@Composable
fun ReservaScreenContent(
    modifier: Modifier = Modifier,
    uiState: ReservaUiState,
    onMatriculaChange: (String) -> Unit,
    onFechaChange: (LocalDate) -> Unit,
    onHoraInicioChange: (LocalTime) -> Unit,
    onHoraFinChange: (LocalTime) -> Unit,
    onReservarClick: (Plaza) -> Unit,
    onVolverClick: () -> Unit
) {
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        BasicToolbar(title = AppText.reservas_title)

        OutlinedTextField(
            value = uiState.matricula,
            onValueChange = onMatriculaChange,
            singleLine = true,
            modifier = Modifier.fieldModifier(),
            label = { Text(stringResource(R.string.matricula)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, day -> onFechaChange(LocalDate.of(year, month + 1, day)) },
                        uiState.fecha.year,
                        uiState.fecha.monthValue - 1,
                        uiState.fecha.dayOfMonth
                    ).show()
                }
            ) {
                Text(stringResource(R.string.select_date, uiState.fecha.format(dateFormatter)))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute -> onHoraInicioChange(LocalTime.of(hour, minute)) },
                        uiState.horaInicio.hour,
                        uiState.horaInicio.minute,
                        true
                    ).show()
                }
            ) {
                Text(stringResource(R.string.select_start_time, uiState.horaInicio.format(timeFormatter)))
            }

            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute -> onHoraFinChange(LocalTime.of(hour, minute)) },
                        uiState.horaFin.hour,
                        uiState.horaFin.minute,
                        true
                    ).show()
                }
            ) {
                Text(stringResource(R.string.select_end_time, uiState.horaFin.format(timeFormatter)))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.plazas.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.no_plazas))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (uiState.plazasOficina.isNotEmpty()) {
                    item(key = "header_oficina") {
                        SectionHeader(title = stringResource(R.string.garaje_oficina_title))
                    }
                    items(uiState.plazasOficina, key = { "of_${it.id}" }) { plaza ->
                        PlazaItem(plaza = plaza, onReservarClick = onReservarClick)
                    }
                }

                if (uiState.plazasPago.isNotEmpty()) {
                    item(key = "header_pago") {
                        SectionHeader(title = stringResource(R.string.parking_pago_title))
                    }
                    items(uiState.plazasPago, key = { "pg_${it.id}" }) { plaza ->
                        PlazaItem(plaza = plaza, onReservarClick = onReservarClick)
                    }
                }
            }
        }

        Button(
            onClick = onVolverClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.back_to_home))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun PlazaItem(
    plaza: Plaza,
    onReservarClick: (Plaza) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.plaza_numero, plaza.id),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${tipoTexto(plaza.tipoEnum)} · ${estadoTexto(plaza.estadoEnum)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (plaza.estadoEnum == EstadoPlaza.LIBRE) {
                Button(onClick = { onReservarClick(plaza) }) {
                    Text(text = stringResource(R.string.reservar))
                }
            }
        }
    }
}

@Composable
private fun tipoTexto(tipo: TipoPlaza): String = when (tipo) {
    TipoPlaza.NORMAL -> stringResource(R.string.tipo_normal)
    TipoPlaza.ELECTRICA -> stringResource(R.string.tipo_electrica)
    TipoPlaza.MOTO -> stringResource(R.string.tipo_moto)
    TipoPlaza.PRIORITARIA -> stringResource(R.string.tipo_prioritaria)
    TipoPlaza.TANDEM -> stringResource(R.string.tipo_tandem)
}

@Composable
private fun estadoTexto(estado: EstadoPlaza): String = when (estado) {
    EstadoPlaza.LIBRE -> stringResource(R.string.estado_libre)
    EstadoPlaza.OCUPADA -> stringResource(R.string.estado_ocupada)
    EstadoPlaza.BLOQUEADA_POR_TANDEM -> stringResource(R.string.estado_bloqueada)
}

@Preview(showBackground = true)
@Composable
fun ReservaScreenPreview() {
    AparkauTheme {
        ReservaScreenContent(
            uiState = ReservaUiState(
                plazas = listOf(
                    Plaza(id = "2", tipo = "NORMAL", estado = "LIBRE"),
                    Plaza(id = "11", tipo = "ELECTRICA", estado = "OCUPADA"),
                    Plaza(id = "200", tipo = "MOTO", estado = "LIBRE"),
                    Plaza(id = "63", tipo = "NORMAL", estado = "LIBRE")
                ),
                matricula = "1234ABC"
            ),
            onMatriculaChange = {},
            onFechaChange = {},
            onHoraInicioChange = {},
            onHoraFinChange = {},
            onReservarClick = {},
            onVolverClick = {}
        )
    }
}

