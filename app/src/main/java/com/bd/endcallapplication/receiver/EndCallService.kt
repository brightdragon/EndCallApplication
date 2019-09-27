package com.bd.endcallapplication.receiver

import android.app.KeyguardManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import java.lang.Exception

class EndCallService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        notification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val phoneNumber = intent.getStringExtra("phone_number")

            if ("ENDCALL_NUMBER" == phoneNumber) {
                endCall(this).apply {
                    stopForeground(true)
                    stopSelf()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun notification() {
        val channelId = "com.cb.endcallapplication"
        val channelName = "EndCallService"

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notificationId = 1
        val builder = NotificationCompat.Builder(this, channelId)
        builder.setAutoCancel(true)
        startForeground(notificationId, builder.build())
    }


    private fun endCall(context: Context): Boolean {
        return try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val classTelephony = Class.forName(telephonyManager.javaClass.name)
            val iTelephony = classTelephony.getDeclaredMethod("getITelephony")
            iTelephony.isAccessible = true
            val telephonyInterface = iTelephony.invoke(telephonyManager)
            val telephonyInterfaceClass = Class.forName(telephonyInterface.javaClass.name)
            val methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall")
            methodEndCall.invoke(telephonyInterface)
            true
        } catch (e: Exception) {
            Log.e("BrightDragon", "endcall Exception Message : ${e.message}")
            e.printStackTrace()
            false
        }
    }

}