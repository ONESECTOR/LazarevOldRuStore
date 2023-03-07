package com.owlylabs.playerlibrary.helpers.crypto

import java.nio.charset.StandardCharsets
import java.security.NoSuchAlgorithmException
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESHelper {
    companion object{
        val AES_KEY by lazy { "098f6bcd4621d373" }
        private val ALGORITHM_TAG : String by lazy { "AES/CBC/PKCS5Padding" }
        private val AES_TAG by lazy { "AES" }
        private var cipher: Cipher? = null
        fun encrypt(inputMessage: String): ByteArray? {
            try {
                val iv = byteArrayOf(
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0
                ) // use different random value
                val algorithmSpec: AlgorithmParameterSpec =
                    IvParameterSpec(iv)
                val secretKey: SecretKey =
                    SecretKeySpec(AES_KEY.toByteArray(),
                        AES_TAG
                    )
                cipher!!.init(Cipher.ENCRYPT_MODE, secretKey, algorithmSpec)
                return cipher!!.doFinal(inputMessage.toByteArray(StandardCharsets.UTF_8))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun isSpecialCharacter(symbol: Char): Boolean {
            return symbol == '!' || symbol == '*' || symbol == '\'' || symbol == '`' || symbol == '(' || symbol == ')' || symbol == ';' || symbol == ':' || symbol == '@' || symbol == '&' || symbol == '=' || symbol == '+' || symbol == '$' || symbol == ',' || symbol == '/' || symbol == '?' || symbol == '%' || symbol == '#' || symbol == '[' || symbol == ']' || symbol == '{' || symbol == '}' || symbol == '<' || symbol == '>' || symbol == '|' || symbol == '\\'
        }

        fun charToHex(symbol: Char): String {
            return String.format("%02x", symbol.toInt())
        }

        @Throws(java.lang.Exception::class)
        fun decrypt(cryptedText: String): String? {
            val iv = byteArrayOf(
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
            ) // use different random value
            val algorithmSpec: AlgorithmParameterSpec =
                IvParameterSpec(iv)
            val secretKey: SecretKey =
                SecretKeySpec(AES_KEY.toByteArray(), AES_TAG)
            cipher!!.init(Cipher.DECRYPT_MODE, secretKey, algorithmSpec)
            val bytes = Base64.decode(cryptedText)
            val decrypted = cipher!!.doFinal(bytes)
            val s1 = String(decrypted, StandardCharsets.UTF_8)
            return s1
        }

        init {
            try {
                cipher = Cipher.getInstance(
                    ALGORITHM_TAG
                )
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            }
        }
    }
}