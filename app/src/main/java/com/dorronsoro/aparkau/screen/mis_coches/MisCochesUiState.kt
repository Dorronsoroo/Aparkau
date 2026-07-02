package com.dorronsoro.aparkau.screen.mis_coches

import com.dorronsoro.aparkau.model.Vehiculo

data class MisCochesUiState(
    val matricula: String = "",
    val modelo: String = "",
    val vehiculos: List<Vehiculo> = emptyList(),
    val isLoading: Boolean = false
)

