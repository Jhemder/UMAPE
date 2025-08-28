package com.example.umape.data.models

data class User(
    val name: String,
    val avatar: String = "",
    val trainerLevel: Int = 1,
    val language: String = "es",
    val createdAt: Long = System.currentTimeMillis()
)