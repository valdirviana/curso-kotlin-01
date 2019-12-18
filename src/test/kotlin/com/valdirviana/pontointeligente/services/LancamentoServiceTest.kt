package com.valdirviana.pontointeligente.services

import com.valdirviana.pontointeligente.documents.Lancamento
import com.valdirviana.pontointeligente.enums.TipoEnum
import com.valdirviana.pontointeligente.repositories.LancamentoRepository
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
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

@SpringBootTest
class LancamentoServiceTest {

  @Autowired
  private val lancamentoService: LancamentoService? = null

  @MockBean
  private val lancamentoRepository: LancamentoRepository? = null

  private val id: String = "1"

  @BeforeEach
  @Throws(Exception::class)
  fun setUp(){
    BDDMockito.given<Page<Lancamento>>(lancamentoRepository?.findByFuncionarioId(id, PageRequest.of(0,10)))
            .willReturn(PageImpl(ArrayList<Lancamento>()))
    BDDMockito.given(lancamentoRepository?.findById(id)).willReturn(lancamentoOptional())
    BDDMockito.given(lancamentoRepository?.save(Mockito.any(Lancamento::class.java))).willReturn(lancamento())
  }

  @Test
  fun testBuscarLancamentoPorFuncionarioId() {
    val lancamentos: Page<Lancamento>? = lancamentoService?.buscarPorFuncionarioId(id, PageRequest.of(0,10))
    Assertions.assertNotNull(lancamentos)
  }

  @Test
  fun testBuscarLancamentoPorId() {
    val lancamento: Optional<Lancamento>? = lancamentoService?.buscarPorId(id)
    Assertions.assertNotNull(lancamento)
  }

  @Test
  fun testPersistirLancamento() {
    val lancamento: Lancamento? = lancamentoService?.persistir(lancamento())
    Assertions.assertNotNull(lancamento)
  }

  private fun lancamento(): Lancamento = Lancamento(Date(), TipoEnum.INICIO_TRABALHO,id)

  private fun lancamentoOptional(): Optional<Lancamento> = Optional.of(Lancamento(Date(), TipoEnum.INICIO_TRABALHO,id))
}