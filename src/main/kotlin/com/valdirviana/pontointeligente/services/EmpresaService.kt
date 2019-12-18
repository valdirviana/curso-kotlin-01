package com.valdirviana.pontointeligente.services

import com.valdirviana.pontointeligente.documents.Empresa

interface EmpresaService {

  fun buscarPorCnpj(cnpj: String): Empresa?

  fun persistir(empresa: Empresa): Empresa
}