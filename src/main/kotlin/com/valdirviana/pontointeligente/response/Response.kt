package com.valdirviana.pontointeligente.response

data class Response<T> (
        val erros: ArrayList<String>,
        var data: T
)