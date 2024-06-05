package com.spring.kotlin.curso.pontoeletronico.response

/**
 * Classe genérica de Response
 */
data class Response<T> (
        val erros: ArrayList<String> = arrayListOf(),
        var data: T? = null
)