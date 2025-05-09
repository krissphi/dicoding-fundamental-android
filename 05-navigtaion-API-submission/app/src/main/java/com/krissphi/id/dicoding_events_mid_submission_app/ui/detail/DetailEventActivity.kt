package com.krissphi.id.dicoding_events_mid_submission_app.ui.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.krissphi.id.dicoding_events_mid_submission_app.R
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.ActivityDetailEventBinding
import androidx.core.net.toUri
import com.krissphi.id.dicoding_events_mid_submission_app.util.formatTime
import androidx.core.graphics.drawable.toDrawable

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Event"
        supportActionBar?.setBackgroundDrawable(Color.WHITE.toDrawable())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event_data", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("event_data")
        }
        event?.let {
            Glide.with(this)
                .load(it.mediaCover)
                .into(binding.ivCover)
            binding.tvEventName.text = it.name
            binding.tvEventOwner.text = it.ownerName
            binding.tvEventCity.text = it.cityName
            binding.tvEventTime.text = formatTime(it.beginTime ?: "")
            binding.tvEventQuota.text = (it.quota?.minus(it.registrants!!)).toString()

            binding.tvDescription.text = HtmlCompat.fromHtml(
                it.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            val link = it.link
            binding.btnReg.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, link?.toUri() )
                startActivity(intent)
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}