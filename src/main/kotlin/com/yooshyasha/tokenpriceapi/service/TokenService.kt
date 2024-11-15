package com.yooshyasha.tokenpriceapi.service

import com.yooshyasha.tokenpriceapi.model.dto.TokenDTO
import com.yooshyasha.tokenpriceapi.model.entity.Token
import com.yooshyasha.tokenpriceapi.repository.TokenRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TokenService(
    val tokenRepository: TokenRepository,
    val coinGecoService: CoinGecoService = CoinGecoService(),
) {
    private fun updateToken(tokenIdsParam: String) : Token? {
        val token = tokenRepository.findByTokenName(tokenIdsParam)
            ?: Token(tokenName = tokenIdsParam)

        val tokenPrice = coinGecoService.getTokenPrice(tokenIdsParam) ?: return null
        token.price = tokenPrice

        tokenRepository.save(token)

        return token
    }

    fun getToken(tokenName: String): Token? {
        return updateToken(tokenName)
    }

    fun convertTokenToDTO(token: Token): TokenDTO {
        return TokenDTO(name = token.tokenName, price = token.price)
    }


    fun getAllTokens(): List<TokenDTO> {
        val result = mutableListOf<TokenDTO>()

        val tokens = coinGecoService.fetchAllTokens()

        tokens.forEach { token ->
            val tokenName = token["symbol"].toString()
            val tokenDTO = TokenDTO(name = tokenName, price = null)
            result.add(tokenDTO)
        }

        return result
    }

    fun getTokenHistory(tokenName: String): List<TokenDTO> {
        return coinGecoService.getTokenHistory(tokenName)
    }
}