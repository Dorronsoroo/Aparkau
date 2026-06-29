package com.dorronsoro.aparkau.model.service

import com.dorronsoro.aparkau.model.Reserva

interface ReservaService {
    suspend fun crearReserva(reserva: Reserva): String
    suspend fun getReservasActivas(usuarioId: String): List<Reserva>
    // Aquí irán las funciones del algoritmo Tetris
}