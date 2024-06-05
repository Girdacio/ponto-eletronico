package com.spring.kotlin.curso.pontoeletronico.repositories

import com.spring.kotlin.curso.pontoeletronico.documents.Lancamento
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface LancamentoRepository : MongoRepository<Lancamento, String> {

    fun findByFuncionarioId(funfionarioId: String, pageable: Pageable): Page<Lancamento>
}