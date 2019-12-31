package com.valdirviana.pontointeligente.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.valdirviana.pontointeligente.documents.Funcionario
import com.valdirviana.pontointeligente.documents.Lancamento
import com.valdirviana.pontointeligente.dtos.LancamentoDto
import com.valdirviana.pontointeligente.enums.PerfilEnum
import com.valdirviana.pontointeligente.enums.TipoEnum
import com.valdirviana.pontointeligente.services.FuncionarioService
import com.valdirviana.pontointeligente.services.LancamentoService
import com.valdirviana.pontointeligente.utils.SenhaUtils
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
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class LancamentoControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private  val lancamentoService: LancamentoService? = null

    @MockBean
    private val funcionarioService: FuncionarioService? = null

    private val urlBase: String = "/api/lancamentos/"
    private val idFuncionario: String = "1"
    private val idLancamento: String = "1"
    private val tipo: String = TipoEnum.INICIO_TRABALHO.name
    private val data: Date = Date()

    private  val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun testCadastrarLancamento() {
        val lancamento: Optional<Lancamento> = obterDadosLancamento()

        BDDMockito.given<Optional<Funcionario>>(funcionarioService?.buscarPorId(idFuncionario))
                .willReturn(funcionario())
        BDDMockito.given(lancamentoService?.persistir(obterDadosLancamento().get()))
                .willReturn(lancamento.get())

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
    fun testCadastrarLancamentoComFuncionarioIdInvalido() {
        BDDMockito.given<Optional<Funcionario>>(funcionarioService?.buscarPorId(idFuncionario))
                .willReturn(null)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.data").isEmpty)
                .andExpect(jsonPath("$.erros")
                        .value("Funcionário não encontrado. ID inexistente."))

    }

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun testRemoverLancamento() {
        BDDMockito.given<Optional<Lancamento>>(lancamentoService?.buscarPorId(idLancamento))
                .willReturn(obterDadosLancamento())

        mvc!!.perform(MockMvcRequestBuilders.delete(urlBase + idLancamento)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
    }

    @Throws(JsonProcessingException::class)
    private fun obterJsonRequisicaoPost(): String {
        val lancamentoDto: LancamentoDto = LancamentoDto(
                dateFormat.format(data), tipo, "Descrição",
                "1.234,4.234", idFuncionario)
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(lancamentoDto)
    }

    private fun obterDadosLancamento(): Optional<Lancamento> =
            Optional.ofNullable(Lancamento(data, TipoEnum.valueOf(tipo), idFuncionario,
                    "Descricao","1.243,4.345", idLancamento))

    private fun funcionario(): Optional<Funcionario> =
            Optional.ofNullable(Funcionario("Nome", "email@email.com", SenhaUtils().gerarBcrypt("123456"),
                    "12345678901", PerfilEnum.ROLE_USER, idFuncionario))
}