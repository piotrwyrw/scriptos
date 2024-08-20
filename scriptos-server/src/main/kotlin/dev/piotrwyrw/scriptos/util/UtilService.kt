package dev.piotrwyrw.scriptos.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import kotlin.random.Random

@Service
class UtilService (
    val bCryptPasswordEncoder: BCryptPasswordEncoder
){

    fun randomSessionTokenString() = Base64.getEncoder().encodeToString(Random.Default.nextBytes(128))

    fun encodePassword(passwd: String) = bCryptPasswordEncoder.encode(passwd)

    fun comparePassword(passwd: String, hash: String) = bCryptPasswordEncoder.matches(passwd, hash)

}