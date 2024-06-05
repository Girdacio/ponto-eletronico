package com.spring.kotlin.curso.pontoeletronico.dtos

import javax.validation.constraints.NotEmpty

data class LancamentoDTO (

        @get:NotEmpty(message = "Data não pode ser vazia.")
        val data: String? = null,

        @get:NotEmpty(message = "Tipo não pode ser vazio.")
        val tipo: String? = null,

        val descricao: String? = null,
        val localizacao: String? = null,
        val funcionarioId: String? = null,
        val id: String? = null
)