package com.dorronsoro.aparkau.model.service.impl

import com.dorronsoro.aparkau.model.PerfilUsuario
import com.dorronsoro.aparkau.model.Usuario
import com.dorronsoro.aparkau.model.service.UsuarioService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsuarioServiceImpl @Inject constructor() : UsuarioService {

    // Lazy para que la instancia se cree una sola vez cuando se necesite
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    companion object {
        private const val COLECCION_USUARIOS = "usuarios"
    }

    /**
     * Guarda el objeto [Usuario] en Firestore.
     * El ID del documento es exactamente el UID de FirebaseAuth,
     * garantizando así la coherencia entre ambos sistemas.
     */
    override suspend fun guardarUsuario(
        uid: String,
        email: String,
        perfil: PerfilUsuario
    ): Result<Unit> {
        return try {
            val nuevoUsuario = Usuario(
                id = uid,
                email = email,
                perfil = perfil,
                vehiculos = emptyList()
            )

            firestore
                .collection(COLECCION_USUARIOS)
                .document(uid)          // El ID del documento == UID de Auth
                .set(nuevoUsuario)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

