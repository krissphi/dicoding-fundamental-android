package com.krissphi.id.architecture_component_app.live_data

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.krissphi.id.architecture_component_app.R
import com.krissphi.id.architecture_component_app.databinding.ActivityLdactivityBinding

class LDActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLdactivityBinding
    private lateinit var liveDataTimerViewModel: LDViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLdactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        liveDataTimerViewModel = ViewModelProvider(this)[LDViewModel::class.java]
        subscribe()
    }

    private fun subscribe() {
        val elapsedTimeObserver = Observer<Long?> { aLong ->
            val newText = this@LDActivity.resources.getString(R.string.seconds, aLong)
            binding.timerTextview.text = newText
        }
        liveDataTimerViewModel.getElapsedTime().observe(this, elapsedTimeObserver)
    }

}