package com.dagorik.bluetooth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import android.bluetooth.BluetoothAdapter
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat
import com.github.douglasjunior.bluetoothclassiclibrary.*
import android.widget.Toast
import android.content.pm.PackageManager




class Main2Activity : AppCompatActivity(), OnClickListener {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private var itemList = ArrayList<BluetoothDevice>()

    private lateinit var service: BluetoothService
    private lateinit var config: BluetoothConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        viewAdapter = BluetoothListAdapter(itemList, this)

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@Main2Activity)
            adapter = viewAdapter
        }

        config = BluetoothConfiguration()
        config.context = applicationContext
        config.bluetoothServiceClass = BluetoothClassicService::class.java
        config.bufferSize = 1024
        config.characterDelimiter = '\n'
        config.deviceName = "Example Blue"
        config.uuidService = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
        //config.uuidCharacteristic = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        config.transport = BluetoothDevice.TRANSPORT_AUTO
        config.callListenersInMainThread = true



        BluetoothService.init(config)

        service = BluetoothService.getDefaultInstance()

        service.setOnScanCallback(object : BluetoothService.OnBluetoothScanCallback {
            override fun onDeviceDiscovered(device: BluetoothDevice, rssi: Int) {
                Log.e("Evento", device.toString())
                Log.e("Evento", rssi.toString())
                itemList.add(device)
                viewAdapter.notifyDataSetChanged()
            }

            override fun onStartScan() {
                Log.e("Evento", "Emepzo")
            }

            override fun onStopScan() {
                Log.e("Evento", "termino")
            }
        })

        service.startScan()


        service.setOnEventCallback(object : BluetoothService.OnBluetoothEventCallback {
            override fun onDataRead(buffer: ByteArray, length: Int) {
                Log.e("onDataRead", "x")
            }

            override fun onStatusChange(status: BluetoothStatus) {
                Log.e("onStatusChange", status.toString())
            }

            override fun onDeviceName(deviceName: String) {
                Log.e("onDeviceName", deviceName);
            }

            override fun onToast(message: String) {
                Log.e("onToast", message)
            }

            override fun onDataWrite(buffer: ByteArray) {
                Log.e("onDataWrite", "x")
            }
        })
    }

    override fun onClick(bluetoothDeviceModel: BluetoothDevice) {
        Log.e("CLICK", bluetoothDeviceModel.name)
        Log.e("CLICK","----" + bluetoothDeviceModel.uuids )
//        config.uuid = UUID.fromString(bluetoothDeviceModel.uuids[0].toString()) // Required
//        config.uuidCharacteristic = UUID.fromString(bluetoothDeviceModel.uuids[0].toString()) // Required
//        config.transport = BluetoothDevice.TRANSPORT_LE;
        val x = BluetoothDeviceDecorator(bluetoothDeviceModel)
        service.connect(x.device)

    }
}
