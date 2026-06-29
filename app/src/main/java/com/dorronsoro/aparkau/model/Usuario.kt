package com.dorronsoro.aparkau.model

import com.google.firebase.firestore.DocumentId

data class Usuario(
    @DocumentId val id: String = "",
    val email: String = "",
    val perfil: PerfilUsuario = PerfilUsuario.EMPLEADO_HABITUAL,
    val vehiculos: List<Vehiculo> = emptyList()
)

