package com.dorronsoro.aparkau.screen.sign_up

import com.dorronsoro.aparkau.model.PerfilUsuario

data class SignUpUiState(
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val perfil: PerfilUsuario = PerfilUsuario.EMPLEADO_HABITUAL
)
