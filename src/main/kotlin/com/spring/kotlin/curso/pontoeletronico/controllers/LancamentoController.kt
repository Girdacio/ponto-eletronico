package com.spring.kotlin.curso.pontoeletronico.controllers

import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario
import com.spring.kotlin.curso.pontoeletronico.documents.Lancamento
import com.spring.kotlin.curso.pontoeletronico.dtos.LancamentoDTO
import com.spring.kotlin.curso.pontoeletronico.enums.TipoEnum
import com.spring.kotlin.curso.pontoeletronico.response.Response
import com.spring.kotlin.curso.pontoeletronico.services.FuncionarioService
import com.spring.kotlin.curso.pontoeletronico.services.LancamentoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
open class LancamentoController(
        val lancamentoService: LancamentoService,
        val funcionarioService: FuncionarioService) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Value("\${paginacao.qtde_por_pagina}")
    val qtdePorPagina: Int = 20

    @PostMapping
    fun adicionar(@Valid @RequestBody lancamentoDTO: LancamentoDTO, result: BindingResult): ResponseEntity<Response<LancamentoDTO>> {

        val response: Response<LancamentoDTO> = Response<LancamentoDTO>()
        validarFuncionario(lancamentoDTO, result)

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        val lancamento: Lancamento = converterDTOParaLancamento(lancamentoDTO, result)
        lancamentoService.persistir(lancamento)
        response.data = converterLancamentoParaDTO(lancamento)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDTO>> {

        val response: Response<LancamentoDTO> = Response<LancamentoDTO>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null) {
            response.erros.add("Lançamento não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterLancamentoParaDTO(lancamento)

        return ResponseEntity.ok(response)
    }

//    @GetMapping("/funcionario/{funcionarioId}")
//    fun listarPorFuncionarioId(
//            @PathVariable("funcionarioId") funcionarioId: String,
//            @RequestParam(value = "pag", defaultValue = "0") pag: Int,
//            @RequestParam(value = "ord", defaultValue = "id") ord: String,
//            @RequestParam(value = "dir", defaultValue = "DESC") dir: String) : ResponseEntity<Response<Page<LancamentoDTO>>> {
//
//        val response: Response<Page<LancamentoDTO>> = Response<Page<LancamentoDTO>>()
//        val pageRequest: PageRequest = PageRequest.of(pag, qtdePorPagina, Sort.Direction.valueOf(dir), ord)
//        val lancamentos: Page<Lancamento> = lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)
//
//        val lancamentosDTO: Page<LancamentoDTO> = lancamentos.map { lancamento -> converterLancamentoParaDTO(lancamento) }
//
//        response.data = lancamentosDTO
//        return ResponseEntity.ok(response)
//    }

    @PutMapping("/{id}")
    fun atualizar(
            @PathVariable("id") id: String,
            @Valid @RequestBody lancamentoDTO: LancamentoDTO,
            result: BindingResult): ResponseEntity<Response<LancamentoDTO>> {

        val response: Response<LancamentoDTO> = Response<LancamentoDTO>()
        validarFuncionario(lancamentoDTO, result)

        val lancamento: Lancamento = converterDTOParaLancamento(lancamentoDTO, result)

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        lancamentoService.persistir(lancamento)
        response.data = converterLancamentoParaDTO(lancamento)

        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun remover(@PathVariable("id") id: String): ResponseEntity<Response<String>> {

        val response: Response<String> = Response<String>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null) {
            response.erros.add("Erro ao remover lançamento. Registro não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        lancamentoService.remover(id)

        return ResponseEntity.ok(Response<String>())
    }

    private fun converterLancamentoParaDTO(lancamento: Lancamento): LancamentoDTO =
            LancamentoDTO(
                    dateFormat.format(lancamento.data),
                    lancamento.tipo.toString(),
                    lancamento.descricao,
                    lancamento.localizacao,
                    lancamento.funcionarioId,
                    lancamento.id)

    private fun converterDTOParaLancamento(lancamentoDTO: LancamentoDTO, result: BindingResult): Lancamento {

        if (lancamentoDTO.id != null) {
            val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDTO.id)
            if (lanc == null) result.addError(ObjectError("lancamento", "Lançamento não encontrado."))
        }

        return Lancamento(
                dateFormat.parse(lancamentoDTO.data),
                TipoEnum.valueOf(lancamentoDTO.tipo!!),
                lancamentoDTO.funcionarioId!!,
                lancamentoDTO.descricao,
                lancamentoDTO.localizacao,
                lancamentoDTO.id)
    }

    private fun validarFuncionario(lancamentoDTO: LancamentoDTO, result: BindingResult) {

        if (lancamentoDTO.funcionarioId == null) {
            result.addError(ObjectError("funcionario", "Funcionário não informado."))
            return
        }

        val funcionario: Funcionario? = funcionarioService.buscarPorId(lancamentoDTO.funcionarioId)
        if (funcionario == null) {
            result.addError(ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."))
        }
    }
}