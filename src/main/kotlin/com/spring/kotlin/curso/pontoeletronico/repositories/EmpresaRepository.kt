package com.spring.kotlin.curso.pontoeletronico.repositories

import com.spring.kotlin.curso.pontoeletronico.documents.Empresa
import org.springframework.data.mongodb.repository.MongoRepository

interface EmpresaRepository : MongoRepository<Empresa, String> {

    fun findByCnpj(cnpj: String): Empresa

}