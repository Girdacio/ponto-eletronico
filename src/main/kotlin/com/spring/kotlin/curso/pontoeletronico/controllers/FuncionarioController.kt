package com.spring.kotlin.curso.pontoeletronico.controllers

import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario
import com.spring.kotlin.curso.pontoeletronico.dtos.FuncionarioDTO
import com.spring.kotlin.curso.pontoeletronico.response.Response
import com.spring.kotlin.curso.pontoeletronico.services.FuncionarioService
import com.spring.kotlin.curso.pontoeletronico.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/funcionarios")
class FuncionarioController(val funcionarioService: FuncionarioService) {

    @PutMapping("/{id}")
    fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody funcionarioDTO: FuncionarioDTO, result: BindingResult): ResponseEntity<Response<FuncionarioDTO>> {

        val response: Response<FuncionarioDTO> = Response<FuncionarioDTO>()
        val funcionario: Funcionario? = funcionarioService.buscarPorId(id)
        if (funcionario == null) {
            result.addError(ObjectError("funcionario", "Funcionário não encontrado."))
        }

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        val funcAtualizar: Funcionario = atualizarDadosFuncionario(funcionario!!, funcionarioDTO)
        funcionarioService.persistir(funcAtualizar)
        response.data = converterFuncionarioDTO(funcAtualizar)

        return ResponseEntity.ok(response)
    }

    private fun converterFuncionarioDTO(funcionario: Funcionario): FuncionarioDTO? =
            FuncionarioDTO(funcionario.nome, funcionario.email, "",
                    funcionario.valorHora.toString(), funcionario.qtdeHorasTrabalhoDia.toString(),
                    funcionario.qtdeHorasAlmoco.toString(), funcionario.id)

    private fun atualizarDadosFuncionario(funcionario: Funcionario, funcionarioDTO: FuncionarioDTO): Funcionario {

        var senha: String = if (funcionario.senha == null) funcionario.senha else SenhaUtils().gerarBcrypt(funcionarioDTO.senha!!)

        return Funcionario(funcionarioDTO.nome, funcionario.email,senha,
                funcionario.cpf, funcionario.perfil, funcionario.empresaId,
                funcionarioDTO.valorHora?.toDouble(),
                funcionarioDTO.qtdeHorasTrabalhoDia?.toFloat(),
                funcionarioDTO.qtdeHorasAlmoco?.toFloat(),
                funcionario.id)
    }
}