package com.dorronsoro.aparkau.model.service

import com.dorronsoro.aparkau.model.Reserva

interface ReservaService {
    suspend fun crearReserva(reserva: Reserva): String
    suspend fun getReservasActivas(usuarioId: String): List<Reserva>
    suspend fun getTodasLasReservasActivas(): List<Reserva>
    suspend fun eliminarReserva(reservaId: String)
    // Aquí irán las funciones del algoritmo Tetris
}