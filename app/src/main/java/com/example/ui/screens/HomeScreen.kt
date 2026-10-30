package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ConnectionStatus
import com.example.ui.components.TelemetryMetricItem
import com.example.ui.components.VpnPowerButton
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
fun HomeScreen(
    onToggleVpn: () -> Unit,
    onNavigateToServers: () -> Unit,
    onNavigateToProtocols: () -> Unit,
    onNavigateToSpeedBoost: () -> Unit,
    modifier: Modifier = Modifier
) {
    val status by VpnController.connectionStatus.collectAsState()
    val server by VpnController.selectedServer.collectAsState()
    val protocol by VpnController.selectedProtocol.collectAsState()
    val metrics by VpnController.metrics.collectAsState()
    val settings by VpnController.securitySettings.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Security Status Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            color = if (status == ConnectionStatus.CONNECTED) CyberDarkSurface else CyberDarkElevated,
            border = BorderStroke(
                1.dp,
                if (status == ConnectionStatus.CONNECTED) SpeedGreen.copy(alpha = 0.5f) else CyberBorder
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (status == ConnectionStatus.CONNECTED) Icons.Default.Lock else Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = if (status == ConnectionStatus.CONNECTED) SpeedGreen else AlertRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (status == ConnectionStatus.CONNECTED) "SECURE TUNNEL ACTIVE" else "CONNECTION UNENCRYPTED",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (status == ConnectionStatus.CONNECTED) SpeedGreen else AlertRed,
                        fontFamily = RobotoMonoFontFamily,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = if (status == ConnectionStatus.CONNECTED) "IP: ${server.ip}" else "ISP ROUTE",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontFamily = RobotoMonoFontFamily
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Hero Cyber World Map Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, CyberBorder),
            colors = CardDefaults.cardColors(containerColor = CyberDarkElevated)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.vpn_world_nodes),
                    contentDescription = "Holographic Global Network",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    CyberDark.copy(alpha = 0.75f),
                                    CyberDark
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (status == ConnectionStatus.CONNECTED) SpeedGreen else NeonCyan)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TURBOSHIELD GLOBAL BACKBONE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan,
                                letterSpacing = 1.sp,
                                fontFamily = RobotoMonoFontFamily
                            )
                        }
                        Text(
                            text = if (status == ConnectionStatus.CONNECTED)
                                "Optimized Route: ${server.city} (${server.pingMs}ms)"
                            else
                                "14 Global Tier-1 Acceleration Nodes",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NeonCyan.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f)),
                        modifier = Modifier.clickable(onClick = onNavigateToSpeedBoost)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "ACCELERATOR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan,
                                fontFamily = RobotoMonoFontFamily
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Center Power Button
        VpnPowerButton(
            status = status,
            onClick = onToggleVpn
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Status description text
        Text(
            text = when (status) {
                ConnectionStatus.CONNECTED -> "Connection Accelerated & Encrypted"
                ConnectionStatus.CONNECTING -> "Establishing Secure TUN Buffer..."
                ConnectionStatus.DISCONNECTING -> "Restoring Default Routing..."
                ConnectionStatus.DISCONNECTED -> "Tap Power Button to Fasten & Encrypt"
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (status == ConnectionStatus.CONNECTED) SpeedGreen else TextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Live Telemetry Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val downMbps = metrics.downloadSpeedKbps / 1024f
            val upMbps = metrics.uploadSpeedKbps / 1024f

            TelemetryMetricItem(
                title = "Download",
                value = if (status == ConnectionStatus.CONNECTED) String.format(Locale.US, "%.1f", downMbps) else "0.0",
                unit = "MB/S",
                iconColor = SpeedGreen,
                modifier = Modifier.weight(1f)
            )
            TelemetryMetricItem(
                title = "Upload",
                value = if (status == ConnectionStatus.CONNECTED) String.format(Locale.US, "%.1f", upMbps) else "0.0",
                unit = "MB/S",
                iconColor = ElectricBlue,
                modifier = Modifier.weight(1f)
            )
            TelemetryMetricItem(
                title = "Latency",
                value = if (status == ConnectionStatus.CONNECTED) "${metrics.currentPingMs}" else "${server.pingMs}",
                unit = "MS",
                iconColor = NeonCyan,
                modifier = Modifier.weight(1f)
            )
            val minutes = metrics.durationSeconds / 60
            val seconds = metrics.durationSeconds % 60
            TelemetryMetricItem(
                title = "Duration",
                value = String.format(Locale.US, "%02d:%02d", minutes, seconds),
                unit = "",
                iconColor = WarningAmber,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Server Quick Switcher Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateToServers)
                .testTag("server_selection_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDarkElevated),
            border = BorderStroke(1.dp, CyberBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(CyberDarkSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = server.flag,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = server.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SpeedGreen.copy(alpha = 0.15f),
                                border = BorderStroke(0.5.dp, SpeedGreen.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "${server.pingMs}ms",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpeedGreen,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                    fontFamily = RobotoMonoFontFamily
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "${server.city}, ${server.country} • Load: ${server.loadPercentage}%",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Switch Server",
                    tint = NeonCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Active Protocol & Security Speed Boost Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Protocol selector shortcut
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onNavigateToProtocols)
                    .testTag("protocol_shortcut_card"),
                shape = RoundedCornerShape(12.dp),
                color = CyberDarkElevated,
                border = BorderStroke(1.dp, CyberBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "PROTOCOL",
                            fontSize = 9.sp,
                            color = TextMuted,
                            fontFamily = RobotoMonoFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = protocol.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
            }

            // Connection booster diagnostic shortcut
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onNavigateToSpeedBoost)
                    .testTag("speed_boost_shortcut_card"),
                shape = RoundedCornerShape(12.dp),
                color = CyberDarkElevated,
                border = BorderStroke(1.dp, CyberBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = SpeedGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "BOOST ENGINE",
                            fontSize = 9.sp,
                            color = TextMuted,
                            fontFamily = RobotoMonoFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "MTU 1420 + DNS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SpeedGreen
                        )
                    }
                }
            }
        }
    }
}
