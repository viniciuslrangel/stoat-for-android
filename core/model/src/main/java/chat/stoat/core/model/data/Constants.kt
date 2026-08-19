package chat.stoat.core.model.data

object ServerConfig {
    var customOrigin: String? = null

    val apiBase: String
        get() = customOrigin?.let { "$it/api/0.8" } ?: OFFICIAL_API

    val websocket: String
        get() = customOrigin?.let {
            it.replace("https://", "wss://").replace("http://", "ws://") + "/ws"
        } ?: OFFICIAL_WS

    val files: String
        get() = customOrigin?.let { "$it/autumn" } ?: OFFICIAL_FILES

    val proxy: String
        get() = customOrigin?.let { "$it/january" } ?: OFFICIAL_PROXY

    val webApp: String
        get() = customOrigin ?: OFFICIAL_WEB_APP

    val betaWebApp: String
        get() = customOrigin ?: OFFICIAL_BETA_WEB_APP

    val marketing: String
        get() = customOrigin ?: OFFICIAL_MARKETING

    val invites: String
        get() = customOrigin ?: OFFICIAL_INVITES
}

private const val OFFICIAL_API = "https://api.stoat.chat/0.8"
private const val OFFICIAL_WS = "wss://events.stoat.chat"
private const val OFFICIAL_FILES = "https://cdn.stoatusercontent.com"
private const val OFFICIAL_PROXY = "https://proxy.stoatusercontent.com"
private const val OFFICIAL_WEB_APP = "https://stoat.chat"
private const val OFFICIAL_BETA_WEB_APP = "https://beta.stoat.chat"
private const val OFFICIAL_MARKETING = "https://stoat.chat"
private const val OFFICIAL_INVITES = "https://stt.gg"

val STOAT_BASE get() = ServerConfig.apiBase
const val STOAT_SUPPORT = "https://support.stoat.chat"
val STOAT_MARKETING get() = ServerConfig.marketing
val STOAT_FILES get() = ServerConfig.files
val STOAT_PROXY get() = ServerConfig.proxy
val STOAT_WEB_APP get() = ServerConfig.webApp
val STOAT_BETA_WEB_APP get() = ServerConfig.betaWebApp
val STOAT_INVITES get() = ServerConfig.invites
val STOAT_WEBSOCKET get() = ServerConfig.websocket
const val STOAT_CHANGELOG = "https://changelog.stoat.chat"
