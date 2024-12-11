package de.bp.backposition
import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanSettings
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.xsens.dot.android.sdk.DotSdk
import com.xsens.dot.android.sdk.events.DotData
import com.xsens.dot.android.sdk.interfaces.DotDeviceCallback
import com.xsens.dot.android.sdk.interfaces.DotScannerCallback
import com.xsens.dot.android.sdk.interfaces.DotSyncCallback
import com.xsens.dot.android.sdk.models.DotDevice
import com.xsens.dot.android.sdk.models.DotPayload.PAYLOAD_TYPE_ORIENTATION_EULER
import com.xsens.dot.android.sdk.models.FilterProfileInfo
import com.xsens.dot.android.sdk.utils.DotScanner
import de.bp.backposition.ui.theme.BackPositionTheme
import java.util.HashMap


class MainActivity : ComponentActivity(), DotDeviceCallback, DotScannerCallback, DotSyncCallback {
    val discoveredDots = ArrayList<String>()
    val connectedDots = ArrayList<DotDevice>()
    val angleX1 = mutableStateOf("N/A")
    val angleY1 = mutableStateOf("N/A")
    val angleZ1 = mutableStateOf("N/A")
    val angleX2 = mutableStateOf("N/A")
    val angleY2 = mutableStateOf("N/A")
    val angleZ2 = mutableStateOf("N/A")
    val diffAngle = mutableStateOf("N/A")
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
        val shownTextX1 by angleX1
        val shownTextY1 by angleY1
        val shownTextZ1 by angleZ1

        val shownTextX2 by angleX2
        val shownTextY2 by angleY2
        val shownTextZ2 by angleZ2

        val shownDiff by diffAngle

        Column {
            RequestBluetoothPermission()
            Text("Sensor 1:")
            Text(text = "X: "+ shownTextX1)
            Text(text = "Y: "+ shownTextY1)
            Text(text = "Z: "+ shownTextZ1)
            Text("Sensor 2:")
            Text(text = "X: "+ shownTextX2)
            Text(text = "Y: "+ shownTextY2)
            Text(text = "Z: "+ shownTextZ2)
            Text("Differenz X:")
            Text(text = shownDiff, fontSize = 50.sp)
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

    override fun onDotDataChanged(address: String?, dotData: DotData?) {
        Log.d("BT", "Data Changed")
        if (address == connectedDots[0].address){
            angleX1.value = dotData?.euler!![0].toString()
            angleY1.value = dotData?.euler!![1].toString()
            angleZ1.value = dotData?.euler!![2].toString()
        }else{
            angleX2.value = dotData?.euler!![0].toString()
            angleY2.value = dotData?.euler!![1].toString()
            angleZ2.value = dotData?.euler!![2].toString()
        }
        diffAngle.value = (angleX1.value.toFloat() - angleX2.value.toFloat()).toString()
    }

    override fun onDotInitDone(address: String?) {
        Log.d("BT", "Init Done")
        Log.d("BT", "--------FIRST DATA--------")
        Log.d("BT", connectedDots[0].firmwareVersion)
        Log.d("BT", "Battery: " + connectedDots[0].batteryPercentage)
        //connectedDots[0].identifyDevice()

        // connectedDots[0].measurementMode = PAYLOAD_TYPE_ORIENTATION_EULER
        // connectedDots[0].startMeasuring()

        for (dot in connectedDots){
            if(dot.address == address){
                dot.measurementMode = PAYLOAD_TYPE_ORIENTATION_EULER
                dot.startMeasuring()
            }
        }

//        if (!connectedDots[0].isSynced){
//            connectedDots[0].isRootDevice = true
//            DotSyncManager.getInstance(this).startSyncing(connectedDots, 666)
//        }else{
//            Log.d("BT", "Already Synced")
//        }

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
                    Log.d("BT", connectedDots.toString())
                    Log.d("BT", "Connected to: $name")
                }
            }else{
                Log.d("BT", "Device $name already discovered")
            }
        }
    }


    // Syncing
    override fun onSyncingStarted(p0: String?, p1: Boolean, p2: Int) {
        Log.d("BT", "Syncing Started")
    }

    override fun onSyncingProgress(p0: Int, p1: Int) {
        Log.d("BT", "Syncing Progress " + p0 +" "+ p1)
    }

    override fun onSyncingResult(p0: String?, p1: Boolean, p2: Int) {
        Log.d("BT", "Syncing Result" + p1)
    }

    override fun onSyncingDone(syncingResultMap: HashMap<String, Boolean>?, isSuccess: Boolean, p2: Int) {
        Log.d("BT", "Syncing Done")
        if (isSuccess){
            connectedDots[0].measurementMode = PAYLOAD_TYPE_ORIENTATION_EULER
            connectedDots[0].startMeasuring()
        }
    }

    override fun onSyncingStopped(p0: String?, p1: Boolean, p2: Int) {
        Log.d("BT", "Syncing Stopped")
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