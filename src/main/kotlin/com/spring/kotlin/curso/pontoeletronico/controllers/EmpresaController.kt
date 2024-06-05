package com.spring.kotlin.curso.pontoeletronico.controllers

import com.spring.kotlin.curso.pontoeletronico.documents.Empresa
import com.spring.kotlin.curso.pontoeletronico.dtos.EmpresaDTO
import com.spring.kotlin.curso.pontoeletronico.response.Response
import com.spring.kotlin.curso.pontoeletronico.services.EmpresaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(val empresaService: EmpresaService) {

    @GetMapping("/cnpj/{cnpj}")
    fun buscarPorCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<Response<EmpresaDTO>> {

        val response: Response<EmpresaDTO> = Response<EmpresaDTO>()
        val empresa: Empresa? = empresaService.buscarPorCnpj(cnpj)
        if (empresa == null) {
            response.erros.add("Empresa não encontrada para o CNPJ $cnpj")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterEmpresaDTO(empresa)
        return ResponseEntity.ok(response)
    }

    private fun converterEmpresaDTO(empresa: Empresa): EmpresaDTO? =
            EmpresaDTO(empresa.razaoSocial, empresa.cnpj, empresa.id)

}