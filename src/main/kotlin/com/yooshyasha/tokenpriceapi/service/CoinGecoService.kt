package com.yooshyasha.tokenpriceapi.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yooshyasha.tokenpriceapi.model.dto.TokenDTO
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class CoinGecoService(
    private val url: String = "https://api.coingecko.com/api/v3",
    private val apiToken: String = "CG-TyvWWaNyTxVgZjmf55yUTbC1",
    private val client: OkHttpClient = OkHttpClient(),
) {
    private fun sendRequest(url: String): Response {
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("x-cg-pro-api-key", apiToken)
            .build()

        return client.newCall(request).execute()
    }

    fun fetchAllTokens(): List<Map<String, Any>> {
        val url = "$url/coins/list"

        sendRequest(url).use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string() ?: throw IOException("Response body is null")

            val gson = Gson()
            val type = object : TypeToken<List<Map<String, Any>>>() {}.type
            val parsedResponse: List<Map<String, Any>> = gson.fromJson(responseBody, type)

            return parsedResponse
        }
    }

    fun getTokenPrice(token: String): Double? {
        val url = "$url/simple/price?ids=$token&vs_currencies=usd"

        sendRequest(url).use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string() ?: throw IOException("Response body is null")

            val gson = Gson()
            val type = object : TypeToken<Map<String, Map<String, Double>>>() {}.type
            val parsedResponse: Map<String, Map<String, Double>> = gson.fromJson(responseBody, type)

            val usdValue = parsedResponse[token]?.get("usd")

            println(usdValue)
            return usdValue
        }
    }

    fun getTokenHistory(tokenName: String, days: Int = 7): List<TokenDTO> {
        val url = "$url/coins/$tokenName/market_chart?vs_currency=usd&days=$days"

        sendRequest(url).use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string() ?: throw IOException("Response body is null")

            val gson = Gson()
            val type = object : TypeToken<Map<String, List<List<Any>>>>() {}.type
            val parsedResponse: Map<String, List<List<Any>>> = gson.fromJson(responseBody, type)

            val prices = parsedResponse["prices"] ?: throw IOException("Prices data is missing")

            return prices.map { entry ->
                val timestamp = entry[0] as Double
                val price = entry[1] as Double
                TokenDTO(
                    name = tokenName,
                    price = price
                )
            }
        }
    }
}
