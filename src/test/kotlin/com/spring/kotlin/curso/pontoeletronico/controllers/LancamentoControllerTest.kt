package com.spring.kotlin.curso.pontoeletronico.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario
import com.spring.kotlin.curso.pontoeletronico.documents.Lancamento
import com.spring.kotlin.curso.pontoeletronico.dtos.LancamentoDTO
import com.spring.kotlin.curso.pontoeletronico.enums.PerfilEnum
import com.spring.kotlin.curso.pontoeletronico.enums.TipoEnum
import com.spring.kotlin.curso.pontoeletronico.services.FuncionarioService
import com.spring.kotlin.curso.pontoeletronico.services.LancamentoService
import com.spring.kotlin.curso.pontoeletronico.utils.SenhaUtils
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class LancamentoControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val lancamentoService: LancamentoService? = null

    @MockBean
    private val funcionarioService: FuncionarioService? = null

    private val urlBase: String = "/api/lancamentos/"
    private val idFuncionario: String = "1"
    private val idLancamento: String = "1"
    private val idEmpresa: String = "1"
    private val tipo: String = TipoEnum.INICIO_TRABALHO.name
    private val data: Date = Date()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun testCadastrarLancamento() {

        val lancamento: Lancamento = obterDadosLancamento()

        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(idFuncionario)).willReturn(funcionario())
        BDDMockito.given(lancamentoService?.persistir(obterDadosLancamento())).willReturn(lancamento)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipo").value(tipo))
                .andExpect(jsonPath("$.data.data").value(dateFormat.format(data)))
                .andExpect(jsonPath("$.data.funcionarioId").value(idFuncionario))
                .andExpect(jsonPath("$.erros").isEmpty())
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun testCadastrarLancamentoFuncionarioIdInvalido() {
        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(idFuncionario)).willReturn(null)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Funcionário não encontrado. ID inexistente."))
                .andExpect(jsonPath("$.data").isEmpty())
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser(username = "admin@admin.com", roles = arrayOf("ADMIN"))
    fun testRemoverLancamento() {
        BDDMockito.given<Lancamento>(lancamentoService?.buscarPorId(idLancamento)).willReturn(obterDadosLancamento())

        mvc!!.perform(MockMvcRequestBuilders.delete(urlBase + idLancamento)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }

    private fun obterJsonRequisicaoPost(): String {
        val lancamentoDTO: LancamentoDTO = LancamentoDTO(dateFormat.format(data), tipo, "Descrição", "1.234.5", idFuncionario)
        val mapper = ObjectMapper()

        return mapper.writeValueAsString(lancamentoDTO)
    }

    private fun funcionario(): Funcionario? =
            Funcionario("Nome", "fulano@gmail.com", SenhaUtils().gerarBcrypt("123465"), cpf = "12345679821", perfil = PerfilEnum.ROLE_USUARIO, id = idFuncionario, empresaId = idEmpresa)

    private fun obterDadosLancamento(): Lancamento = Lancamento(data, TipoEnum.valueOf(tipo), idFuncionario, "Descrição", "1.234.4.234", idLancamento)
}