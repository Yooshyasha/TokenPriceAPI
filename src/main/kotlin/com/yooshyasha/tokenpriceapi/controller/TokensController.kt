package com.yooshyasha.tokenpriceapi.controller

import com.yooshyasha.tokenpriceapi.model.dto.TokenDTO
import com.yooshyasha.tokenpriceapi.service.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/crypto")
class TokensController(
    private val tokenService: TokenService,
) {
    @GetMapping("")
    fun getTokens() : ResponseEntity<List<TokenDTO>> {
        return ResponseEntity.ok(tokenService.getAllTokens())
    }

    @GetMapping("/{symbol}")
    fun getToken(@PathVariable("symbol") symbol: String) : ResponseEntity<TokenDTO> {
        return ResponseEntity.ok(tokenService.getToken(symbol)?.let { tokenService.convertTokenToDTO(it) })
    }

    @GetMapping("/{symbol}/history")
    fun getTokenHistory(@PathVariable symbol: String) : ResponseEntity<List<TokenDTO>> {
        return ResponseEntity.ok()
    }
}