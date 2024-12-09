package de.bp.backposition
import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanSettings
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
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
import android.util.Log
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import kotlin.contracts.contract

class MainActivity : ComponentActivity(), DotDeviceCallback, DotScannerCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DotSdk.setDebugEnabled(true)
        DotSdk.setReconnectEnabled(true)
        enableEdgeToEdge()
        setContent {
            BackPositionTheme {
                Content()
            }
        }
    }

    @Composable
    fun Content(){
        Column {
            Button(onClick = {}) {Text(text = "request Permission") }
            RequestBluetoothPermission()
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestBluetoothPermission() {
        Log.d("BT", "Start Looking")
        val permissions = listOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION)
        val bluetoothPermissionState = rememberMultiplePermissionsState(
            permissions = permissions
        )

        if (bluetoothPermissionState.allPermissionsGranted) {
            Text("Bluetooth permission granted")
            val bltScanner = DotScanner(applicationContext, this)
            bltScanner.setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            bltScanner.startScan()
            Log.d("BT", "Start Scanning")
            // Your code that uses Bluetooth functionality
        } else {
            Button(onClick = { bluetoothPermissionState.launchMultiplePermissionRequest() }) {
                Text("Request Bluetooth permission")
            }
        }
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
        println("onDotScannedHalloooo")
        Log.d("BT", "Haloooo")
        val name = p0.name
        val adress = p0.address
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