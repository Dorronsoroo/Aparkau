package com.dorronsoro.aparkau.screen.reserva

import com.dorronsoro.aparkau.model.GrupoPlaza
import com.dorronsoro.aparkau.model.Plaza
import com.dorronsoro.aparkau.model.Vehiculo
import com.dorronsoro.aparkau.model.ZonaPlaza
import com.dorronsoro.aparkau.model.agruparTandem
import java.time.LocalDate
import java.time.LocalTime

data class ReservaUiState(
    val plazas: List<Plaza> = emptyList(),
    val vehiculos: List<Vehiculo> = emptyList(),
    val matriculaSeleccionada: String = "",
    val fecha: LocalDate = LocalDate.now(),
    val horaInicio: LocalTime = LocalTime.of(8, 0),
    val horaFin: LocalTime = LocalTime.of(17, 0),
    val isLoading: Boolean = false,
    /** Mapa de plazaId -> nombre completo del ocupante (solo plazas OCUPADAS) */
    val ocupantePorPlaza: Map<String, String> = emptyMap()
) {
    val plazasOficina: List<Plaza>
        get() = plazas.filter { it.zonaEnum == ZonaPlaza.OFICINA }

    val plazasPago: List<Plaza>
        get() = plazas.filter { it.zonaEnum == ZonaPlaza.PAGO }

    val gruposOficina: List<GrupoPlaza>
        get() = plazasOficina.agruparTandem()

    val gruposPago: List<GrupoPlaza>
        get() = plazasPago.agruparTandem()
}
