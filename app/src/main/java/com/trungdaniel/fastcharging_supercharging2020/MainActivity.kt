package com.trungdaniel.fastcharging_supercharging2020

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yuan.waveview.WaveView
import kotlinx.android.synthetic.main.activity_main.*
import android.os.BatteryManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.view.Window
import android.view.WindowManager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        getInformation()
        btn_optimize.setOnClickListener {
            val intent = Intent(this, CleanRamActivity::class.java)
            startActivity(intent)
        }

    }


    fun checkWifi() {
        val manager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting
        if (isWifi == true) {
            tb_wifi.isChecked = true
        } else {
            tb_wifi.isChecked = false
        }

    }

    private inner class mNhietDoInfoReceiver : BroadcastReceiver() {

        internal var temp = 0

        internal val _temp: Float
            get() = (temp / 10).toFloat()

        override fun onReceive(arg0: Context, intent: Intent) {
            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            cv_nhiet_do.setValue(temp.toFloat()/10)
        }


    }

    private fun getInformation() {
        this.registerReceiver(this.mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        checkWifi()

        this.registerReceiver(
            this.mNhietDoInfoReceiver(),
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    val mBatInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            tv_pin.setText(level.toString() + "%")
            initWaveView(level)
        }
    }

    private fun initWaveView(level: Int) {
        waveview.setMode(WaveView.MODE_CIRCLE)
        waveview.setWaveColor(Color.parseColor("#00e200"))
        waveview.setbgColor(Color.parseColor("#1467a9"))
        waveview.setSpeed(WaveView.SPEED_FAST)
        waveview.setProgress(level.toLong())
        waveview.setMax(100)
    }
}
