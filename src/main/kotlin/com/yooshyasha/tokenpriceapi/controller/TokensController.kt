package com.yooshyasha.tokenpriceapi.controller

import com.yooshyasha.tokenpriceapi.model.dto.TokenDTO
import com.yooshyasha.tokenpriceapi.model.entity.Token
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
        val token = tokenService.getToken(symbol)
        var tokenDTO : TokenDTO? = null

        if (token != null) {
            tokenDTO = tokenService.convertTokenToDTO(token)
        }

        return ResponseEntity.ok(tokenDTO)
    }

    @GetMapping("/{symbol}/history")
    fun getTokenHistory(@PathVariable symbol: String) : ResponseEntity<List<TokenDTO>> {
        return ResponseEntity.ok(tokenService.getTokenHistory(symbol))
    }
}