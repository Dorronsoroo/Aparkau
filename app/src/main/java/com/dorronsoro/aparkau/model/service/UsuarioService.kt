package com.dorronsoro.aparkau.model.service

import com.dorronsoro.aparkau.model.PerfilUsuario
import com.dorronsoro.aparkau.model.Usuario

interface UsuarioService {

    /**
     * Guarda un nuevo [Usuario] en la colección "usuarios" de Firestore.
     * El documento se crea con el mismo UID que generó Firebase Auth.
     *
     * @param uid       UID del usuario recién registrado en FirebaseAuth.
     * @param nombre    Nombre del usuario.
     * @param apellidos Apellidos del usuario.
     * @param email     Correo electrónico del usuario.
     * @param perfil    Perfil seleccionado durante el registro.
     * @return [Result.success] si la operación fue exitosa,
     *         [Result.failure] con la excepción en caso contrario.
     */
    suspend fun guardarUsuario(
        uid: String,
        nombre: String,
        apellidos: String,
        email: String,
        perfil: PerfilUsuario
    ): Result<Unit>

    /**
     * Lee el documento del usuario y lo devuelve, o null si no existe.
     */
    suspend fun getUsuario(uid: String): Usuario?

    /**
     * Actualiza el email y el perfil del usuario sin sobrescribir el resto
     * de campos (por ejemplo, la lista de vehículos).
     */
    suspend fun actualizarUsuario(
        uid: String,
        email: String,
        perfil: PerfilUsuario
    ): Result<Unit>
}

