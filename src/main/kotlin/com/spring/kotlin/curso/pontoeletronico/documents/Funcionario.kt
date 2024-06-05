package com.spring.kotlin.curso.pontoeletronico.documents

import com.spring.kotlin.curso.pontoeletronico.enums.PerfilEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document // @Entity para MongoDB
data class Funcionario (
        val nome: String,
        val email: String,
        val senha: String,
        val cpf: String,
        val perfil: PerfilEnum,
        val empresaId: String,
        val valorHora: Double? = 0.0,
        val qtdeHorasTrabalhoDia: Float? = 0.0f,
        val qtdeHorasAlmoco: Float? = 0.0f,
        @Id val id: String? = null
)