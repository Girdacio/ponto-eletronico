package com.spring.kotlin.curso.pontoeletronico.services

import com.spring.kotlin.curso.pontoeletronico.documents.Lancamento
import com.spring.kotlin.curso.pontoeletronico.enums.TipoEnum
import com.spring.kotlin.curso.pontoeletronico.repositories.LancamentoRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@SpringBootTest
class LancamentoServiceTest {

    @MockBean
    private val lancamentoRepository: LancamentoRepository? = null

    @Autowired
    private val lancamentoService: LancamentoService? = null

    private val id: String = "1"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(lancamentoRepository?.findByFuncionarioId(id, PageRequest.of(0, 10))).willReturn(PageImpl(ArrayList<Lancamento>()))
        BDDMockito.given(lancamentoRepository?.findById(id)).willReturn(Optional.of(lancamento()))
        BDDMockito.given(lancamentoRepository?.save(Mockito.any(Lancamento::class.java))).willReturn(lancamento())
    }

    @Test
    fun testBuscarLancamentoPorFuncionarioId() {
        val lancamento: Page<Lancamento>? = lancamentoService?.buscarPorFuncionarioId(id, PageRequest.of(0, 10))
        Assertions.assertNotNull(lancamento)
    }

    @Test
    fun buscarLancamentoPorId() {
        val lancamento: Lancamento? = lancamentoService?.buscarPorId(id)
        Assertions.assertNotNull(lancamento)
    }

    @Test
    fun testPersisirLancamento() {
        val lancamento: Lancamento? = lancamentoService?.persistir(lancamento())
        Assertions.assertNotNull(lancamento)
    }

    private fun lancamento(): Lancamento = Lancamento(Date(), TipoEnum.INICIO_TRABALHO, id)
}