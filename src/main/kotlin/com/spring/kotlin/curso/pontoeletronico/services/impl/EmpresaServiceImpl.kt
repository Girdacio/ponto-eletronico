package com.spring.kotlin.curso.pontoeletronico.services.impl

import com.spring.kotlin.curso.pontoeletronico.documents.Empresa
import com.spring.kotlin.curso.pontoeletronico.repositories.EmpresaRepository
import com.spring.kotlin.curso.pontoeletronico.services.EmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImpl(val empresaRepository: EmpresaRepository) : EmpresaService {

    /* Kotlin recomenda Injeção pelo Construtor, não usar @Autowired */

    override fun buscarPorCnpj(cpnj: String): Empresa? = empresaRepository.findByCnpj(cpnj)

    override fun persistir(empresa: Empresa): Empresa = empresaRepository.save(empresa)
}