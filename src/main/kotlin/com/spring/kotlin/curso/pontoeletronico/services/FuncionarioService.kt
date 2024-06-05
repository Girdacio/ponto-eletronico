package com.spring.kotlin.curso.pontoeletronico.services

import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario

interface FuncionarioService {

    fun persistir(funcionario: Funcionario): Funcionario
    fun buscarPorCpf(cpf: String): Funcionario?
    fun buscarPorEmail(email: String): Funcionario?
    fun buscarPorId(id: String): Funcionario?
}