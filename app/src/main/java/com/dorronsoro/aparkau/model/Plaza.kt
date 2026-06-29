package com.dorronsoro.aparkau.model

import com.google.firebase.firestore.DocumentId

data class Plaza(
    @DocumentId val id: String = "",
    val numero: String = "",
    val tipo: TipoPlaza = TipoPlaza.NORMAL,
    val estado: EstadoPlaza = EstadoPlaza.LIBRE,
    val bloqueaA_plazaId: String? = null
)
