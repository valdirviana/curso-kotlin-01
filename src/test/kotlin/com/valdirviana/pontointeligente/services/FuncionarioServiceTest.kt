package com.valdirviana.pontointeligente.services

import com.valdirviana.pontointeligente.documents.Funcionario
import com.valdirviana.pontointeligente.enums.PerfilEnum
import com.valdirviana.pontointeligente.repositories.FuncionarioRepository
import com.valdirviana.pontointeligente.utils.SenhaUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.lang.Exception
import java.util.*
import javax.swing.text.html.Option

@SpringBootTest
class FuncionarioServiceTest {

  @Autowired
  val funcionarioService: FuncionarioService? = null

  @MockBean
  private val funcionarioRepository: FuncionarioRepository? = null

  private val email: String = "email@email.com"
  private val cpf: String = "12345678910"
  private val id: String = "1"

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
    BDDMockito.given(funcionarioRepository?.save(Mockito.any(Funcionario::class.java))).willReturn(funcionario())
    BDDMockito.given(funcionarioRepository?.findById(id)).willReturn(funcionarioOptional())
    BDDMockito.given(funcionarioRepository?.findByEmail(email)).willReturn(funcionario())
    BDDMockito.given(funcionarioRepository?.findByCpf(cpf)).willReturn(funcionario())
  }

  @Test
  fun testPersistirFuncionario() {
    val funcionario: Funcionario? = this.funcionarioService?.persistir(funcionario())
    Assertions.assertNotNull(funcionario)
  }

  @Test
  fun testBuscarFuncionarioPorId() {
    val funcionario: Optional<Funcionario>? = funcionarioService?.buscarPorId(id)
    Assertions.assertNotNull(funcionario)
  }

  @Test
  fun testBuscarFuncionarioPorEmail() {
    val funcionario: Funcionario? = funcionarioService?.buscarPorEmail(email)
    Assertions.assertNotNull(funcionario)
  }

  @Test
  fun testBuscarFuncionarioPorCpf() {
    val funcionario: Funcionario? = funcionarioService?.buscarPorCpf(cpf)
    Assertions.assertNotNull(funcionario)
  }

  private fun funcionario(): Funcionario =
          Funcionario("Nome", email, SenhaUtils().gerarBcrypt("123456"), cpf, PerfilEnum.ROLE_USUARIO, id)

  private fun funcionarioOptional(): Optional<Funcionario> =
          Optional.ofNullable(
                  Funcionario(
                          nome ="Nome",
                          email =  email,
                          senha = SenhaUtils().gerarBcrypt("123456"),
                          cpf = cpf,
                          perfil =  PerfilEnum.ROLE_USUARIO,
                          id = id,
                          empresaId = "1"
                  )
          )
}