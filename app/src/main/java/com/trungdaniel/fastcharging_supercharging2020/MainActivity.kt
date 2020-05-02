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
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Menu


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
    }



    private fun getInformation() {
        this.registerReceiver(this.mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
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
        //waveview.setProgress(level.toLong())
        waveview.setProgress(60)
        waveview.setMax(100)
    }
}
