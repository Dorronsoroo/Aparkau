package com.dorronsoro.aparkau.screen.reserva

import com.dorronsoro.aparkau.model.Plaza
import com.dorronsoro.aparkau.model.ZonaPlaza
import java.time.LocalDate
import java.time.LocalTime

data class ReservaUiState(
    val plazas: List<Plaza> = emptyList(),
    val matricula: String = "",
    val fecha: LocalDate = LocalDate.now(),
    val horaInicio: LocalTime = LocalTime.of(8, 0),
    val horaFin: LocalTime = LocalTime.of(17, 0),
    val isLoading: Boolean = false
) {
    val plazasOficina: List<Plaza>
        get() = plazas.filter { it.zonaEnum == ZonaPlaza.OFICINA }

    val plazasPago: List<Plaza>
        get() = plazas.filter { it.zonaEnum == ZonaPlaza.PAGO }
}
