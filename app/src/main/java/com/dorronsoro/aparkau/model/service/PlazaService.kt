package com.dorronsoro.aparkau.model.service

import com.dorronsoro.aparkau.model.Plaza

interface PlazaService {
    suspend fun getTodasLasPlazas(): List<Plaza>
    suspend fun getPlaza(plazaId: String): Plaza?
    suspend fun actualizarEstadoPlaza(plazaId: String, nuevoEstado: String)
}