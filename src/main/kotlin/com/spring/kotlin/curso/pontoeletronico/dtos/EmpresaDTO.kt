package com.spring.kotlin.curso.pontoeletronico.dtos

data class EmpresaDTO (
        val razaoSocial: String,
        val cnpj: String,
        val id: String? = null
)