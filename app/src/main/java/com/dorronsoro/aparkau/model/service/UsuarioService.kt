package com.dorronsoro.aparkau.model.service

import com.dorronsoro.aparkau.model.PerfilUsuario

interface UsuarioService {

    /**
     * Guarda un nuevo [Usuario] en la colección "usuarios" de Firestore.
     * El documento se crea con el mismo UID que generó Firebase Auth.
     *
     * @param uid    UID del usuario recién registrado en FirebaseAuth.
     * @param email  Correo electrónico del usuario.
     * @param perfil Perfil seleccionado durante el registro.
     * @return [Result.success] si la operación fue exitosa,
     *         [Result.failure] con la excepción en caso contrario.
     */
    suspend fun guardarUsuario(
        uid: String,
        email: String,
        perfil: PerfilUsuario
    ): Result<Unit>
}

