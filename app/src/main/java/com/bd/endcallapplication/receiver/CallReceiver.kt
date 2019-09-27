package com.bd.endcallapplication.receiver

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let {
            if(!p1!!.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)){
                return
            }

            val telephonyManager: TelephonyManager =
                p0.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            val phoneNumber = p1!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            when (telephonyManager.callState) {
                // call Ring
                TelephonyManager.CALL_STATE_RINGING -> { moveToSerivce(p0, p1, phoneNumber) }
                // call End
                TelephonyManager.CALL_STATE_IDLE -> { }
                // call offhook
                TelephonyManager.CALL_STATE_OFFHOOK -> { }
                else -> { "YOUR NONE WORK" }
            }
        }
    }

    private fun moveToSerivce(context: Context, intent: Intent, phoneNumber: String){
        val endcallIntent = Intent(context, EndCallService::class.java)
        endcallIntent.action = intent.action
        endcallIntent.putExtra(TelephonyManager.EXTRA_STATE, intent.getStringExtra(TelephonyManager.EXTRA_STATE))
        endcallIntent.putExtra("phone_number", phoneNumber)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(endcallIntent)
        } else {
            context.startService(endcallIntent)
        }
    }
}