package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TelemetryMetricItem
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
import java.util.Locale

@Composable
fun SpeedBoostScreen(
    modifier: Modifier = Modifier
) {
    val isTesting by VpnController.isTestingSpeed.collectAsState()
    val progress by VpnController.speedTestProgress.collectAsState()
    val testResult by VpnController.speedTestResult.collectAsState()
    val settings by VpnController.securitySettings.collectAsState()
    val server by VpnController.selectedServer.collectAsState()

    var mtuBoostEnabled by remember { mutableStateOf(settings.mtuSize == 1420) }
    var dnsAcceleratorEnabled by remember { mutableStateOf(true) }
    var congestionControlEnabled by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Top Accelerator Hero Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDarkElevated),
            border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = SpeedGreen,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Connection Accelerator Engine",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bypass ISP bandwidth throttling & minimize route latency",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Diagnostic Result or Launch Button
                if (isTesting) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = NeonCyan,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = when {
                                progress < 0.35f -> "Measuring Millisecond Round-Trip Latency..."
                                progress < 0.70f -> "Testing Encrypted Throughput & MTU Buffers..."
                                else -> "Calculating Speed Gain vs Standard ISP..."
                            },
                            fontSize = 12.sp,
                            color = NeonCyan,
                            fontFamily = RobotoMonoFontFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = SpeedGreen,
                            trackColor = CyberDarkSurfaceVariant
                        )
                    }
                } else {
                    Button(
                        onClick = { VpnController.runSpeedAndBoostDiagnosis() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("run_speed_test_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = CyberDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (testResult != null) "RE-TEST ACCELERATED SPEED" else "TEST SPEED & LATENCY GAIN",
                            color = CyberDark,
                            fontWeight = FontWeight.Bold,
                            fontFamily = RobotoMonoFontFamily,
                            fontSize = 13.sp
                        )
                    }
                }

                if (testResult != null && !isTesting) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SpeedGreen.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, SpeedGreen.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SpeedGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Speed Gain Verified",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Route optimized via ${testResult!!.serverName}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Text(
                                text = "+${testResult!!.speedBoostPercent}% BOOST",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpeedGreen,
                                fontFamily = RobotoMonoFontFamily
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Speed Metrics Display
        val res = testResult
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TelemetryMetricItem(
                title = "Download",
                value = if (res != null) String.format(Locale.US, "%.1f", res.downloadMbps) else "124.8",
                unit = "MBPS",
                iconColor = SpeedGreen,
                modifier = Modifier.weight(1f)
            )
            TelemetryMetricItem(
                title = "Upload",
                value = if (res != null) String.format(Locale.US, "%.1f", res.uploadMbps) else "68.2",
                unit = "MBPS",
                iconColor = ElectricBlue,
                modifier = Modifier.weight(1f)
            )
            TelemetryMetricItem(
                title = "Ping",
                value = if (res != null) "${res.pingMs}" else "${server.pingMs}",
                unit = "MS",
                iconColor = NeonCyan,
                modifier = Modifier.weight(1f)
            )
            TelemetryMetricItem(
                title = "Jitter",
                value = if (res != null) "${res.jitterMs}" else "2",
                unit = "MS",
                iconColor = WarningAmber,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "How TurboShield Accelerates Connections" Explainer
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDarkElevated),
            border = BorderStroke(1.dp, CyberBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "How TurboShield Fastens Your Connection",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                AccelerationPillarItem(
                    title = "ISP Throttling Neutralizer",
                    desc = "ISPs inspect and throttle streaming, torrents, and gaming traffic during peak hours. TurboShield's ChaCha20 encryption renders your traffic unclassifiable, restoring full uncapped bandwidth.",
                    icon = Icons.Default.NetworkCheck,
                    accentColor = NeonCyan
                )

                Spacer(modifier = Modifier.height(10.dp))

                AccelerationPillarItem(
                    title = "Kernel MTU Frame Tuning (1420 bytes)",
                    desc = "Standard 1500-byte packets get fragmented across carrier cell towers and fiber backbones. TurboShield auto-tunes MTU to 1420 bytes, eliminating fragmentation latency and packet retransmission.",
                    icon = Icons.Default.Tune,
                    accentColor = SpeedGreen
                )

                Spacer(modifier = Modifier.height(10.dp))

                AccelerationPillarItem(
                    title = "Direct Tier-1 Anycast Routing",
                    desc = "Instead of zig-zagging through slow residential ISP transit nodes, your packets enter TurboShield's 10Gbps Tier-1 backbone hops directly to destination cloud datacenters.",
                    icon = Icons.Default.CompareArrows,
                    accentColor = ElectricBlue
                )

                Spacer(modifier = Modifier.height(10.dp))

                AccelerationPillarItem(
                    title = "Zero-Wait Anycast DNS",
                    desc = "Replaces slow 80ms ISP DNS resolvers with distributed 1.1.1.1 / 9.9.9.9 memory-cached lookups with sub-5ms lookup times for web pages and API calls.",
                    icon = Icons.Default.Dns,
                    accentColor = WarningAmber
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Optimization Toggles
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDarkElevated),
            border = BorderStroke(1.dp, CyberBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Hardware & Network Acceleration Toggles",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "MTU 1420 WireGuard Tuning",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Eliminate packet fragmentation on LTE/5G",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = mtuBoostEnabled,
                        onCheckedChange = {
                            mtuBoostEnabled = it
                            VpnController.updateSettings(settings.copy(mtuSize = if (it) 1420 else 1500))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberDark,
                            checkedTrackColor = NeonCyan,
                            uncheckedTrackColor = CyberDarkSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Anycast DNS Acceleration",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Sub-10ms domain name lookups",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = dnsAcceleratorEnabled,
                        onCheckedChange = { dnsAcceleratorEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberDark,
                            checkedTrackColor = SpeedGreen,
                            uncheckedTrackColor = CyberDarkSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BBR Congestion Control Emulation",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Maintains high throughput even with wireless packet loss",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = congestionControlEnabled,
                        onCheckedChange = { congestionControlEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberDark,
                            checkedTrackColor = ElectricBlue,
                            uncheckedTrackColor = CyberDarkSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AccelerationPillarItem(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberDarkSurface, RoundedCornerShape(12.dp))
            .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}
