package com.spring.kotlin.curso.pontoeletronico.controllers

import com.spring.kotlin.curso.pontoeletronico.documents.Empresa
import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario
import com.spring.kotlin.curso.pontoeletronico.dtos.CadastroPFDTO
import com.spring.kotlin.curso.pontoeletronico.enums.PerfilEnum
import com.spring.kotlin.curso.pontoeletronico.response.Response
import com.spring.kotlin.curso.pontoeletronico.services.EmpresaService
import com.spring.kotlin.curso.pontoeletronico.services.FuncionarioService
import com.spring.kotlin.curso.pontoeletronico.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pf")
class CadastroPFController(val empresaService: EmpresaService, val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPFDTO: CadastroPFDTO, result: BindingResult): ResponseEntity<Response<CadastroPFDTO>> {

        val response: Response<CadastroPFDTO> = Response<CadastroPFDTO>()

        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPFDTO.cnpj)
        validarDadosExistentes(cadastroPFDTO, empresa, result)
        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        val funcionario: Funcionario = converterDTOParaFuncionario(cadastroPFDTO, empresa!!)
        funcionarioService.persistir(funcionario)
        response.data = converterCadastroPFDTO(funcionario, empresa!!)

        return ResponseEntity.ok(response)
    }

    private fun converterCadastroPFDTO(funcionario: Funcionario, empresa: Empresa): CadastroPFDTO? =
            CadastroPFDTO(funcionario.nome, funcionario.email, "", funcionario.cpf, empresa.cnpj, empresa.id.toString(),
                    funcionario.valorHora.toString(), funcionario.qtdeHorasTrabalhoDia.toString(), funcionario.qtdeHorasAlmoco.toString(), funcionario.id)

    private fun converterDTOParaFuncionario(cadastroPFDTO: CadastroPFDTO, empresa: Empresa): Funcionario =
            Funcionario(cadastroPFDTO.nome, cadastroPFDTO.email, SenhaUtils().gerarBcrypt(cadastroPFDTO.senha), cadastroPFDTO.cpf,
                    PerfilEnum.ROLE_USUARIO, empresa.id.toString(),
                    cadastroPFDTO.valorHora?.toDouble(), cadastroPFDTO.qtdeHorasTrabalhoDia?.toFloat(),
                    cadastroPFDTO.qtdeHorasAlmoco?.toFloat(), cadastroPFDTO.id)

    private fun validarDadosExistentes(cadastroPFDTO: CadastroPFDTO, empresa: Empresa?, result: BindingResult) {

        if (empresa == null) {
            result.addError(ObjectError("empresa", "Empresa não cadastrada."))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPFDTO.cpf)
        if (funcionarioCpf != null) {
            result.addError(ObjectError("funcionario", "CPF já existente."))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(cadastroPFDTO.email)
        if (funcionarioEmail != null) {
            result.addError(ObjectError("funcionario", "Email já existente."))
        }
    }
}