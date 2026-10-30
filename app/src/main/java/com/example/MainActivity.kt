package com.example

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.ConnectionStatus
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LogsScreen
import com.example.ui.screens.SecurityScreen
import com.example.ui.screens.ServersScreen
import com.example.ui.screens.SpeedBoostScreen
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberDark
import com.example.ui.theme.CyberDarkElevated
import com.example.ui.theme.CyberDarkSurfaceVariant
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SpeedGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.vpn.VpnController

enum class VpnScreen {
    HOME,
    SERVERS,
    SPEED_BOOST,
    SECURITY,
    LOGS
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent() {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(VpnScreen.HOME) }
    val status by VpnController.connectionStatus.collectAsState()

    // Notification permission launcher for Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Processed */ }

    // VPN permission launcher
    val vpnPrepareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            VpnController.connect(context)
        } else {
            VpnController.appendLog("VPN connection request cancelled.")
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Handle system back navigation to return to Home screen
    if (currentScreen != VpnScreen.HOME) {
        BackHandler {
            currentScreen = VpnScreen.HOME
        }
    }

    val toggleVpn = {
        if (status == ConnectionStatus.CONNECTED || status == ConnectionStatus.CONNECTING) {
            VpnController.disconnect(context)
        } else {
            val prepareIntent = VpnService.prepare(context)
            if (prepareIntent != null) {
                vpnPrepareLauncher.launch(prepareIntent)
            } else {
                VpnController.connect(context)
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDark),
        containerColor = CyberDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentScreen) {
                            VpnScreen.HOME -> "TurboShield VPN"
                            VpnScreen.SERVERS -> "Global Servers"
                            VpnScreen.SPEED_BOOST -> "Speed & Boost"
                            VpnScreen.SECURITY -> "Security Protocols"
                            VpnScreen.LOGS -> "System Audit Logs"
                        },
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CyberDark
                ),
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CyberDarkElevated,
                contentColor = TextPrimary,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = currentScreen == VpnScreen.HOME,
                    onClick = { currentScreen = VpnScreen.HOME },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Turbo Home",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Turbo", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                NavigationBarItem(
                    selected = currentScreen == VpnScreen.SERVERS,
                    onClick = { currentScreen = VpnScreen.SERVERS },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Servers",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Servers", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_servers")
                )

                NavigationBarItem(
                    selected = currentScreen == VpnScreen.SPEED_BOOST,
                    onClick = { currentScreen = VpnScreen.SPEED_BOOST },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Speed Boost",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Boost", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SpeedGreen,
                        selectedTextColor = SpeedGreen,
                        indicatorColor = SpeedGreen.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_boost")
                )

                NavigationBarItem(
                    selected = currentScreen == VpnScreen.SECURITY,
                    onClick = { currentScreen = VpnScreen.SECURITY },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Security Protocols",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Security", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_security")
                )

                NavigationBarItem(
                    selected = currentScreen == VpnScreen.LOGS,
                    onClick = { currentScreen = VpnScreen.LOGS },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "Audit Logs",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Logs", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.2f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_logs")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                VpnScreen.HOME -> HomeScreen(
                    onToggleVpn = toggleVpn,
                    onNavigateToServers = { currentScreen = VpnScreen.SERVERS },
                    onNavigateToProtocols = { currentScreen = VpnScreen.SECURITY },
                    onNavigateToSpeedBoost = { currentScreen = VpnScreen.SPEED_BOOST }
                )
                VpnScreen.SERVERS -> ServersScreen(
                    onServerSelected = { server ->
                        VpnController.selectServer(server)
                        currentScreen = VpnScreen.HOME
                        if (status == ConnectionStatus.CONNECTED) {
                            toggleVpn() // Reconnect with new server
                        }
                    }
                )
                VpnScreen.SPEED_BOOST -> SpeedBoostScreen()
                VpnScreen.SECURITY -> SecurityScreen()
                VpnScreen.LOGS -> LogsScreen()
            }
        }
    }
}
