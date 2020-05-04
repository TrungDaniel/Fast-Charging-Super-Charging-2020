package com.trungdaniel.fastcharging_supercharging2020

import android.app.ActivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast







class CleanRamActivity : AppCompatActivity() {
    val mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean_ram)
        toast()
    }

    fun toast() {
        val actvityManager =
            applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val procInfos = actvityManager.runningAppProcesses

        for (pnum in procInfos.indices) {
            if (!procInfos[pnum].processName.contains("cleanram")) {
                actvityManager.killBackgroundProcesses(procInfos[pnum].processName)
            }
        }
        Toast.makeText(this, "System Clean :)", Toast.LENGTH_SHORT).show()
        finish()
    }


}
