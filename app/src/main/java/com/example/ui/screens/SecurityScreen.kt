package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NetworkPing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProtocolType
import com.example.data.model.VpnProtocol
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberDark
import com.example.ui.theme.CyberDarkElevated
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberDarkSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RobotoMonoFontFamily
import com.example.ui.theme.SpeedGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.vpn.VpnController

@Composable
fun SecurityScreen(
    modifier: Modifier = Modifier
) {
    val selectedProtocol by VpnController.selectedProtocol.collectAsState()
    val settings by VpnController.securitySettings.collectAsState()
    val protocols = VpnController.protocols
    val dnsOptions = VpnController.dnsOptions

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Section: Advanced Security Protocols
        Text(
            text = "Advanced Cryptographic Protocols",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Select from high-throughput or stealth anti-censorship ciphers",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Protocol cards list
        protocols.forEach { proto ->
            val isSelected = proto.type == selectedProtocol.type
            ProtocolSelectorCard(
                protocol = proto,
                isSelected = isSelected,
                onSelect = { VpnController.selectProtocol(proto) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Advanced Defense Suite
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDarkElevated),
            border = BorderStroke(1.dp, CyberBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Advanced Defense Suite",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Kill Switch Toggle
                SecurityToggleRow(
                    title = "Kill Switch (Always-On Leak Shield)",
                    description = "Immediately halts non-VPN Internet traffic if VPN drops unexpectedly, preventing real IP exposure.",
                    checked = settings.killSwitchEnabled,
                    accentColor = AlertRed,
                    onCheckedChange = {
                        VpnController.updateSettings(settings.copy(killSwitchEnabled = it))
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // DNS Leak Protection
                SecurityToggleRow(
                    title = "Strict DNS Leak Shield",
                    description = "Forces all domain name resolution queries through encrypted VPN tunnel sockets.",
                    checked = settings.dnsLeakProtection,
                    accentColor = SpeedGreen,
                    onCheckedChange = {
                        VpnController.updateSettings(settings.copy(dnsLeakProtection = it))
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Ad & Malware Blocker
                SecurityToggleRow(
                    title = "CleanWeb Ad & Threat Blocker",
                    description = "Intercepts telemetry tracking, malware domains, and crypto-miners at the DNS gateway.",
                    checked = settings.adAndMalwareBlocker,
                    accentColor = NeonCyan,
                    onCheckedChange = {
                        VpnController.updateSettings(settings.copy(adAndMalwareBlocker = it))
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Wi-Fi Auto Protect
                SecurityToggleRow(
                    title = "Public Wi-Fi Auto-Shield",
                    description = "Automatically prompts to activate VPN encryption when connecting to unsecured or guest networks.",
                    checked = settings.autoConnectOnUnsecuredWifi,
                    accentColor = WarningAmber,
                    onCheckedChange = {
                        VpnController.updateSettings(settings.copy(autoConnectOnUnsecuredWifi = it))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Encrypted DNS Resolver Picker
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDarkElevated),
            border = BorderStroke(1.dp, CyberBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Dns,
                        contentDescription = null,
                        tint = SpeedGreen,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Encrypted Anycast DNS",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                dnsOptions.forEach { dns ->
                    val isDnsSelected = dns.id == settings.selectedDnsId
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { VpnController.updateSettings(settings.copy(selectedDnsId = dns.id)) }
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = if (isDnsSelected) CyberDarkSurface else CyberDarkSurfaceVariant,
                        border = BorderStroke(1.dp, if (isDnsSelected) SpeedGreen else CyberBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = dns.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = dns.primaryIp,
                                        fontSize = 11.sp,
                                        color = NeonCyan,
                                        fontFamily = RobotoMonoFontFamily
                                    )
                                }
                                Text(
                                    text = dns.description,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            RadioButton(
                                selected = isDnsSelected,
                                onClick = { VpnController.updateSettings(settings.copy(selectedDnsId = dns.id)) },
                                colors = RadioButtonDefaults.colors(selectedColor = SpeedGreen)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProtocolSelectorCard(
    protocol: VpnProtocol,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .testTag("protocol_card_${protocol.type.name}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) CyberDarkSurface else CyberDarkElevated
        ),
        border = BorderStroke(
            1.dp,
            if (isSelected) NeonCyan else CyberBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else CyberDarkSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VpnKey,
                            contentDescription = null,
                            tint = if (isSelected) NeonCyan else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = protocol.displayName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (protocol.type == ProtocolType.WIREGUARD_TURBO) SpeedGreen.copy(alpha = 0.15f) else ElectricBlue.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = protocol.badge,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (protocol.type == ProtocolType.WIREGUARD_TURBO) SpeedGreen else ElectricBlue,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    fontFamily = RobotoMonoFontFamily
                                )
                            }
                        }
                        Text(
                            text = "${protocol.cipher} • ${protocol.portAndTransport}",
                            fontSize = 11.sp,
                            color = NeonCyan,
                            fontFamily = RobotoMonoFontFamily
                        )
                    }
                }

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(NeonCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Active Protocol",
                            tint = CyberDark,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = protocol.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Best for: ${protocol.idealFor}",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Text(
                    text = protocol.latencyBonus,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpeedGreen,
                    fontFamily = RobotoMonoFontFamily
                )
            }
        }
    }
}

@Composable
fun SecurityToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CyberDark,
                checkedTrackColor = accentColor,
                uncheckedTrackColor = CyberDarkSurfaceVariant
            )
        )
    }
}
