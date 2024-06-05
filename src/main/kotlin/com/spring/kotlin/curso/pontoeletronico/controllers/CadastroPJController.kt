package com.spring.kotlin.curso.pontoeletronico.controllers

import com.spring.kotlin.curso.pontoeletronico.documents.Empresa
import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario
import com.spring.kotlin.curso.pontoeletronico.dtos.CadastroPJDTO
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
@RequestMapping("/api/cadastrar-pj")
class CadastroPJController(val empresaService: EmpresaService, val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPJDTO: CadastroPJDTO, result: BindingResult): ResponseEntity<Response<CadastroPJDTO>> {

        val response: Response<CadastroPJDTO> = Response<CadastroPJDTO>()

        validarDadosExistentes(cadastroPJDTO, result)
        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }
        
        val empresa: Empresa = converterDTOParaEmpresa(cadastroPJDTO)
        empresaService.persistir(empresa)

        val funcionario: Funcionario = converterDTOParaFuncionario(cadastroPJDTO, empresa)
        funcionarioService.persistir(funcionario)
        response.data = converterCadastroPJDTO(funcionario, empresa)

        return ResponseEntity.ok(response)
    }

    private fun converterCadastroPJDTO(funcionario: Funcionario, empresa: Empresa): CadastroPJDTO? =
            CadastroPJDTO(funcionario.nome, funcionario.email, "", funcionario.cpf, empresa.cnpj, empresa.razaoSocial, funcionario.id)

    private fun converterDTOParaFuncionario(cadastroPJDTO: CadastroPJDTO, empresa: Empresa): Funcionario =
            Funcionario(cadastroPJDTO.nome, cadastroPJDTO.email, SenhaUtils().gerarBcrypt(cadastroPJDTO.senha), cadastroPJDTO.cnpj, PerfilEnum.ROLE_ADMIN, empresaId = empresa.id.toString())

    private fun converterDTOParaEmpresa(cadastroPJDTO: CadastroPJDTO): Empresa = Empresa(cadastroPJDTO.razaoSocial, cadastroPJDTO.cnpj)

    private fun validarDadosExistentes(cadastroPJDTO: CadastroPJDTO, result: BindingResult) {
        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPJDTO.cnpj)
        if (empresa != null) {
            result.addError(ObjectError("empresa", "Empresa já existente."))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPJDTO.cpf)
        if (funcionarioCpf != null) {
            result.addError(ObjectError("funcionario", "CPF já existente."))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(cadastroPJDTO.email)
        if (funcionarioEmail != null) {
            result.addError(ObjectError("funcionario", "Email já existente."))
        }
    }
}