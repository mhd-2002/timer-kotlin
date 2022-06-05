package com.example.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class TimerServes : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getDoubleExtra(TIMER_Extra, 0.0)
        timer.scheduleAtFixedRate(TimerTask(time), 0, 1000)
        return START_NOT_STICKY
    }

     inner class TimerTask(var time: Double) : java.util.TimerTask() {

        override fun run() {

            val intent = Intent(TIMER_UPDATED)
            time++
            intent.putExtra(TIMER_Extra , time)
            sendBroadcast(intent)
        }

    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    companion object {
        const val TIMER_UPDATED = "timerUpdated"
        const val TIMER_Extra = "timerExtra"
    }

}