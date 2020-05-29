package com.example.atm.services

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.delay
import kotlin.math.log

class HelloService : IntentService("HelloService") {
    companion object{
        val TAG = HelloService::class.java.simpleName
        val ACRION_MESSAGE_SEND = "message_sent"
    }

    override fun onHandleIntent(intent: Intent?) {
        val message = intent?.getStringExtra("extra_MSG")
        Log.d(TAG,"Sending")
        Thread.sleep(2000)
        Log.d(TAG,"$message sent")
        sendBroadcast(Intent(ACRION_MESSAGE_SEND))
    }
}
