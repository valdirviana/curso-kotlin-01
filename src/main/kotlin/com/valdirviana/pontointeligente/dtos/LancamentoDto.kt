package com.valdirviana.pontointeligente.dtos

import javax.validation.constraints.NotEmpty

data class LancamentoDto (
        @get:NotEmpty(message = "Data não pode ser vazio.")
        val data: String = "",

        @get:NotEmpty(message = "Tipo não pode ser vazio.")
        val tipo: String = "",

        val descricao: String? = null,
        val localizacao: String? = null,
        val funcionarioId: String? = null,
        var id: String? = null
)