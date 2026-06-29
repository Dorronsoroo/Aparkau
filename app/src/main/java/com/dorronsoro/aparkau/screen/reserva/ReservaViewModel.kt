package com.dorronsoro.aparkau.screen.reserva

import androidx.compose.runtime.mutableStateOf
import com.dorronsoro.aparkau.AparkauRoutes
import com.dorronsoro.aparkau.common.AppText
import com.dorronsoro.aparkau.MakeItSoViewModel
import com.dorronsoro.aparkau.model.EstadoPlaza
import com.dorronsoro.aparkau.model.EstadoReserva
import com.dorronsoro.aparkau.model.Plaza
import com.dorronsoro.aparkau.model.Reserva
import com.dorronsoro.aparkau.common.snackbar.SnackbarManager
import com.dorronsoro.aparkau.model.service.AccountService
import com.dorronsoro.aparkau.model.service.LogService
import com.dorronsoro.aparkau.model.service.PlazaService
import com.dorronsoro.aparkau.model.service.ReservaService
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReservaViewModel @Inject constructor(
    private val accountService: AccountService,
    private val plazaService: PlazaService,
    private val reservaService: ReservaService,
    logService: LogService
) : MakeItSoViewModel(logService) {

    var uiState = mutableStateOf(ReservaUiState())
        private set

    private val matricula get() = uiState.value.matricula
    private val fecha get() = uiState.value.fecha
    private val horaInicio get() = uiState.value.horaInicio
    private val horaFin get() = uiState.value.horaFin

    companion object {
        private const val MAX_DIAS_ANTELACION = 7L
        private const val MAX_HORAS_DURACION = 9L
    }

    init {
        cargarPlazas()
    }

    fun onMatriculaChange(newValue: String) {
        uiState.value = uiState.value.copy(matricula = newValue.uppercase())
    }

    fun onFechaChange(newValue: LocalDate) {
        uiState.value = uiState.value.copy(fecha = newValue)
    }

    fun onHoraInicioChange(newValue: LocalTime) {
        uiState.value = uiState.value.copy(horaInicio = newValue)
    }

    fun onHoraFinChange(newValue: LocalTime) {
        uiState.value = uiState.value.copy(horaFin = newValue)
    }

    fun onVolverClick(openAndPopUp: (String, String) -> Unit) {
        // Volvemos a Home recreándolo para que recargue la lista de reservas
        openAndPopUp(AparkauRoutes.HOME_SCREEN, AparkauRoutes.HOME_SCREEN)
    }

    private fun cargarPlazas() {
        uiState.value = uiState.value.copy(isLoading = true)
        launchCatching {
            try {
                val plazas = plazaService.getTodasLasPlazas()
                    .sortedWith(
                        compareBy(
                            { it.id.takeWhile { c -> c.isDigit() }.toIntOrNull() ?: Int.MAX_VALUE },
                            { it.id }
                        )
                    )
                uiState.value = uiState.value.copy(plazas = plazas)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onReservarClick(plaza: Plaza) {
        if (plaza.estadoEnum != EstadoPlaza.LIBRE) {
            SnackbarManager.showMessage(AppText.plaza_no_disponible)
            return
        }
        if (matricula.isBlank()) {
            SnackbarManager.showMessage(AppText.matricula_error)
            return
        }
        if (!validarFechaYHora()) return

        launchCatching {
            val zona = ZoneId.systemDefault()
            val inicio = Timestamp(Date.from(fecha.atTime(horaInicio).atZone(zona).toInstant()))
            val fin = Timestamp(Date.from(fecha.atTime(horaFin).atZone(zona).toInstant()))
            val dia = Timestamp(Date.from(fecha.atStartOfDay(zona).toInstant()))

            val reserva = Reserva(
                usuarioId = accountService.currentUserId,
                plazaId = plaza.id,
                matriculaVehiculo = matricula,
                estado = EstadoReserva.AGENDADA,
                fechaReserva = dia,
                horaInicio = inicio,
                horaFin = fin
            )

            // 1. Crear la reserva en Firestore
            reservaService.crearReserva(reserva)

            // 2. Marcar la plaza como OCUPADA
            plazaService.actualizarEstadoPlaza(plaza.id, EstadoPlaza.OCUPADA.name)

            // 3. Avisar y refrescar el listado de plazas
            SnackbarManager.showMessage(AppText.reserva_creada)
            cargarPlazas()
        }
    }

    /** Valida las reglas de negocio: máx. 7 días de antelación y máx. 9 horas de duración. */
    private fun validarFechaYHora(): Boolean {
        val hoy = LocalDate.now()

        if (fecha.isBefore(hoy)) {
            SnackbarManager.showMessage(AppText.fecha_pasada_error)
            return false
        }
        if (fecha.isAfter(hoy.plusDays(MAX_DIAS_ANTELACION))) {
            SnackbarManager.showMessage(AppText.fecha_max_error)
            return false
        }
        if (!horaFin.isAfter(horaInicio)) {
            SnackbarManager.showMessage(AppText.hora_invalida_error)
            return false
        }
        if (Duration.between(horaInicio, horaFin).toMinutes() > MAX_HORAS_DURACION * 60) {
            SnackbarManager.showMessage(AppText.duracion_error)
            return false
        }
        return true
    }
}
