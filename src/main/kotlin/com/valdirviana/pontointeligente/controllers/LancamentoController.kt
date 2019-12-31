package com.valdirviana.pontointeligente.controllers

import com.valdirviana.pontointeligente.documents.Funcionario
import com.valdirviana.pontointeligente.documents.Lancamento
import com.valdirviana.pontointeligente.dtos.LancamentoDto
import com.valdirviana.pontointeligente.enums.TipoEnum
import com.valdirviana.pontointeligente.response.Response
import com.valdirviana.pontointeligente.services.FuncionarioService
import com.valdirviana.pontointeligente.services.LancamentoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.text.html.Option
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController (val lancamentoService: LancamentoService,
                            val funcionarioService: FuncionarioService) {

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  @Value("\${paginacao.qtd_por_pagina}")
  val qtdPorPagina: Int = 15

  @PostMapping
  fun adicionar(@Valid @RequestBody lancamentoDto: LancamentoDto,
                result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
    val response: Response<LancamentoDto> = Response<LancamentoDto>()
    validarFuncionario(lancamentoDto,result)

    if(result.hasErrors()) {
      for(erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return ResponseEntity.badRequest().body(response)
    }

    val  lancamento: Lancamento = converDtoParaLancamento(lancamentoDto, result)
    lancamentoService.persistir(lancamento)
    response.data = converterLancamentoDto(lancamento)
    return ResponseEntity.ok(response)
  }

  @GetMapping(value = ["/{id}"])
  fun listarPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDto>> {
    val response: Response<LancamentoDto> = Response<LancamentoDto>()
    val lancamento: Optional<Lancamento> = lancamentoService.buscarPorId(id)

    if(lancamento == null) {
      response.erros.add("Lançamento não encontrado para o id $id")
      return ResponseEntity.badRequest().body(response)
    }

    response.data = converterLancamentoDto(lancamento.get())
    return ResponseEntity.ok(response)
  }

  @GetMapping(value = ["/funcionario/{funcionarioId}"])
  fun listarPorFuncionarioId(@PathVariable("funcionarioId") funcionarioId: String,
                             @RequestParam(value = "pag", defaultValue = "0") pag: Int,
                             @RequestParam(value = "ord", defaultValue = "id") ord: String,
                             @RequestParam(value = "dir", defaultValue = "DESC") dir: String):
          ResponseEntity<Response<Page<LancamentoDto>>> {
    val response: Response<Page<LancamentoDto>> = Response<Page<LancamentoDto>>()
    val pageRequest: PageRequest = PageRequest.of(pag, qtdPorPagina, Sort.Direction.valueOf(dir) ,ord)
    val lancamentos: Page<Lancamento> =
            lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)

    val lancamentosDtos: Page<LancamentoDto> =
            lancamentos.map { lancamento -> converterLancamentoDto(lancamento) }

    response.data = lancamentosDtos
    return ResponseEntity.ok(response)
  }

  @PutMapping(value = ["/{id}"])
  fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody lancamentoDto: LancamentoDto,
                result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
    val response: Response<LancamentoDto> = Response<LancamentoDto>()
    validarFuncionario(lancamentoDto, result)
    lancamentoDto.id = id
    val lancamento: Lancamento = converDtoParaLancamento(lancamentoDto, result)

    if(result.hasErrors()) {
      for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
      return  ResponseEntity.badRequest().body(response)
    }

    lancamentoService.persistir(lancamento)
    response.data = converterLancamentoDto(lancamento)
    return ResponseEntity.ok(response)
  }

  @DeleteMapping(value = ["/{id}"])
  fun remover(@PathVariable("id") id: String): ResponseEntity<Response<String>> {
    val response: Response<String> = Response<String>()
    val lancamento: Optional<Lancamento> = lancamentoService.buscarPorId(id)

    if(lancamento.isEmpty) {
      response.erros.add("Erro ao remover lançamento. Registro não encontrado para o id $id")
      return ResponseEntity.badRequest().body(response)
    }

    lancamentoService.remover(id)
    response.data = "Removeu o Id: $id"
    return ResponseEntity.ok(response)
  }

  private fun converterLancamentoDto(lancamento: Lancamento): LancamentoDto =
          LancamentoDto(dateFormat.format(lancamento.data), lancamento.tipo.toString(),
                  lancamento.descricao, lancamento.localizacao, lancamento.funcionarioId,
                  lancamento.id)

  private fun converDtoParaLancamento(lancamentoDto: LancamentoDto, result: BindingResult): Lancamento {
    if(lancamentoDto.id != null) {
      val lanc: Optional<Lancamento> = lancamentoService.buscarPorId(lancamentoDto.id!!)
      if(lanc.isEmpty) result.addError(ObjectError("lancamento", "Lançamento não encontrado."))
    }

    return Lancamento(dateFormat.parse(lancamentoDto.data), TipoEnum.valueOf(lancamentoDto.tipo!!),
            lancamentoDto.funcionarioId!!, lancamentoDto.descricao,
            lancamentoDto.localizacao)
  }

  private fun validarFuncionario(lancamentoDto: LancamentoDto, result: BindingResult) {
    if(lancamentoDto.funcionarioId == null) {
      result.addError(ObjectError("funcionario","Funcionário não informado"))
      return
    }

    val funcionario: Optional<Funcionario>? = funcionarioService.buscarPorId(lancamentoDto.funcionarioId)
    if(funcionario == null) {
      result.addError(ObjectError("funcionario","Funcionário não encontrado. ID inexistente."))
      return
    }
  }

}