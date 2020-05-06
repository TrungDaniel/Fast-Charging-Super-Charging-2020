package com.trungdaniel.fastcharging_supercharging2020.ui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trungdaniel.fastcharging_supercharging2020.CleanRamActivity
import com.trungdaniel.fastcharging_supercharging2020.R
import com.yuan.waveview.WaveView
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInformation()
        btn_optimize.setOnClickListener {
            val intent = Intent(context, CleanRamActivity::class.java)
            startActivity(intent)
        }
    }


    private inner class mNhietDoInfoReceiver : BroadcastReceiver() {

        internal var temp = 0

        internal val _temp: Float
            get() = (temp / 10).toFloat()

        override fun onReceive(arg0: Context, intent: Intent) {
            try {
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
                cv_nhiet_do.setValue(temp.toFloat() / 10)
            } catch (e: Exception) {

            }

        }


    }

    private fun getInformation() {
        context!!.registerReceiver(
            this.mBatInfoReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        checkWifi()

        context!!.registerReceiver(
            this.mNhietDoInfoReceiver(),
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    val mBatInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            try {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                tv_pin.setText(level.toString() + "%")
                initWaveView(level)
            } catch (e: Exception) {

            }

        }
    }

    private fun initWaveView(level: Int) {
        waveview.setProgressValue(level)

    }

    fun checkWifi() {
        val manager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting
        if (isWifi == true) {
            tb_wifi.isChecked = true
        } else {
            tb_wifi.isChecked = false
        }

    }

}
