package com.yooshyasha.tokenpriceapi.repository

import com.yooshyasha.tokenpriceapi.model.entity.Token
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository : JpaRepository<Token, Long> {
    fun findByTokenName(tokenName: String): Token?
}