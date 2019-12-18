package com.valdirviana.pontointeligente.services.impl

import com.valdirviana.pontointeligente.documents.Empresa
import com.valdirviana.pontointeligente.repositories.EmpresaRepository
import com.valdirviana.pontointeligente.services.EmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImpl(val empresaRepository: EmpresaRepository) : EmpresaService {

  override fun buscarPorCnpj(cnpj: String): Empresa? = empresaRepository.findByCnpj(cnpj)

  override fun persistir(empresa: Empresa): Empresa = empresaRepository.save(empresa)

}