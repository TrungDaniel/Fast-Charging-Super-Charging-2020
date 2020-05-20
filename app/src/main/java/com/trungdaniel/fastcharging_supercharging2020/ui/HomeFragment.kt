package com.trungdaniel.fastcharging_supercharging2020.ui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception


class HomeFragment : Fragment() {
    var stopTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            com.trungdaniel.fastcharging_supercharging2020.R.layout.fragment_home,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInformation()
        btn_optimize.setOnClickListener {
            /*val intent = Intent(context, CleanRamActivity::class.java)
            startActivity(intent)*/
        }


    }


    private inner class mNhietDoInfoReceiver : BroadcastReceiver() {

        internal var temp = 0

        internal val _temp: Float
            get() = (temp / 10).toFloat()

        override fun onReceive(arg0: Context, intent: Intent) {
            try {
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
                val tam = temp.toFloat() / 10
                cv_nhiet_do.setValue(tam)
                tv_nhiet_do.setText(tam.toString())


            } catch (e: Exception) {

            }

        }

    }

    object Power {
        fun isConnected(context: Context): Boolean {
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val plugged = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS
        }
    }

    private inner class CheckPower : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val intent =
                context?.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val plugged = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)

            plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS
            Toast.makeText(context, "" + plugged, Toast.LENGTH_SHORT).show()

            if (plugged == 2 || plugged == 1) {
                tb_sac_pin.isChecked = true
                tb_binh_thuong.isChecked = false
                stopTime = chronometer1.base - SystemClock.elapsedRealtime()
                chronometer1.base = SystemClock.elapsedRealtime() + stopTime
                chronometer1.start()
            }
            if (plugged == 0) {
                tb_sac_pin.isChecked = false
                stopTime = 0
                chronometer1.base = SystemClock.elapsedRealtime()
                chronometer1.stop()
                tb_binh_thuong.isChecked = true
            }

        }

    }

    private inner class CheckVol : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val b = context?.registerReceiver(null, ifilter)
            try {
                if (b != null) {
                    val vol = b.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) / 1000
                    Toast.makeText(context, "" + vol.toFloat(), Toast.LENGTH_SHORT).show()
                    cv_vol.setValue(vol.toFloat())
                    tv_vol.setText(vol.toString())
                }
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
        context!!.registerReceiver(
            this.CheckPower(),
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        context!!.registerReceiver(
            this.CheckVol(),
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
