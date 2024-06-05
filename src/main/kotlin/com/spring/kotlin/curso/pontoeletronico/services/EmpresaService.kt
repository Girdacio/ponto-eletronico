package com.spring.kotlin.curso.pontoeletronico.services

import com.spring.kotlin.curso.pontoeletronico.documents.Empresa

interface EmpresaService {

    fun buscarPorCnpj(cpnj: String): Empresa?
    fun persistir(empresa: Empresa): Empresa
}