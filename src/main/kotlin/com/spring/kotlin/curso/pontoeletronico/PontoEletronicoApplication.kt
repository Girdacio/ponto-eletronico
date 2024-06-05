package com.spring.kotlin.curso.pontoeletronico

import com.spring.kotlin.curso.pontoeletronico.documents.Empresa
import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario
import com.spring.kotlin.curso.pontoeletronico.enums.PerfilEnum
import com.spring.kotlin.curso.pontoeletronico.repositories.EmpresaRepository
import com.spring.kotlin.curso.pontoeletronico.repositories.FuncionarioRepository
import com.spring.kotlin.curso.pontoeletronico.repositories.LancamentoRepository
import com.spring.kotlin.curso.pontoeletronico.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class PontoEletronicoApplication (
		val empresaRepository: EmpresaRepository,
		val funcionarioRepository: FuncionarioRepository,
		val lancamentoRepository: LancamentoRepository) : CommandLineRunner {

	override fun run(vararg args: String?) {

		empresaRepository.deleteAll()
		funcionarioRepository.deleteAll()
		lancamentoRepository.deleteAll()

		var empresa: Empresa = Empresa("Empresa", "10443887000146")
		empresa = empresaRepository.save(empresa)

		var admin: Funcionario = Funcionario(
				"Admin", "admin@empresa.com", SenhaUtils().gerarBcrypt("123456"),
				"25708317000", PerfilEnum.ROLE_ADMIN, empresa.id!!)
		admin = funcionarioRepository.save(admin)

		var funcionario: Funcionario = Funcionario(
				"Funcionario", "funcionario@emrpesa.com", SenhaUtils().gerarBcrypt("123456"),
				"44325441557", PerfilEnum.ROLE_USUARIO, empresa.id!!)
		funcionario = funcionarioRepository.save(funcionario)

		println("Empresa ID: ${empresa.id}")
		println("Admin ID: ${admin.id}")
		println("Funcionario ID: ${funcionario.id}")
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<PontoEletronicoApplication>(*args)
		}
	}
}