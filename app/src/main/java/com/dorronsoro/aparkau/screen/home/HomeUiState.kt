package com.dorronsoro.aparkau.screen.home

import com.dorronsoro.aparkau.model.Reserva

data class HomeUiState(
    val userId: String = "",
    val reservas: List<Reserva> = emptyList(),
    val isLoading: Boolean = false
)
