package com.krissphi.id.dicoding_events_mid_submission_app.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.krissphi.id.dicoding_events_mid_submission_app.R
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.ActivitySettingsBinding
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var workManager: WorkManager
    private lateinit var mainViewModel: MainViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val switchTheme = binding.switchTheme
        val switchReminder = binding.switchReminder

        val themePref = SettingPreferences.getInstance(application.dataStore)
        val reminderPref = ReminderPreferences.getInstance(application.reminderDataStore)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(themePref, reminderPref))[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        val instance = WorkManager.getInstance(this)
        workManager = instance

        mainViewModel.getReminderSettings().observe(this) { isReminderActive ->
            switchReminder.isChecked = isReminderActive
            if (isReminderActive) {
                startPeriodicTask()
            }
        }

        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= 33) {
                    val permissionCheck = ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        switchReminder.isChecked = false
                        return@setOnCheckedChangeListener
                    }
                }

                mainViewModel.saveReminderSetting(true)
                startPeriodicTask()
            } else {
                mainViewModel.saveReminderSetting(false)
                cancelPeriodicTask()
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun startPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            ReminderWorkManager::class.java,
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .addTag("daily_reminder")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "DailyReminderWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

    }


    private fun cancelPeriodicTask() {
        val workIdStr = getSharedPreferences("reminder_prefs", MODE_PRIVATE)
            .getString("reminder_work_id", null)

        workIdStr?.let {
            workManager.cancelWorkById(java.util.UUID.fromString(it))
        }
    }
}