package com.yooshyasha.tokenpriceapi.service

import com.yooshyasha.tokenpriceapi.model.dto.TokenDTO
import com.yooshyasha.tokenpriceapi.model.entity.Token
import com.yooshyasha.tokenpriceapi.repository.TokenRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class TokenService(
    val tokenRepository: TokenRepository,
) {
    val restTemplate: RestTemplate = RestTemplate()

    fun getTokenPrice(token: String): Double? {
        try {
            val url = "https://api.coingecko.com/api/v3/simple/price?ids=$token&vs_currencies=usd"
            val response: Map<String, Map<String, Double>>? =
                restTemplate.getForObject(url, Map::class.java) as? Map<String, Map<String, Double>>

            return response?.get(token)?.get("usd")
        } catch (e: HttpClientErrorException.TooManyRequests) {
            // Задержка перед повторной попыткой, если возникает ошибка 429
            Thread.sleep(3000)
            return getTokenPrice(token)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun updateToken(tokenIdsParam: String) {
        val token = tokenRepository.findByTokenName(tokenIdsParam)
            ?: Token(tokenName = tokenIdsParam)

        val tokenPrice = getTokenPrice(tokenIdsParam) ?: return
        token.price = tokenPrice

        tokenRepository.save(token)
    }

    fun getToken(tokenName: String) : Token? {
        return tokenRepository.findByTokenName(tokenName)
    }

    fun convertTokenToDTO(token: Token) : TokenDTO {
        return TokenDTO(name = token.tokenName, price = token.price)
    }

    fun fetchAllTokens(): List<String> {
        val url = "https://api.coingecko.com/api/v3/coins/list"
        val response: List<Map<String, Any>>? =
            restTemplate.getForObject(url, List::class.java) as? List<Map<String, Any>>

        return response?.mapNotNull { it["id"] as? String } ?: emptyList()
    }

    fun getAllTokens(): List<TokenDTO> {
        val result = mutableListOf<TokenDTO>()

        val tokens = fetchAllTokens()

        tokens.forEach { token ->
            val tokenPrice = getTokenPrice(token) ?: return@forEach

            result.add(TokenDTO(name = token, price = tokenPrice))
        }

        return result
    }

    @Scheduled(cron = "0 */10 * * * *")
    private fun updateToken() {
        val tokenIds = fetchAllTokens()
        if (tokenIds.isEmpty()) {
            println("Не удалось получить список токенов")
            return
        }

        tokenIds.chunked(250).forEach { chunk ->
            val tokenIdsParam = chunk.joinToString(",")
            updateToken(tokenIdsParam)
        }

        println("Обновление токенов завершено")
    }
}