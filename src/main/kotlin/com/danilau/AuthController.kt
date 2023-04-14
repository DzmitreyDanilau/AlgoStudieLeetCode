package com.danilau

import java.time.Duration
import java.time.Instant
import java.util.UUID
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {

    @PostMapping("/auth")
    fun auth(
        @RequestBody userName: String,
        @RequestParam(value = "expireAfter", required = false) expireAfter: Long?,
    ):
        AuthResponse {
        val at = generateAccessToken(expireAfter)
        val rt = UUID.randomUUID().toString()
        return AuthResponse(userName, at, rt)
    }

    @GetMapping("/just-get")
    fun get(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): ResponseEntity<String> {
        return if (Instant.now() > token.toInstant()) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("TOKEN EXPIRED")
        } else {
            ResponseEntity.ok("TOKEN IS FINE")
        }
    }

    @GetMapping("/refresh")
    fun refresh(
        @RequestParam("refreshToken") refreshToken: String,
        @RequestParam(value = "expireAfter", required = false) expireAfter: Long?,
    ) = generateAccessToken(expireAfter)

    private fun generateAccessToken(expireAfter: Long? = null) =
        (Instant.now() + (expireAfter?.let { Duration.ofSeconds(it) } ?: Duration.ofMinutes(1)))
            .epochSecond.toString()

    private fun String.toInstant(): Instant =
        Instant.ofEpochSecond(replace("Bearer ", "").toLong())
}

data class AuthResponse(
    val userName: String,
    val accessToken: String,
    val refreshToken: String,
)
