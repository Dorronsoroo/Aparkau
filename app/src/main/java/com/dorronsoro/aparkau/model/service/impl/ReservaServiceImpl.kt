package com.dorronsoro.aparkau.model.service.impl

import com.dorronsoro.aparkau.model.EstadoReserva
import com.dorronsoro.aparkau.model.Reserva
import com.dorronsoro.aparkau.model.service.ReservaService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReservaServiceImpl @Inject constructor() : ReservaService {

    // Lazy para que la instancia se cree una sola vez cuando se necesite
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    companion object {
        private const val COLECCION_RESERVAS = "reservas"
        private const val CAMPO_USUARIO_ID = "usuarioId"
    }

    /**
     * Crea una nueva reserva en Firestore y devuelve el ID del documento generado.
     */
    override suspend fun crearReserva(reserva: Reserva): String {
        return firestore
            .collection(COLECCION_RESERVAS)
            .add(reserva)
            .await()
            .id
    }

    /**
     * Devuelve las reservas de un usuario que todavía no han finalizado
     * (estado AGENDADA o ACTIVA).
     *
     * Filtramos el estado en memoria para no requerir un índice compuesto en Firestore.
     */
    override suspend fun getReservasActivas(usuarioId: String): List<Reserva> {
        return firestore
            .collection(COLECCION_RESERVAS)
            .whereEqualTo(CAMPO_USUARIO_ID, usuarioId)
            .get()
            .await()
            .toObjects(Reserva::class.java)
            .filter {
                it.estado == EstadoReserva.AGENDADA || it.estado == EstadoReserva.ACTIVA
            }
    }
}



