package com.valdirviana.pontointeligente.services.impl

import com.valdirviana.pontointeligente.documents.Lancamento
import com.valdirviana.pontointeligente.repositories.LancamentoRepository
import com.valdirviana.pontointeligente.services.LancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class LancamentoServiceImpl(val lancamentoRepository: LancamentoRepository) : LancamentoService {

  override fun buscarPorFuncionarioId(funcionarioId: String, pageRequest: PageRequest): Page<Lancamento> =
          lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest)

  override fun buscarPorId(id: String): Optional<Lancamento> = lancamentoRepository.findById(id)

  override fun persistir(lancamento: Lancamento): Lancamento = lancamentoRepository.save(lancamento)

  override fun remover(id: String) = lancamentoRepository.deleteById(id)

}