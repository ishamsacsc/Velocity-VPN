package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.ConnectionStatus
import com.example.data.model.ProtocolType
import com.example.vpn.VpnController
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TurboShield", appName)
  }

  @Test
  fun `verify vpn servers and protocols initialized`() {
    assertTrue(VpnController.defaultServers.isNotEmpty())
    assertTrue(VpnController.protocols.isNotEmpty())
    assertEquals(ConnectionStatus.DISCONNECTED, VpnController.connectionStatus.value)
    assertEquals(ProtocolType.WIREGUARD_TURBO, VpnController.selectedProtocol.value.type)
  }
}
