package com.example.timer

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timer.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btStart.setOnClickListener { startStopTimer() }
        binding.btReset.setOnClickListener { resetTimer() }

        serviceIntent = Intent(this, TimerServes::class.java)
        registerReceiver(updateTimer, IntentFilter(TimerServes.TIMER_UPDATED))

    }
    private val updateTimer: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerServes.TIMER_Extra, 0.0)
            binding.tvTimer.text = getTimeStringFromDouble(time)
        }

    }

    private fun getTimeStringFromDouble(time : Double): String {
        val resualt = time.roundToInt()
        val hours = resualt % 86400 / 3600
        val min = resualt % 86400 % 3600 / 60
        val sec = resualt % 86400 % 3600 % 60
        return makeTimeString(hours, min, sec)
    }

    private fun makeTimeString(hours: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hours, min, sec)

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.tvTimer.text = getTimeStringFromDouble(time)
    }

    private fun startStopTimer() {

        if (timerStarted){
            stopTimer()
        }else{
            startTimer()
        }

    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun startTimer() {
        serviceIntent.putExtra(TimerServes.TIMER_Extra ,time)
        startService(serviceIntent)
        binding.btStart.text = "Stop"
        binding.btStart.icon = getDrawable(R.drawable.ic_baseline_pause_24)
        timerStarted = true
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun stopTimer() {
        stopService(serviceIntent)
        binding.btStart.text = "Start"
        binding.btStart.icon = getDrawable(R.drawable.ic_baseline_play_arrow_24)
        timerStarted = false
    }
}