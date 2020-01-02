package com.valdirviana.pontointeligente.repositories

import com.valdirviana.pontointeligente.documents.Empresa
import org.springframework.data.mongodb.repository.MongoRepository

interface EmpresaRepository : MongoRepository<Empresa, String> {

  fun findByCnpj(cnpj: String): Empresa?
}