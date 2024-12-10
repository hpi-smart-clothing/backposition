package de.bp.backposition
import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
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
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.xsens.dot.android.sdk.models.DotDevice
import kotlin.contracts.contract

class MainActivity : ComponentActivity(), DotDeviceCallback, DotScannerCallback {
    val discoveredDots = ArrayList<String>()
    val connectedDots = ArrayList<DotDevice>()
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
        val permissions = listOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
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
        Log.d("BT", "Connectionnnnn Changed"+ p0)
    }

    override fun onDotServicesDiscovered(address: String?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS){
            Log.d("BT", "Services Discovered")
        }
    }

    override fun onDotFirmwareVersionRead(address: String?, version: String?) {
        Log.d("BT", "Firmware Version:" + version)
    }

    override fun onDotTagChanged(p0: String?, p1: String?) {
        Log.d("BT", "Tag Changed")
    }

    override fun onDotBatteryChanged(p0: String?, p1: Int, p2: Int) {
        Log.d("BT", "Battery Changed")
    }

    override fun onDotDataChanged(p0: String?, p1: DotData?) {
        Log.d("BT", "Data Changed")
    }

    override fun onDotInitDone(p0: String?) {
        Log.d("BT", "Init Done")
        Log.d("BT", "--------FIRST DATA--------")
        Log.d("BT", connectedDots[0].firmwareVersion)
        Log.d("BT", "Battery: " + connectedDots[0].batteryPercentage)
        connectedDots[0].identifyDevice()

    }

    override fun onDotButtonClicked(p0: String?, p1: Long) {
        Log.d("BT", "Button Clicked")
    }

    override fun onDotPowerSavingTriggered(p0: String?) {
        Log.d("BT", "Power Saving Triggered")
    }

    override fun onReadRemoteRssi(p0: String?, p1: Int) {
        Log.d("BT", "Read Remote Rssi")
    }

    override fun onDotOutputRateUpdate(p0: String?, p1: Int) {
        Log.d("BT", "Output Rate Update")
    }

    override fun onDotFilterProfileUpdate(p0: String?, p1: Int) {
        Log.d("BT", "Filter Profile Update")
    }

    override fun onDotGetFilterProfileInfo(p0: String?, p1: ArrayList<FilterProfileInfo>?) {
        Log.d("BT", "Get Filter Profile Info")
    }

    override fun onSyncStatusUpdate(p0: String?, p1: Boolean) {
        Log.d("BT", "Sync Status Update")
    }

    override fun onDotScanned(p0: BluetoothDevice, p1: Int) {
        val name = p0.name
        val address = p0.address
        if (address != null) {
            if(!discoveredDots.contains(address)){
                discoveredDots.add(address)
                val newDot = DotDevice(applicationContext, p0, this)
                Log.d("BT", newDot.toString())
                Log.d("BT", "Found Device: $name")
                if (name != null) {
                    newDot.connect()
                    connectedDots.add(newDot)
                    Log.d("BT", "Connected to: $name")
                }
            }else{
                Log.d("BT", "Device $name already discovered")
            }
        }
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