package chat.stoat.settings

import chat.stoat.core.model.data.ServerConfig
import chat.stoat.persistence.KVStorage
import java.net.URI

object ServerConfigRepository {
    const val KV_KEY = "serverOrigin"
    const val DEFAULT_ORIGIN = "https://stoat.chat"

    suspend fun load(kv: KVStorage) {
        ServerConfig.customOrigin = kv.get(KV_KEY)?.takeIf { it.isNotBlank() }
    }

    suspend fun save(kv: KVStorage, input: String): Result<String> {
        val trimmed = input.trim()

        if (trimmed.isEmpty() || trimmed == DEFAULT_ORIGIN) {
            kv.remove(KV_KEY)
            ServerConfig.customOrigin = null
            return Result.success(DEFAULT_ORIGIN)
        }

        return try {
            val normalized = normalizeOrigin(trimmed)
            kv.set(KV_KEY, normalized)
            ServerConfig.customOrigin = normalized
            Result.success(normalized)
        } catch (error: IllegalArgumentException) {
            Result.failure(error)
        }
    }

    fun currentOrigin(): String = ServerConfig.customOrigin ?: DEFAULT_ORIGIN

    fun normalizeOrigin(input: String): String {
        var value = input.trim()

        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            value = "https://$value"
        }

        val uri = URI(value)
        val scheme = uri.scheme?.lowercase()
        val host = uri.host?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Invalid server URL")

        if (scheme != "http" && scheme != "https") {
            throw IllegalArgumentException("Server URL must use http or https")
        }

        val isLocalhost = host == "localhost" || host == "127.0.0.1"
        if (scheme == "http" && !isLocalhost) {
            throw IllegalArgumentException("HTTP is only allowed for localhost")
        }

        val port = uri.port
        val portSuffix = if (port != -1) ":$port" else ""

        return "$scheme://$host$portSuffix"
    }
}
