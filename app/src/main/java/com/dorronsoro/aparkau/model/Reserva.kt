package com.dorronsoro.aparkau.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Reserva(
    @DocumentId val id: String = "",
    val usuarioId: String = "",
    val plazaId: String = "",
    val matriculaVehiculo: String = "",
    val estado: EstadoReserva = EstadoReserva.AGENDADA,
    val fechaReserva: Timestamp? = null,
    val horaInicio: Timestamp? = null,
    val horaFin: Timestamp? = null,
    val horaSalidaEstimada: Timestamp? = null,
    val checkInReal: Timestamp? = null,
    val checkOutReal: Timestamp? = null
)
