package services

import java.security.MessageDigest
import java.security.SecureRandom

object HashingService {
    private const val HASH_ALGORITHM = "SHA-256"

    /** Генерирует новую соль */
    fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    /**
     * Хэширует пароль с использованием соли.
     * @param password Пароль в виде строки.
     * @param salt Соль в виде массива байт.
     * @return Хэш в виде строки (Base64).
     */
    fun hashPassword(password: String, salt: ByteArray): String {
        val md = MessageDigest.getInstance(HASH_ALGORITHM)
        md.update(salt)
        val hashedPassword = md.digest(password.toByteArray(Charsets.UTF_8))
        return java.util.Base64.getEncoder().encodeToString(hashedPassword)
    }

    /** Удобная функция для преобразования строки Base64 в ByteArray */
    fun saltFromBase64(saltString: String): ByteArray = java.util.Base64.getDecoder().decode(saltString)

    /** Удобная функция для преобразования ByteArray в строку Base64 */
    fun saltToBase64(salt: ByteArray): String = java.util.Base64.getEncoder().encodeToString(salt)
}