package com.example.data.model

enum class ConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    DISCONNECTING
}

data class VpnServer(
    val id: String,
    val name: String,
    val country: String,
    val city: String,
    val flag: String,
    val ip: String,
    val pingMs: Int,
    val loadPercentage: Int,
    val tags: List<String>,
    val isRecommended: Boolean = false
)

enum class ProtocolType {
    WIREGUARD_TURBO,
    OPENVPN_STEALTH,
    IKEV2_HYPERSPEED,
    QUANTUM_SHIELD
}

data class VpnProtocol(
    val type: ProtocolType,
    val displayName: String,
    val badge: String,
    val cipher: String,
    val portAndTransport: String,
    val packetOverhead: String,
    val description: String,
    val latencyBonus: String,
    val idealFor: String
)

data class VpnMetrics(
    val durationSeconds: Long = 0,
    val downloadSpeedKbps: Float = 0f,
    val uploadSpeedKbps: Float = 0f,
    val totalDownloadBytes: Long = 0L,
    val totalUploadBytes: Long = 0L,
    val currentPingMs: Int = 22
)

data class SpeedTestResult(
    val serverName: String,
    val pingMs: Int,
    val jitterMs: Int,
    val downloadMbps: Float,
    val uploadMbps: Float,
    val speedBoostPercent: Int,
    val packetLossPercent: Float,
    val timestamp: Long = System.currentTimeMillis()
)

data class DnsOption(
    val id: String,
    val name: String,
    val primaryIp: String,
    val secondaryIp: String,
    val category: String,
    val description: String
)

data class SecuritySettings(
    val killSwitchEnabled: Boolean = true,
    val dnsLeakProtection: Boolean = true,
    val adAndMalwareBlocker: Boolean = true,
    val mtuSize: Int = 1420, // 1420 WireGuard Turbo default
    val selectedDnsId: String = "cloudflare",
    val autoConnectOnUnsecuredWifi: Boolean = true,
    val splitTunnelingEnabled: Boolean = false,
    val bypassedPackages: Set<String> = emptySet()
)
