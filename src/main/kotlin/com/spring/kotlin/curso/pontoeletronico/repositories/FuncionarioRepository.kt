package com.spring.kotlin.curso.pontoeletronico.repositories

import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario
import org.springframework.data.mongodb.repository.MongoRepository

interface FuncionarioRepository : MongoRepository<Funcionario, String> {

    fun findByEmail(email: String): Funcionario
    fun findByCpf(cpf: String): Funcionario
}