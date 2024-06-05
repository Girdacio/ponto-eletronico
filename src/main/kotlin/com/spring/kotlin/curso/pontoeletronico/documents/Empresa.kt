package com.spring.kotlin.curso.pontoeletronico.documents

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document // @Entity para MongoDB
data class Empresa (
        val razaoSocial: String,
        val cnpj: String,
        @Id val id: String? = null
)