package com.valdirviana.pontointeligente

import com.valdirviana.pontointeligente.documents.Empresa
import com.valdirviana.pontointeligente.documents.Funcionario
import com.valdirviana.pontointeligente.enums.PerfilEnum
import com.valdirviana.pontointeligente.repositories.EmpresaRepository
import com.valdirviana.pontointeligente.repositories.FuncionarioRepository
import com.valdirviana.pontointeligente.repositories.LancamentoRepository
import com.valdirviana.pontointeligente.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class PontointeligenteApplication(val empresaRepository: EmpresaRepository,
								  val funcionarioRepository: FuncionarioRepository,
								  val lancamentoRepository: LancamentoRepository) : CommandLineRunner {
	override fun run(vararg args: String?) {
		empresaRepository.deleteAll()
		funcionarioRepository.deleteAll()
		lancamentoRepository.deleteAll()

		val empresa: Empresa = Empresa("Empresa", "10443887000146")
		empresaRepository.save(empresa)

		val admin: Funcionario = Funcionario("Admin",
						"admin@empresa", SenhaUtils().gerarBcrypt("123456"),
						"12345679810", PerfilEnum.ROLE_ADMIN, empresa._id!!)
		funcionarioRepository.save(admin)

		val funcionario: Funcionario = Funcionario("Funcionario",
						"funcionario@empresa", SenhaUtils().gerarBcrypt("123456"),
						"12345679810", PerfilEnum.ROLE_USER, empresa._id!!)
		funcionarioRepository.save(funcionario)

		System.out.println("Empresa ID ${empresa._id}")
		System.out.println("Admin ID ${admin.id}")
		System.out.println("Funcionario ID ${funcionario.id}")
	}
}

fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}
