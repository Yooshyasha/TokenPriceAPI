package com.yooshyasha.tokenpriceapi.service

import com.yooshyasha.tokenpriceapi.model.entity.Token
import com.yooshyasha.tokenpriceapi.repository.TokenRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class TokenService(
    val tokenRepository: TokenRepository,
) {
    val restTemplate: RestTemplate = RestTemplate()

    fun getToken(tokenIdsParam: String?) {
        val url = "https://api.coingecko.com/api/v3/simple/price?ids=$tokenIdsParam&vs_currencies=usd"

        val response: Map<String, Map<String, Double>>? =
            restTemplate.getForObject(url, Map::class.java) as? Map<String, Map<String, Double>>

        response?.forEach { (tokenName, priceData) ->
            val usdPrice = priceData["usd"]
            if (usdPrice != null) {
                val token = tokenRepository.findByTokenName(tokenName) ?: Token(tokenName = tokenName)
                token.price = usdPrice
                tokenRepository.save(token)
            }
        }
    }

    private fun fetchAllTokens(): List<String> {
        val url = "https://api.coingecko.com/api/v3/coins/list"
        val response: List<Map<String, Any>>? =
            restTemplate.getForObject(url, List::class.java) as? List<Map<String, Any>>

        return response?.mapNotNull { it["id"] as? String } ?: emptyList()
    }

    @Scheduled(cron = "0 */10 * * * *")
    private fun updateTokens() {
        val tokenIds = fetchAllTokens()
        if (tokenIds.isEmpty()) {
            println("Не удалось получить список токенов")
            return
        }

        tokenIds.chunked(250).forEach { chunk ->
            val tokenIdsParam = chunk.joinToString(",")
            getToken(tokenIdsParam)
        }

        println("Обновление токенов завершено")
    }
}