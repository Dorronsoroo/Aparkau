package com.dorronsoro.aparkau.screen.mi_cuenta

import com.dorronsoro.aparkau.model.PerfilUsuario

data class MiCuentaUiState(
    val email: String = "",
    val perfil: PerfilUsuario = PerfilUsuario.EMPLEADO_HABITUAL,
    val isLoading: Boolean = false
)

