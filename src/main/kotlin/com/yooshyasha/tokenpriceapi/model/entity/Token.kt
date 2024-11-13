package com.yooshyasha.tokenpriceapi.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "tokens")
data class Token (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "TokenName")
    var tokenName: String? = null,
    var price: Double? = null,
)