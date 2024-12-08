package de.bp.backposition

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanSettings
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xsens.dot.android.sdk.DotSdk
import com.xsens.dot.android.sdk.events.DotData
import com.xsens.dot.android.sdk.interfaces.DotDeviceCallback
import com.xsens.dot.android.sdk.interfaces.DotScannerCallback
import com.xsens.dot.android.sdk.models.FilterProfileInfo
import com.xsens.dot.android.sdk.utils.DotScanner
import de.bp.backposition.ui.theme.BackPositionTheme
import java.util.ArrayList

class MainActivity : ComponentActivity(), DotDeviceCallback, DotScannerCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DotSdk.setDebugEnabled(true)
        DotSdk.setReconnectEnabled(true)
        enableEdgeToEdge()
        setContent {
            BackPositionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        val bltScanner = DotScanner(this, this)
        bltScanner.setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        bltScanner.startScan()
    }

    override fun onDotConnectionChanged(p0: String?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onDotServicesDiscovered(p0: String?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onDotFirmwareVersionRead(p0: String?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun onDotTagChanged(p0: String?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun onDotBatteryChanged(p0: String?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    override fun onDotDataChanged(p0: String?, p1: DotData?) {
        TODO("Not yet implemented")
    }

    override fun onDotInitDone(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onDotButtonClicked(p0: String?, p1: Long) {
        TODO("Not yet implemented")
    }

    override fun onDotPowerSavingTriggered(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onReadRemoteRssi(p0: String?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onDotOutputRateUpdate(p0: String?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onDotFilterProfileUpdate(p0: String?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onDotGetFilterProfileInfo(p0: String?, p1: ArrayList<FilterProfileInfo>?) {
        TODO("Not yet implemented")
    }

    override fun onSyncStatusUpdate(p0: String?, p1: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onDotScanned(p0: BluetoothDevice, p1: Int) {
        TODO("Not yet implemented")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BackPositionTheme {
        Greeting("Android")
    }
}