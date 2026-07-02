package com.dorronsoro.aparkau.model.service

import com.dorronsoro.aparkau.model.Vehiculo

interface VehiculoService {

    /**
     * Devuelve la lista de vehículos registrados por el usuario.
     *
     * @param uid UID del usuario en FirebaseAuth.
     */
    suspend fun getVehiculos(uid: String): List<Vehiculo>

    /**
     * Añade un nuevo [Vehiculo] a la lista del usuario en Firestore.
     *
     * @return [Result.success] si la operación fue exitosa,
     *         [Result.failure] con la excepción en caso contrario.
     */
    suspend fun agregarVehiculo(uid: String, vehiculo: Vehiculo): Result<Unit>

    /**
     * Elimina un [Vehiculo] de la lista del usuario en Firestore.
     */
    suspend fun eliminarVehiculo(uid: String, vehiculo: Vehiculo): Result<Unit>
}

